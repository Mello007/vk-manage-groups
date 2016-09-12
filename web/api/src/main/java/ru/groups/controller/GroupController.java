package ru.groups.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.GroupVk;

import java.util.List;

@RestController
@RequestMapping(value = "groups")
public class GroupController {

    @RequestMapping(value = "getName", method = RequestMethod.GET, produces = "application/json")
    public List<GroupVk> getGroups(){
        return null;
    }
}
