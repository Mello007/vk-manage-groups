package ru.groups.service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.queries.account.AccountGetInfoQuery;
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
public class GetUserInfoService {

    @Autowired UserService userService;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired GroupService groupService;

    private final Integer appId = 5861084;
    private final String clientSecret = "5iYyqXDE3EfmDiBcSpI9";
    private final String redirectUri = "http://localhost:8080/oauth/token";


    private JsonNode loadFullNameById(String userId) throws IOException{
        String method = "users.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", userId);
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
        if (actualObj.get("response") == null){
            throw new RuntimeException("Ошибка авторизации!!!");
        }
        return actualObj;
    }

//    private UserVk addToUserNameAndLastName(UserVk userVk) throws Exception {
//        JsonNode userWithFullName = loadFullNameById(userVk.getUserId());
//        userVk.setUserName(JsonParsingHelper.findValueInJson(userWithFullName, "first_name"));
//        userVk.setUserLastName(JsonParsingHelper.findValueInJson(userWithFullName, "last_name"));
//        return userVk;
//    }

    @Transactional
    public UserVk loadUserByCode(String code) throws Exception {
        UserVk userVk = loggedUserHelper.getUserFromBD();
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        UserAuthResponse authResponse = vk.oauth()
                .userAuthorizationCodeFlow(appId, clientSecret, redirectUri, code)
                .execute();
        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        userVk.setUserId(actor.getId().toString());
        userVk.setUserAccessToken(actor.getAccessToken());
        userVk.setUserName("sfgg");
        userVk.setUserLastName("dfgfgh");
//        userVk = addToUserNameAndLastName(userVk);
        groupService.findUserGroupsInAPI(userVk, vk, actor);
        return userVk;
    }
}
