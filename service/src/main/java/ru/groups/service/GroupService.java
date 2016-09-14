package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.security.SecurityServiceContext;

import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {
    @Autowired OauthService oauthService;
    @Autowired SecurityServiceContext session;
    @Autowired SessionFactory sessionFactory;


    private static final String versionOfVkApi = "5.53";


    @Transactional
    public List<GroupVk> getUserGroups(UserVk user) throws Exception{
        List<GroupVk> groupVks = new LinkedList<>();
        String method = "groups.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}&extended=1&filter=admin&access_token={access_token}&v={version}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", user.getUserId())
                .replace("{access_token}", user.getUserAccessToken())
                .replace("{version}", versionOfVkApi);
        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        Integer countOfgroups = Integer.getInteger(oauthService.findValueInJson(actualObj, "count"));
        for(int i = 0; i<countOfgroups; i++){
            GroupVk group = new GroupVk();
            group.setGroupId(oauthService.findValueInJson(actualObj, "id"));
            group.setGroupName(oauthService.findValueInJson(actualObj, "name"));
            group.setPhoto50px(oauthService.findValueInJson(actualObj, "photo_50"));
            group.setPhoto100px(oauthService.findValueInJson(actualObj, "photo_100"));
            groupVks.add(group);
        }
        user.setUserGroups(groupVks); //добавляем юзеру группы его
        return groupVks;
    }

    @Transactional
    public List<GroupVk> getGroupsFromBD(){
        long userId = session.getLoggedUserId();
        UserVk userVk = sessionFactory.openSession().get(UserVk.class, userId);
        List<GroupVk> groupList = userVk.getUserGroups();
        groupList.forEach(group -> userVk.getUserGroups());
        return groupList;
    }

    public void getAccessKeyOfGroups(String code) throws Exception{
        String reqUrl = "https://oauth.vk.com/access_token?client_id=5499487&client_secret=bMTTeUDFad7H95I8LiIt&redirect_uri=http://localhost:8080/resources/admin.html&code={code}"
                .replace("{code}", code);
        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        long userId = session.getLoggedUserId();
        UserVk userVk = sessionFactory.openSession().get(UserVk.class, userId);
        List<GroupVk> groups =  userVk.getUserGroups();
        String accessToken = oauthService.findValueInJson(actualObj, "access_token"); //вот это гребаный ключ
        //  я хуй его знает как тут дописать добавление аксесса именно в нужную группу, тут скорее всего нужно принимать еще и айдишку группу
    }



}
