package ru.groups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.service.UserService;
import ru.groups.service.VkInformationService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping(value = "oauth")
public class OauthVkController {

    @Autowired VkInformationService vkInformationService;
    @Autowired UserService userService;

    @RequestMapping(value = "vk", method = RequestMethod.GET)
    public void sendLink(HttpServletResponse response) throws IOException{
        response.sendRedirect("https://oauth.vk.com/authorize?client_id=5499487&display=page&scope=groups,messages,photos,docs&redirect_uri=http://89.223.28.35:8080/api-1.0-SNAPSHOT/oauth/token&scope=offline&response_type=code&v=5.52");
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public void getToken(@RequestParam String code, HttpServletResponse response) throws Exception {
        vkInformationService.loadUserByCode(code);
        response.sendRedirect("http://89.223.28.35:8080/api-1.0-SNAPSHOT/admin.html");
    }
}
