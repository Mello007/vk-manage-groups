package ru.groups.service.messages;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.typeOfMessages.BadMessages;
import ru.groups.service.GroupService;

import java.util.List;

public class BadMessageService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

    public boolean isBadMessage(String message, String groupId){
        String loverCaseMessage = message.toLowerCase();
        GroupVk groupVk = groupService.searchGroup(groupId);
        List<BadMessages> badMessages = groupVk.getBadMessages();
        boolean isBadMessage = badMessages.stream().
                anyMatch(badMessage -> loverCaseMessage.
                contains(badMessage.getBadMessage().
                        toLowerCase()));
        return isBadMessage;
    }
}
