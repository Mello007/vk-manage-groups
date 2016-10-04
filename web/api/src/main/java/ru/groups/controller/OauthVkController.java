package ru.groups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.OauthService;
import ru.groups.service.VkInformationService;
import ru.groups.service.security.SecurityServiceContext;
import ru.groups.service.security.SecurityUser;

import javax.servlet.http.HttpServletResponse;



@RestController
@RequestMapping(value = "oauth")
public class OauthVkController {

    @Autowired VkInformationService vkInformationService;
    @Autowired
    SecurityServiceContext securityServiceContext;

    @RequestMapping(value = "vk", method = RequestMethod.GET)
    public String sendLink(){
        return "https://oauth.vk.com/authorize?client_id=5499487&display=page&scope=groups,messages,photos,docs&redirect_uri=http://localhost:8080/oauth/token&scope=offline&response_type=code&v=5.52";
    }


    @RequestMapping(value = "token", method = RequestMethod.GET)
    public void getToken(@RequestParam String code) throws Exception {
        UserVk userVk =  vkInformationService.loadUserByCode(code);
        SecurityUser securityUser = new SecurityUser(userVk);
        securityServiceContext.authUser(securityUser);
    }



}
