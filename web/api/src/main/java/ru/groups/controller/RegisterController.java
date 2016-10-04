package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.UserVk;
import ru.groups.service.UserService;

@RestController
@RequestMapping(value = "register")
public class RegisterController {

    @Autowired UserService userService;

    @RequestMapping(value = "new", method = RequestMethod.POST, consumes = "application/json")
    void registerNewUser(@RequestBody UserVk user){
        userService.saveUserVk(user);
    }
}
