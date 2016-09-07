package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.service.UserService;

import java.security.Principal;


@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired UserService userService;

    @RequestMapping(value = "getName", method = RequestMethod.GET, produces = "application/json")
    public UserDTO getUserName(Principal principal){
        return userService.getUserName();
    }
}
