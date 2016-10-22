package ru.groups.service.longpolling;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.VkInformationService;
import ru.groups.service.help.JsonParsingHelper;

import java.io.IOException;

@Service
public class LongPollingService {

    private static final String versionOfVkApi = "5.59";

    @Autowired SessionFactory sessionFactory;
    @Autowired VkInformationService oauthService;

    @Transactional
    public void getLongPolling(GroupVk groupVk) throws IOException {
        String method = "messages.getLongPollServer";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?access_token={ACCESS_TOKEN}&v={API_VERSION}"
                .replace("{METHOD_NAME}", method)
                .replace("{ACCESS_TOKEN}", groupVk.getAccessToken())
                .replace("{API_VERSION}", versionOfVkApi);
        System.out.println(reqUrl);
        StringBuffer response = oauthService.apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        groupVk.setTempKeyOfPollingServer(JsonParsingHelper.findValueInJson(actualObj ,"key"));
        groupVk.setAddressOfPollingServer(JsonParsingHelper.findValueInJson(actualObj ,"server"));
        groupVk.setNumberOfLastAction(JsonParsingHelper.findValueInJson(actualObj ,"ts"));
        sessionFactory.getCurrentSession().merge(groupVk);
        requestToPollingServer(groupVk);
    }

    // Here I need to add annotation @Sheduled
    @Transactional
    public void requestToPollingServer(GroupVk groupVk) throws IOException {

        String reqUrl = "https://{SERVER_ADDRESS}?act=a_check&key={GROUP_KEY}&ts={LATEST_ACTION}&wait=25&mode=2&version=1"
                .replace("{SERVER_ADDRESS}", groupVk.getAddressOfPollingServer())
                .replace("{GROUP_KEY}", groupVk.getTempKeyOfPollingServer())
                .replace("{LATEST_ACTION}", groupVk.getNumberOfLastAction());

        StringBuffer response = oauthService.apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());

        checkPollingServerAtErrors(actualObj, groupVk);


        groupVk.setTempKeyOfPollingServer(JsonParsingHelper.findValueInJson(actualObj ,"key"));
        groupVk.setAddressOfPollingServer(JsonParsingHelper.findValueInJson(actualObj ,"server"));
        groupVk.setNumberOfLastAction(JsonParsingHelper.findValueInJson(actualObj ,"ts"));
        sessionFactory.getCurrentSession().merge(groupVk);
    }



    public void getNewMessages(JsonNode actualObj, GroupVk groupVk){
        
    }


    private void checkPollingServerAtErrors(JsonNode actualObj, GroupVk groupVk) throws IOException {
        String newTs = JsonParsingHelper.findValueInJson(actualObj ,"ts");
        String updates = JsonParsingHelper.findValueInJson(actualObj ,"updates");

        if (updates == null || updates.equals("[]")){
            groupVk.setNumberOfLastAction(newTs);
            requestToPollingServer(groupVk);
        }

        String numberOfError = JsonParsingHelper.findValueInJson(actualObj ,"failed");
        if (numberOfError != null){
            switch (numberOfError){
                case "1":
                    groupVk.setNumberOfLastAction(newTs);
                    requestToPollingServer(groupVk);
                    break;
                case "2":
                    getLongPolling(groupVk);
                    break;
                case "3":
                    getLongPolling(groupVk);
                    break;
                case "4":
                    break;
            }
        }
    }
}
