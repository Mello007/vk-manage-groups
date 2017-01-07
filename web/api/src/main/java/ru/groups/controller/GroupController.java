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

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "application/json")
    public List<GroupVk> getGroups() throws Exception{
           List<GroupVk> groupVks = groupService.getGroupsFromBD();
        return  groupVks;
    }

    @RequestMapping(value = "setpolling", method = RequestMethod.POST)
    public void setTokenToPollingServer(@RequestParam("token") String accessToken, @RequestParam("groupId") String groupId) throws Exception {
        groupService.setAccessTokenToGroup(accessToken, groupId);
       // response.sendRedirect("http://localhost:8080/resources/admin.html");
    }
}
