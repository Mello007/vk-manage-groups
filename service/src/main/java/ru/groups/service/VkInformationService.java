package ru.groups.service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.UserVk;
import ru.groups.service.help.JsonParsingHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class VkInformationService {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String API_VERSION = "5.21";


    @Autowired
    SessionFactory sessionFactory;


    public StringBuffer apiRequestToUser(String reqUrl) throws IOException { //Метод, который получает с GET запроса данные в response в формате json
        URL obj = new URL(reqUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("UserVk-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

    private JsonNode loadJsonUserByCode(String code) throws IOException{
        String reqUrl = "https://oauth.vk.com/{METHOD_NAME}?client_id={USER_ID}&client_secret={CLIENT_SECRET}&redirect_uri={REDIRECT_URI}&code={CODE}"
                .replace("{METHOD_NAME}", "access_token")
                .replace("{USER_ID}", "5499487")
                .replace("{CLIENT_SECRET}", "bMTTeUDFad7H95I8LiIt")
                .replace("{REDIRECT_URI}", "http://localhost:8080/oauth/token")
                .replace("{CODE}", code);
        StringBuffer response = apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        return actualObj;
    }


    public UserVk getAccessTokeByCode(String code) throws Exception {
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
        StringBuffer response = apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        if (actualObj.get("response") == null){
            throw new RuntimeException("Ошибка авторизации!!!");
        }
        return actualObj;
    }

    public UserVk loadUserWithFullName(String userId) throws Exception {
        JsonNode userWithFullName = loadFullNameById(userId);
        UserVk userVk = new UserVk();
        userVk.setUserName(JsonParsingHelper.findValueInJson(userWithFullName, "first_name"));
        userVk.setUserLastName(JsonParsingHelper.findValueInJson(userWithFullName, "last_name"));
        return userVk;
    }


    @Transactional
    public UserVk loadUserByCode(String code) throws Exception{
        UserVk userWithAccessTokenAndId = this.getAccessTokeByCode(code);
        UserVk userWithFullName = this.loadUserWithFullName(userWithAccessTokenAndId.getUserId());

        userWithAccessTokenAndId.setUserName(userWithFullName.getUserName());
        userWithAccessTokenAndId.setUserLastName(userWithFullName.getUserLastName());

        sessionFactory.getCurrentSession().save(userWithAccessTokenAndId);
        return userWithAccessTokenAndId;
    }
}
