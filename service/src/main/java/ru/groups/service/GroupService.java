package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.entity.typeOfMessages.BadMessages;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.security.SecurityServiceContext;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {
    @Autowired VkInformationService oauthService;
    @Autowired SecurityServiceContext session;
    @Autowired SessionFactory sessionFactory;
    @Autowired
    LoggedUserHelper loggedUserHelper;

    private static final String versionOfVkApi = "5.53";


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
        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
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


    @Transactional
    public void getAccessKeyOfGroups(String code, String groupId) throws Exception{
        String reqUrl = "https://oauth.vk.com/access_token?client_id=5499487&client_secret=bMTTeUDFad7H95I8LiIt&redirect_uri=http://localhost:8080/resources/admin.html&code={code}"
                .replace("{code}", code);
        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        long userId = session.getLoggedUserId();
        UserVk userVk = sessionFactory.openSession().get(UserVk.class, userId);
        List<GroupVk> groups =  userVk.getUserGroups();
        String accessToken = JsonParsingHelper.findValueInJson(actualObj, "access_token");//вот это гребаный ключ
        BadMessages badMessages = new BadMessages(); //тут создаем новый список с помощью конструктора плохих слов и добавляем его
        //  я хуй его знает как тут дописать добавление аксесса именно в нужную группу, тут скорее всего нужно принимать еще и айдишку группу
    }


    @Transactional
    public GroupVk searchGroup(String groupId){
        Query query = sessionFactory.getCurrentSession().createQuery("from GroupVk vkgroup join vkgroup.badMessages as messages where id = :id");
        query.setParameter("id", groupId);//Делаем запрос в БД с помощью HQL
        GroupVk groupVk = (GroupVk) query.uniqueResult();
        return groupVk;
    }
}
