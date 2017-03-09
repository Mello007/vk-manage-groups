package ru.groups.service;

//import com.vk.api.sdk.client.VkApiClient;
//import com.vk.api.sdk.client.actors.UserActor;
//import com.vk.api.sdk.exceptions.ApiException;
//import com.vk.api.sdk.exceptions.ClientException;
//import com.vk.api.sdk.objects.groups.GroupFull;
//import com.vk.api.sdk.queries.groups.GroupsGetFilter;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.longpolling.LongPollingService;
import ru.groups.service.messages.BadMessageService;
import ru.groups.service.messages.WelcomeMessagesService;
import ru.groups.service.shops.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired GetUserInfoService oauthService;
    @Autowired SessionFactory sessionFactory;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired LongPollingService longPollingService;
    @Autowired BadMessageService badMessageService;
    @Autowired WelcomeMessagesService welcomeMessagesService;
    @Autowired ProductService productService;

    @Transactional
    void findUserGroupsInAPI(UserVk userVk) throws Exception {
        List<GroupVk> groupsFromVkApi = getGroupsFromVkApi();
        List<GroupVk> changedOrNewGroups = findNewUserGroups(userVk.getUserGroups(), groupsFromVkApi);
        if (!changedOrNewGroups.isEmpty()) {
            userVk.setUserGroups(changedOrNewGroups); //добавляем юзеру группы его
            sessionFactory.getCurrentSession().saveOrUpdate(userVk);
        }
    }

    private List<GroupVk> getGroupsFromVkApi () {
//        List<GroupVk> groupVks = new ArrayList<>();
//        GroupsGetFilter filterForGroups = GroupsGetFilter.ADMIN;
//        List<String> groupIds = vk.groups()
//                .get(actor)
//                .filter(filterForGroups)
//                .execute()
//                .getItems()
//                .stream()
//                .map(Object::toString)
//                .collect(Collectors.toList());
//        List<GroupFull> groupFulls = vk.groups().getById(actor).groupIds(groupIds).execute();
//        groupFulls.forEach(groupFull -> {
//            GroupVk groupVk = new GroupVk();
//            groupVk.setGroupName(groupFull.getName());
//            groupVk.setGroupId(groupFull.getId());
//            groupVk.setPhoto50px(groupFull.getPhoto50());
//            groupVk.setPhoto100px(groupFull.getPhoto100());
//            groupVks.add(groupVk);
//        });
        return null;
    }

    private List<GroupVk> findNewUserGroups(List<GroupVk> groupsInBd, List<GroupVk> groupsFromApi) {
        List<GroupVk> newGroups = new ArrayList<>();
        groupsFromApi.forEach(groupVk -> {
            if (!groupsInBd.contains(groupVk)) {
                newGroups.add(groupVk);
            }
        });
        return newGroups;
    }

    @Transactional
    public GroupVk searchGroup(String groupId) {
        Query queryToBd = sessionFactory.getCurrentSession().createQuery("from GroupVk where groupid = :groupid").setParameter("groupid", groupId);
        return (GroupVk) queryToBd.uniqueResult();
    }

    @Transactional
    public void setAccessTokenToGroup(String accessTokenToGroup, String groupId) throws IOException {
        //It's solution, which get groupVk per ID from BD
        GroupVk groupVk = searchGroup(groupId);
        groupVk.setAccessToken(accessTokenToGroup);
        addToGroupDefaultAnswers(groupVk);
        groupVk.setGroupNeededToCheck(true);
    }

//    test method
//    @Transactional
//    public void set(){
//        GroupVk groupVk = searchGroup("139156858");
//        groupVk.setGroupNeededToCheck(true);
//        sessionFactory.getCurrentSession().saveOrUpdate(groupVk);
//    }

    @Transactional
    private void addToGroupDefaultAnswers(GroupVk groupVk) {
        badMessageService.addBadMessagesToGroup(groupVk);
        welcomeMessagesService.addWelcomeMessageToGroup(groupVk);
    }
}
