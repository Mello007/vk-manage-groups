package ru.groups.service;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.User;

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

    private final String USER_AGENT = "Mozilla/5.0";
    private final String API_VERSION = "5.21";

    @Transactional
    private StringBuffer apiRequestToUser(String reqUrl) throws IOException { //Метод, который получает с GET запроса данные в response в формате json
        URL obj = new URL(reqUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
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

    @Transactional
    public void requestToUserForManageToken(String accessToken, String userId) throws Exception {
        String userName, userLastName, userCity;
        String method = "user.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?access_token={ACCESS_TOKEN}&v={API_VERSION}"
                .replace("{METHOD_NAME}", method)
                .replace("{ACCESS_TOKEN}", accessToken)
                .replace("{API_VERSION}", API_VERSION);
        System.out.println(reqUrl);
        StringBuffer response = apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        if (actualObj.get("response") == null){
            throw new Exception("This token isn't right!");
        }
        userName = String.valueOf(actualObj.get("response").findValue("first_name")).replace("\"", "");
        userLastName = String.valueOf(actualObj.get("response").findValue("last_name")).replace("\"", "");
//        userCity = String.valueOf(actualObj.get("response").findValue("home_town")).replace("\"", ""); Получаем город
        System.out.println(userName + userLastName);
    } //Ненужный пока метод который предназнчен для работы с токеном

    @Transactional
    public boolean requestToUserForFindName(User user) throws Exception {
        String userName, userLastName;
        String method = "users.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", user.getUserId());
        System.out.println(reqUrl);
        StringBuffer response = apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        if (actualObj.get("response") == null){
            throw new Exception("Ошибка авторизации!!!");
        }
//        userName = String.valueOf(actualObj.get("response").findValue("first_name")).replace("\"", "");
//        userLastName = String.valueOf(actualObj.get("response").findValue("last_name")).replace("\"", ""); удалить после проверки findValueInJson
        user.setUserName(findValueInJson(actualObj, "first_name"));
        user.setUserLastName(findValueInJson(actualObj, "last_name"));
        return userService.searchUserName(user);
    }

    @Transactional
    public boolean searchUserId(String code) throws Exception {
        User user = new User();
        String reqUrl = "https://oauth.vk.com/{METHOD_NAME}?client_id={USER_ID}&client_secret={CLIENT_SECRET}&redirect_uri={REDIRECT_URI}&code={CODE}"
                .replace("{METHOD_NAME}", "access_token")
                .replace("{USER_ID}", "5499487")
                .replace("{CLIENT_SECRET}", "bMTTeUDFad7H95I8LiIt")
                .replace("{REDIRECT_URI}", "http://localhost:8080/oauth/token")
                .replace("{CODE}", code);
        StringBuffer response = apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
//        String token = String.valueOf(actualObj.findValue("access_token")).replace("\"", "");
//        String userId = String.valueOf(actualObj.findValue("user_id")).replace("\"", ""); удалить после проверки метода findValueInJson
        user.setUserAccessToken(findValueInJson(actualObj ,"access_token"));
        user.setUserId(findValueInJson(actualObj, "user_id"));
        return requestToUserForFindName(user);
    }

    @Transactional
    public String findValueInJson(JsonNode actualObj, String value){
        return String.valueOf(actualObj.findValue(value)).replace("\"", "");
    }
}
