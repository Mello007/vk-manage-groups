package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.groups.entity.typeOfMessages.AnswerAndAsk;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "groups")
public class GroupController {

    @Autowired GroupService groupService;


    // Here I need get request from front, and this method will send link, which are open in browser
    // After authorization user reroute at address ouath/getaccess, where method in service get access token from api
    @RequestMapping(value = "getgrouptoken", method = RequestMethod.GET)
    public String getAccessKeyOfGroups(@RequestParam String groupId) throws Exception{
        String reqUrl = "https://oauth.vk.com/authorize?client_id=5499487&redirect_uri=http://localhost:8080/groups/getaccess&group_id={group_id}&scope=manage,messages,photos,docs&display=page&response_type=code&v={version}"
                .replace("{group_id}", groupId)
                .replace("{version}", "5.59");
        return reqUrl;
    }


    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "application/json")
    public List<GroupVk> getGroups() throws Exception{
           List<GroupVk> groupVks = groupService.getUserGroups();
        return  groupVks;
    }


    @RequestMapping(value = "getaccess", method = RequestMethod.GET)
    public void getToken(@RequestParam String code, HttpServletResponse response) throws Exception {
        if (code != null) {
            groupService.getAccessKeyOfGroups(code);
        } else response.sendRedirect("http://localhost:8080/resources/admin.html");
//        SecurityUser securityUser = new SecurityUser(userVk);
//        securityServiceContext.authUser(securityUser);
    }


    @RequestMapping(value = "redirect", method = RequestMethod.GET)
    public void sendRedirect(HttpServletResponse response) throws Exception {
        response.sendRedirect("http://localhost:8080/resources/admin.html");
//        SecurityUser securityUser = new SecurityUser(userVk);
//        securityServiceContext.authUser(securityUser);
    }



//
//    @RequestMapping(value = "{groupId}/rights", method = RequestMethod.GET)
//    public void getRouteForGroup(@RequestParam String code, @PathVariable String groupId) throws Exception{
//        groupService.getAccessKeyOfGroups(code, groupId);
//    }

    @RequestMapping(value = "addaskandansmessages", method = RequestMethod.POST, consumes = "application/json")
    public void addAnsAndAskMessages(@RequestBody AnswerAndAsk answerAndAsk){

    }
}
