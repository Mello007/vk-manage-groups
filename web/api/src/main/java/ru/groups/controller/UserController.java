package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.GroupService;
import ru.groups.service.UserService;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.security.Session;

import java.io.IOException;


@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired Session session;
    @Autowired UserService userService;
    @Autowired LoggedUserHelper loggedUserHelper;

    @RequestMapping(value = "getName", method = RequestMethod.GET, produces = "application/json")
    public UserDTO getUserName() throws IOException {
        return new UserDTO(loggedUserHelper.getUserFromBD());
    }

    @RequestMapping(value = "getUser", method = RequestMethod.GET, produces = "application/json")
    public UserVk getUser(){
        return loggedUserHelper.getUserFromBD();
    }

    @RequestMapping(value = "new", method = RequestMethod.POST, consumes = "application/json")
    void registerNewUser(@RequestBody UserVk user){
        userService.saveUserVk(user);
    }
}
