package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;
import ru.groups.entity.MessageVk;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.messages.BadMessageService;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class MessageService {


    @Autowired VkInformationService oauthService;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired BadMessageService badMessageService;

    private void sendMessageToUser(String message, GroupVk groupVk, String userId) throws IOException {
        String reqUrl = "https://api.vk.com/method/messages.send?access_token={ACCESS_TOKEN}&user_id={userID}&message={MESSAGE}&notification=1"
                .replace("{ACCESS_TOKEN}", groupVk.getAccessToken())
                .replace("{userID}", userId)
                .replace("{MESSAGE}", message);
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
//        После успешного выполнения возвращает идентификатор отправленного сообщения.
        String messageId;
    }

    // This method WORK!!!!
    private  MessageVk findAnswerFromMessage(String id[], MessageVk messageVk) {
        for (int i = 0; i < id.length; i++){
            if (id[i].equals("[4")){
                System.out.println(id[i+3]);
                System.out.println(id[i+6]);
                messageVk.setMessageBody(id[i+6]);
                messageVk.setMessageUserId(id[i+3]);
            }
        }
        return messageVk;
    }

    public void findMessageAndUserIdInResponse(JsonNode actualObj, GroupVk groupVk) {
        JsonNode slaidsNode = (ArrayNode) actualObj.get("updates");
        Iterator<JsonNode> slaidsIterator = slaidsNode.elements();
        ArrayList<MessageVk> messagesInNode = new ArrayList<>();
         //здесь сделать условия чтобы если нашел айдишник но не нашел сообщение не отправлять
        while (slaidsIterator.hasNext()) {
            MessageVk messageVk = new MessageVk();
            JsonNode slaidNode = slaidsIterator.next();
            String id[] = slaidNode.toString().split(",");
            messageVk = findAnswerFromMessage(id, messageVk);
            messagesInNode.add(messageVk);
        }
        groupVk.setMessagesOfGroup(messagesInNode);
    }


    private void findAnswerToMessage(GroupVk groupVk) throws IOException{
        //Here I will put many methods (badMessages, AnswerAndAsk message)



    }


}
