package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
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
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
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


    @Transactional
    public void setAccessTokenToGroup(String accessTokenToGroup, String groupId) throws IOException {
        //It's solution, which get groupVk per ID from BD
        Query query = sessionFactory.getCurrentSession().createQuery("from GroupVk where groupid = :groupid");
        query.setParameter("groupid", groupId);
        GroupVk groupVk = (GroupVk) query.uniqueResult();
        groupVk.setAccessToken(accessTokenToGroup);
        sessionFactory.getCurrentSession().merge(groupVk);
        longPollingService.getLongPolling(groupVk);
    }
}
