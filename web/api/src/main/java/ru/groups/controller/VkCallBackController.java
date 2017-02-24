package ru.groups.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;

import java.io.IOException;

@RestController
@RequestMapping(value = "call")
public class VkCallBackController {

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public String get(){
        return "a975253d";
    }


    @RequestMapping(value = "set", method = RequestMethod.GET)
    public String gett(){
        return "asdasd";
    }
}
