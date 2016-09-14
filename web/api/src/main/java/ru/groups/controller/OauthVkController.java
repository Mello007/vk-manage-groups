package ru.groups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.service.OauthService;

import javax.servlet.http.HttpServletResponse;



@RestController
@RequestMapping(value = "oauth")
public class OauthVkController {

    @Autowired OauthService oauthService;

    @RequestMapping(value = "vk", method = RequestMethod.GET)
    public String sendLink(){
        return "https://oauth.vk.com/authorize?client_id=5499487&display=page&scope=groups,messages,photos,docs&redirect_uri=http://localhost:8080/oauth/token&scope=offline&response_type=code&v=5.52";
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public Authentication getToken(@RequestParam String code) throws Exception{
        return oauthService.searchUserId(code);
    }

}
