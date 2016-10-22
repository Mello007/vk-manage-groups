package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.longpolling.LongPollingService;
import ru.groups.service.security.Session;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {
    @Autowired VkInformationService oauthService;
    @Autowired Session session;
    @Autowired SessionFactory sessionFactory;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired LongPollingService longPollingService;



    private static final String versionOfVkApi = "5.59";

    @Transactional
    public List<GroupVk> getUserGroups() throws Exception{
        UserVk user = loggedUserHelper.getUserFromBD();
        List<GroupVk> groupVks = new LinkedList<>();
        String method = "groups.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}&extended=1&filter=admin&access_token={access_token}&v={version}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", user.getUserId())
                .replace("{access_token}", user.getUserAccessToken())
                .replace("{version}", versionOfVkApi);
        StringBuffer response = oauthService.apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        JsonNode massivJson = actualObj.get("response");
        JsonNode slaidsNode = (ArrayNode) massivJson.get("items");
        Iterator<JsonNode> slaidsIterator = slaidsNode.elements();
        while (slaidsIterator.hasNext()) {
            JsonNode slaidNode = slaidsIterator.next();
            GroupVk group = new GroupVk();
            group.setGroupId(slaidNode.get("id").asText());
            group.setGroupName(slaidNode.get("name").asText());
            group.setPhoto50px(slaidNode.get("photo_50").asText());
            group.setPhoto100px(slaidNode.get("photo_100").asText());
            sessionFactory.getCurrentSession().save(group);
            groupVks.add(group);
        }
        user.setUserGroups(groupVks); //добавляем юзеру группы его
        sessionFactory.getCurrentSession().merge(user);
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

    @Transactional
    public GroupVk searchGroup(String groupId){
        Query query = sessionFactory.getCurrentSession().createQuery("from GroupVk vkgroup join vkgroup.badMessages as messages where id = :id");
        query.setParameter("id", groupId);//Делаем запрос в БД с помощью HQL
        GroupVk groupVk = (GroupVk) query.uniqueResult();
        return groupVk;
    }




    public void setAccessTokenToGroup(String accessTokenToGroup, String groupId) throws IOException {
        Query query = sessionFactory.getCurrentSession().createQuery("from GroupVk where groupid = :groupid");
        query.setParameter("groupid", groupId);//Делаем запрос в БД с помощью HQL
        GroupVk groupVk = (GroupVk) query.uniqueResult();
        groupVk.setAccessToken(accessTokenToGroup);
        sessionFactory.getCurrentSession().merge(groupVk);
        longPollingService.getLongPolling(groupVk);

    }




//    This method isn't work with website :( I didn't delete him, because I can find other ways
//    @Transactional
//    public void getAccessKeyOfGroups(String code) throws Exception {
//        String reqUrl = ("https://oauth.vk.com/access_token?client_id=5499487&client_secret=bMTTeUDFad7H95I8LiIt&redirect_uri=http://localhost:8080/groups/getaccess&code={code}")
//                .replace("{code}", code);
//        StringBuffer response = oauthService.apiRequestForGetResponseFromServer(reqUrl);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode actualObj = mapper.readTree(response.toString());
//        long userId = session.getLoggedUserId();
//        UserVk userVk = sessionFactory.openSession().get(UserVk.class, userId);
//        List<GroupVk> groups =  userVk.getUserGroups();
//        String accessToken = JsonParsingHelper.findValueInJson(actualObj, "access_token");//вот это гребаный ключ
//    }
}
