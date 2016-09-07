package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;

import java.util.List;

@Service
public class GroupService {

    @Autowired OauthService oauthService;

    public List<GroupVk> getAdminsGroups(UserVk user) throws Exception{
        String method = "groups.get";
        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}"
                .replace("{METHOD_NAME}", method)
                .replace("{userID}", user.getUserId());
        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        user.setUserId(oauthService.findValueInJson(actualObj, "user_id"));
        return null;
    }
}
