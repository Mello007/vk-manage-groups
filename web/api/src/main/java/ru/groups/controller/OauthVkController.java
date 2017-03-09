package ru.groups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.service.UserService;
import ru.groups.service.GetUserInfoService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping(value = "oauth")
public class OauthVkController {

    @Autowired
    GetUserInfoService vkInformationService;
    @Autowired UserService userService;

    @RequestMapping(value = "vk", method = RequestMethod.GET)
    public void sendLink(HttpServletResponse response) throws IOException{
        response.sendRedirect("https://oauth.vk.com/authorize?client_id=5861084&display=page&scope=groups,messages,photos,docs&redirect_uri=http://localhost:8080/oauth/token&scope=offline&response_type=code");
    }

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public void getToken(@RequestParam String code, HttpServletResponse response) throws Exception {
        vkInformationService.loadUserByCode(code);
        response.sendRedirect("http://localhost:8080/admin.html");
    }
}
