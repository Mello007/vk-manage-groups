package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.groups.entity.AnswerAndAsk;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;
import ru.groups.service.OauthService;

import java.util.List;

@RestController
@RequestMapping(value = "groups")
public class GroupController {

    @Autowired GroupService groupService;

    @RequestMapping(value = "getgrouptoken", method = RequestMethod.GET)
    public String getAccessKeyOfGroups(@RequestParam String groupId) throws Exception{
        String reqUrl = "https://oauth.vk.com/authorize?client_id=5499487&redirect_uri=http://localhost:8080/oauth/getgroupsroute&group_ids={group_id}&scope=manage,messages,photos,docs&display=page&response_type=code&v={version}"
                .replace("{group_id}", groupId)
                .replace("{version}", "5.53");
        return reqUrl;
    }


    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "application/json")
    public List<GroupVk> getGroups() throws Exception{
           List<GroupVk> groupVks = groupService.getUserGroups();
        return  groupVks;
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
