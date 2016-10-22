package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;

import java.io.IOException;

@Service
public class MessageService {


    @Autowired VkInformationService oauthService;

    public void checkWord(String word, String groupId){

    }

    public void sendMessageToUser(String message, GroupVk groupVk, String userId) throws IOException {
        String reqUrl = "https://api.vk.com/method/messages.send?access_token={ACCESS_TOKEN}&user_id={userID}&message={MESSAGE}&notification=1"
                .replace("{ACCESS_TOKEN}", groupVk.getAccessToken())
                .replace("{userID}", userId)
                .replace("{MESSAGE}", message);
        StringBuffer response = oauthService.apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());

//        После успешного выполнения возвращает идентификатор отправленного сообщения.
        String messageId;



    }
}
