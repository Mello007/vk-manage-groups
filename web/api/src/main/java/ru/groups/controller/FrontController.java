package ru.groups.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller


public class FrontController {
    @RequestMapping (value = "/", method = RequestMethod.GET)
    public String getMain(){
        return "resources/index.html";
    }


    @RequestMapping(value = "/vk", method = RequestMethod.GET)
    public String sendLink(){
        return "https://oauth.vk.com/authorize?client_id=5499487&display=page&scope=groups,messages,photos,docs&redirect_uri=http://localhost:8080/resources/admin.html&scope=offline&response_type=code&v=5.52";
    }
}
