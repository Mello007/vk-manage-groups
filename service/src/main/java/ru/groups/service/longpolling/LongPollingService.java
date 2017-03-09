package ru.groups.service.longpolling;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.messages.MessageService;
import ru.groups.service.GetUserInfoService;
//import ru.groups.service.help.JsonParsingHelper;
import java.io.IOException;
import java.util.List;

@Component
@Service
public class LongPollingService {

    private static final String versionOfVkApi = "5.59";
    private static final Integer KEY = 0;
    private static final Integer SERVER_ADDRESS = 1;
    private static final Integer NUMBER_OF_LAST_ACTION = 2;


    @Autowired SessionFactory sessionFactory;
    @Autowired GetUserInfoService oauthService;
    @Autowired MessageService messageService;


    private void addNewKeyServerTsToGroup(GroupVk groupVk, JsonNode actualObj){
    //    List<String> valuesInJson = JsonParsingHelper.findValueInJson(actualObj, "key", "server", "ts");
//        groupVk.setTempKeyOfPollingServer(valuesInJson.get(KEY));
//        groupVk.setAddressOfPollingServer(valuesInJson.get(SERVER_ADDRESS));
//        groupVk.setNumberOfLastAction(valuesInJson.get(NUMBER_OF_LAST_ACTION));
    }

    @Transactional
    public void getLongPolling(GroupVk groupVk) throws IOException {
//        String method = "messages.getLongPollServer";
//        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?access_token={ACCESS_TOKEN}&v={API_VERSION}"
//                .replace("{METHOD_NAME}", method)
//                .replace("{ACCESS_TOKEN}", groupVk.getAccessToken())
//                .replace("{API_VERSION}", versionOfVkApi);
//        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
//        addNewKeyServerTsToGroup(groupVk, actualObj);
//        requestToPollingServer(groupVk);
    }

    @Transactional
    private void requestToPollingServer(GroupVk groupVk) throws IOException {
//        String reqUrl = "https://{SERVER_ADDRESS}?act=a_check&key={GROUP_KEY}&ts={LATEST_ACTION}&wait=25&mode=2&version=1"
//                .replace("{SERVER_ADDRESS}", groupVk.getAddressOfPollingServer())
//                .replace("{GROUP_KEY}", groupVk.getTempKeyOfPollingServer())
//                .replace("{LATEST_ACTION}", groupVk.getNumberOfLastAction());
//        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
//        checkPollingServerAtErrors(actualObj, groupVk);
//        addNewKeyServerTsToGroup(groupVk, actualObj);
//        sessionFactory.getCurrentSession().merge(groupVk);
//        messageService.findMessageAndUserIdInResponse(actualObj, groupVk);
    }

    private void findErrorInServer(JsonNode actualObj, GroupVk groupVk) throws IOException{
//        String numberOfError = JsonParsingHelper.findValueInJson(actualObj ,"failed");
//        if (numberOfError != null){
//            switch (numberOfError){
//                case "1":
//                    groupVk.setNumberOfLastAction(groupVk.getNumberOfLastAction());
//                    requestToPollingServer(groupVk);
//                    break;
//                case "2":
//                    getLongPolling(groupVk);
//                    break;
//                case "3":
//                    getLongPolling(groupVk);
//                    break;
//                case "4":
//                    break;
//            }
//        }
    }

    private void checkPollingServerAtErrors(JsonNode actualObj, GroupVk groupVk) throws IOException {
//        groupVk.setNumberOfLastAction(JsonParsingHelper.findValueInJson(actualObj ,"ts"));
//        String updates = JsonParsingHelper.findValueInJson(actualObj ,"updates");
//        if (updates == null || updates.equals("[]")){
//            requestToPollingServer(groupVk);
//        }
//        findErrorInServer(actualObj, groupVk);
    }
}
