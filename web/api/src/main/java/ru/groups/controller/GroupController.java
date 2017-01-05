package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;
import ru.groups.service.longpolling.LongPollingService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "groups")
public class GroupController {

    @Autowired GroupService groupService;
    @Autowired LongPollingService longPollingService;

    // Here I need get request from front, and this method will send link, which are open in browser
    // After authorization user reroute at address ouath/getaccess, where method in service get access token from api
    @RequestMapping(value = "getgrouptoken", method = RequestMethod.GET)
    public String getAccessKeyOfGroups(@RequestParam String groupId) throws Exception{
        String reqUrl = "https://oauth.vk.com/authorize?client_id=5499487&redirect_uri=89.223.28.35:8080/api-1.0-SNAPSHOT/getaccess&group_id={group_id}&scope=manage,messages,photos,docs&display=page&response_type=code&v={version}"
                .replace("{group_id}", groupId)
                .replace("{version}", "5.59");
        return reqUrl;
    }


    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "application/json")
    public List<GroupVk> getGroups() throws Exception{
           List<GroupVk> groupVks = groupService.getGroupsFromBD();
        return  groupVks;
    }


    //This method adding accessToken to pointed group
    @RequestMapping(value = "getaccess", method = RequestMethod.POST)
    public void getToken(@RequestParam String accessToken, @RequestParam String groupId,
                         HttpServletResponse response) throws Exception {
        response.sendRedirect("89.223.28.35:8080/api-1.0-SNAPSHOT/admin.html");
    }

    @RequestMapping(value = "setpolling", method = RequestMethod.POST)
    public void setTokenToPollingServer(@RequestParam("token") String accessToken, @RequestParam("groupId") String groupId) throws Exception {
        groupService.setAccessTokenToGroup(accessToken, groupId);
       // response.sendRedirect("http://localhost:8080/resources/admin.html");
    }

}
