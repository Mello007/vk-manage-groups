package ru.groups.service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.security.Session;

import java.io.IOException;
import java.util.List;

@Service
public class VkInformationService {

    @Autowired Session session;
    @Autowired SessionFactory sessionFactory;
    @Autowired UserService userService;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired GroupService groupService;


    private JsonNode loadJsonUserByCode(String code) throws IOException{
        String reqUrl = "https://oauth.vk.com/{METHOD_NAME}?client_id={USER_ID}&client_secret={CLIENT_SECRET}&redirect_uri={REDIRECT_URI}&code={CODE}"
                .replace("{METHOD_NAME}", "access_token")
                .replace("{USER_ID}", "5499487")
                .replace("{CLIENT_SECRET}", "bMTTeUDFad7H95I8LiIt")
                .replace("{REDIRECT_URI}", "http://89.223.28.35:8080/api-1.0-SNAPSHOT/oauth/token")
                .replace("{CODE}", code);
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
        return actualObj;
    }

    private UserVk getAccessTokeByCode(String code) throws Exception {
        JsonNode userWithAccessToken = this.loadJsonUserByCode(code);
        UserVk user = new UserVk();
        user.setUserAccessToken(JsonParsingHelper.findValueInJson(userWithAccessToken ,"access_token"));
        user.setUserId(JsonParsingHelper.findValueInJson(userWithAccessToken, "user_id"));
        return user;
    }

    private JsonNode loadFullNameById(String userId) throws IOException{
        String method = "users.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", userId);
        System.out.println(reqUrl);
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
        if (actualObj.get("response") == null){
            throw new RuntimeException("Ошибка авторизации!!!");
        }
        return actualObj;
    }

    private UserVk loadUserWithFullName(String userId) throws Exception {
        JsonNode userWithFullName = loadFullNameById(userId);
        UserVk userVk = new UserVk();
        userVk.setUserName(JsonParsingHelper.findValueInJson(userWithFullName, "first_name"));
        userVk.setUserLastName(JsonParsingHelper.findValueInJson(userWithFullName, "last_name"));
        return userVk;
    }

    // Main method, which create new User with Full Data in DB
    @Transactional
    public UserVk loadUserByCode(String code) throws Exception{

        //here I create new user, which has AccessToken and Id
        UserVk userWithAccessTokenAndId = this.getAccessTokeByCode(code);

        UserVk userVk = loggedUserHelper.getUserFromBD();
        //here I create new user, which has Name and LastName
        UserVk userWithFullName = this.loadUserWithFullName(userWithAccessTokenAndId.getUserId());

        //Here I invoke logged User and adding him parameters from other users

        userVk.setUserAccessToken(userWithAccessTokenAndId.getUserAccessToken());
        userVk.setUserName(userWithFullName.getUserName());
        userVk.setUserLastName(userWithFullName.getUserLastName());
        userVk.setUserId(userWithAccessTokenAndId.getUserId());

        //update fullUser
        sessionFactory.getCurrentSession().merge(userVk);
        groupService.findUserGroupsInAPI();
        return userVk;
    }
}
