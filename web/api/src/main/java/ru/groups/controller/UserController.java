package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.service.UserService;
import ru.groups.service.security.SecurityServiceContext;


@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired SecurityServiceContext context;
    @Autowired UserService userService;

    @RequestMapping(value = "getName", method = RequestMethod.GET, produces = "application/json")
    public UserDTO getUserName(){
        Long loggedUserId = context.getLoggedUserId();
        if (loggedUserId == null){
            throw  new RuntimeException("User not logged");
        }
        return userService.getUserDTO(loggedUserId);
    }
}
