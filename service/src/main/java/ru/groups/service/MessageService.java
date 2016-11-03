package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;
import ru.groups.entity.MessageVk;
import ru.groups.service.help.JsonParsingHelper;
import ru.groups.service.help.LoggedUserHelper;
import ru.groups.service.longpolling.LongPollingService;
import ru.groups.service.messages.BadMessageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MessageService {

    @Autowired VkInformationService oauthService;
    @Autowired LoggedUserHelper loggedUserHelper;
    @Autowired BadMessageService badMessageService;
    @Autowired SessionFactory sessionFactory;
    @Autowired LongPollingService longPollingService;


    private void sendMessageToUser(MessageVk messageVk, String accessToken) throws IOException {
        String reqUrl = "https://api.vk.com/method/messages.send?access_token={ACCESS_TOKEN}&user_id={userID}&message={MESSAGE}&notification=1"
                .replace("{ACCESS_TOKEN}", accessToken)
                .replace("{userID}", messageVk.getMessageUserId())
                .replace("{MESSAGE}", messageVk.getMessageReply().replace(" ", "%20"));
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
//        После успешного выполнения возвращает идентификатор отправленного сообщения.
        String messageId;
    }

    // This method is WORK!!!!
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



    public void findMessageAndUserIdInResponse(JsonNode actualObj, GroupVk groupVk) throws IOException {
        JsonNode slaidsNode = (ArrayNode) actualObj.get("updates");
        Iterator<JsonNode> slaidsIterator = slaidsNode.elements();
        ArrayList<MessageVk> messagesInNode = new ArrayList<>();
        while (slaidsIterator.hasNext()) {
            MessageVk messageVk = new MessageVk();
            JsonNode slaidNode = slaidsIterator.next();
            String id[] = slaidNode.toString().split(",");
            messageVk = findAnswerFromMessage(id, messageVk);
            if (messageVk.getMessageBody() != null && messageVk.getMessageUserId() != null) {
                messagesInNode.add(messageVk);
                sessionFactory.getCurrentSession().merge(messageVk);
            }
        }
        groupVk.setMessagesOfGroup(messagesInNode);
        sessionFactory.getCurrentSession().merge(groupVk);
        findAnswerToMessage(groupVk);
    }

    private void findAnswerToMessage(GroupVk groupVk) throws IOException{
        //Here I will put many methods (badMessages, AnswerAndAsk message)
        List<MessageVk> messageVks = groupVk.getMessagesOfGroup();
        for (MessageVk messageVk : messageVks) {

                if (!messageVk.isMessageAnswered()) {
                    String badMessage = badMessageService.isBadMessage(messageVk.getMessageBody(), groupVk);
                    if (badMessage == null) {
                        //here I need to write else find answer and add answer to messageReply
                        messageVk.setMessageReply("Мой Папка Савич Артем".replace(" ", "%20"));
                        // sendMessageToUser("Привет!!!", groupVk, messageVk.getMessageUserId()); Cheking
                        sendMessageToUser(messageVk, groupVk.getAccessToken());
                    } else {
                        messageVk.setMessageReply(badMessage);
                        sendMessageToUser(messageVk, groupVk.getAccessToken());
                    }
                }
            }
        longPollingService.getLongPolling(groupVk);
    }
}
