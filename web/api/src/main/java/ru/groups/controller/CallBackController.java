package ru.groups.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "callback")
public class CallBackController {


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping (value = "check", method = RequestMethod.GET)
    public String checkApp(){
        return "a975253d";
    }
}
