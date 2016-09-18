package ru.groups.service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.UserVk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Data
public class OauthService {

    @Autowired SessionFactory sessionFactory;
    @Autowired UserService userService;

//
//    @Transactional
//    public void requestToUserForManageToken(String accessToken, String userId) throws Exception {
//        String userName, userLastName, userCity;
//        String method = "user.get";
//        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?access_token={ACCESS_TOKEN}&v={API_VERSION}"
//                .replace("{METHOD_NAME}", method)
//                .replace("{ACCESS_TOKEN}", accessToken)
//                .replace("{API_VERSION}", API_VERSION);
//        System.out.println(reqUrl);
//        StringBuffer response = apiRequestToUser(reqUrl);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode actualObj = mapper.readTree(response.toString());
//        if (actualObj.get("response") == null){
//            throw new Exception("This token isn't right!");
//        }
//        userName = String.valueOf(actualObj.get("response").findValue("first_name")).replace("\"", "");
//        userLastName = String.valueOf(actualObj.get("response").findValue("last_name")).replace("\"", "");
////        userCity = String.valueOf(actualObj.get("response").findValue("home_town")).replace("\"", ""); Получаем город
//        System.out.println(userName + userLastName);
//    } //Ненужный пока метод который предназнчен для работы с токеном

}
