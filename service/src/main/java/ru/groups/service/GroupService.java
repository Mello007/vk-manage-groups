package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.longpolling.LongPollingService;
import ru.groups.service.messages.BadMessageService;
import ru.groups.service.messages.WelcomeMessagesService;
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
    @Autowired BadMessageService badMessageService;
    @Autowired WelcomeMessagesService welcomeMessagesService;
    @Autowired ProductService productService;

    private static final String versionOfVkApi = "5.59";


    @Transactional
    public List<GroupVk> getGroupsFromBD(){
        UserVk user = loggedUserHelper.getUserFromBD();
        return user.getUserGroups();
    }

    @Transactional
    public List<GroupVk> findUserGroupsInAPI(UserVk userVk) throws Exception{
        List<GroupVk> groupVks = new LinkedList<>();
        String method = "groups.get";
        String reqUrl = ("https://api.vk.com/method/{METHOD_NAME}?user_id={userID}&" +
                "extended=1&filter=admin&access_token=" +
                "{access_token}&v={version}")
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", userVk.getUserId())
                .replace("{access_token}", userVk.getUserAccessToken())
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
            sessionFactory.getCurrentSession().saveOrUpdate(group);
            groupVks.add(group);
        }
        userVk.setUserGroups(groupVks); //добавляем юзеру группы его
        sessionFactory.getCurrentSession().merge(userVk);
        return groupVks;
    }


    @Transactional
    public GroupVk searchGroup(String groupId){
        Query queryToBd = sessionFactory.getCurrentSession().createQuery("from GroupVk where groupid = :groupid").setParameter("groupid", groupId);
        GroupVk groupVk = (GroupVk) queryToBd.uniqueResult();
        return groupVk;
    }

    @Transactional
    public void setAccessTokenToGroup(String accessTokenToGroup, String groupId) throws IOException {
        //It's solution, which get groupVk per ID from BD
        GroupVk groupVk = searchGroup(groupId);

        groupVk.setAccessToken(accessTokenToGroup);
        addToGroupDefaultAnswers(groupVk);

        sessionFactory.getCurrentSession().merge(groupVk);
        longPollingService.getLongPolling(groupVk);
    }

    @Transactional
    private void addToGroupDefaultAnswers(GroupVk groupVk){
        badMessageService.addDefaultBadMessages(groupVk);
        welcomeMessagesService.addDefaultAnswerAtWelcomeMessages(groupVk);
        productService.addAsksAboutProduct(groupVk);
    }
}
