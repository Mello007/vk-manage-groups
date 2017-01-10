package ru.groups.service;


import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.node.ArrayNode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.GroupVk;
import ru.groups.entity.MessageVK;
import ru.groups.service.help.FindMessageHelper;
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

    private void sendMessageToUser(MessageVK messageVk, String accessToken) throws IOException {
        String reqUrl = "https://api.vk.com/method/messages.send?access_token={ACCESS_TOKEN}&user_id={userID}&message={MESSAGE}&notification=1"
                .replace("{ACCESS_TOKEN}", accessToken)
                .replace("{userID}", messageVk.getMessageUserId())
                .replace("{MESSAGE}", messageVk.getMessageReply().replace(" ", "%20"));
        JsonNode actualObj = JsonParsingHelper.GetValueAndChangeJsonInString(reqUrl);
        messageVk.isMessageAnswered();
//        После успешного выполнения возвращает идентификатор отправленного сообщения.
    }

    // This method are WORK!!!!
    private MessageVK findAnswerFromMessage(String partOfAnswer[], MessageVK messageVk) {
        for (int i = 0; i < partOfAnswer.length; i++){
            if (partOfAnswer[i].equals("[4")){
                messageVk.setMessageBody(partOfAnswer[i+6]);
                messageVk.setMessageUserId(partOfAnswer[i+3]);
            }
        }
        return messageVk;
    }

    public void findMessageAndUserIdInResponse(JsonNode actualObj, GroupVk groupVk) throws IOException {
        JsonNode slaidsNode = (ArrayNode) actualObj.get("updates");
        Iterator<JsonNode> slaidsIterator = slaidsNode.elements();
        ArrayList<MessageVK> messagesInNode = new ArrayList<>();
        while (slaidsIterator.hasNext()) {
            MessageVK messageVk = new MessageVK();
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

    private void findAnswerToMessage(GroupVk groupVk) throws IOException {
        //Here I will put many methods (badMessages, AnswerAndAsk message)
        List<MessageVK> messageVks = groupVk.getMessagesOfGroup();
        messageVks.forEach(messageVk -> {
            messageVk.setMessageReply(FindMessageHelper.findMessageInListsWithAnswers(messageVk.getMessageBody(), groupVk));
            //  FindMessageHelper.findMessageInListsWithAnswers(messageVk.getMessageBody(), groupVk);
            try {
                sendMessageToUser(messageVk, groupVk.getAccessToken());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
