package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;

import java.util.LinkedList;
import java.util.List;

@Service
public class GroupService {
    @Autowired OauthService oauthService;
    private static final String versionOfVkApi = "5.21";
//    public List<GroupVk> getGroups(UserVk user) throws Exception{
//        List<GroupVk> groupVks = new LinkedList<>();
//        String method = "groups.get";
//        String reqUrl = "https://api.vk.com/method/{METHOD_NAME}?user_id={userID}&extended=1&filter=admin&access_token={access_token}&v={version}"
//                .replace("{METHOD_NAME}", method)
//                .replace("{userID}", user.getUserId())
//                .replace("{access_token}", user.getUserAccessToken())
//                .replace("{version}", versionOfVkApi);
//        StringBuffer response = oauthService.apiRequestToUser(reqUrl);
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode actualObj = mapper.readTree(response.toString());
//        user.setUserId(oauthService.findValueInJson(actualObj, "user_id"));
//        for(int i = 0; i<10; i++){
//            GroupVk group = new GroupVk();
//            group.setGroupId(oauthService.findValueInJson(actualObj, "id"));
//            group.setGroupName(oauthService.findValueInJson(actualObj, "name"));
//            group.setPhoto50px(oauthService.findValueInJson(actualObj, "photo_50"));
//            group.setPhoto100px(oauthService.findValueInJson(actualObj, "photo_100"));
//            groupVks.add(group);
//        }
//        return groupVks;
//    }
}
