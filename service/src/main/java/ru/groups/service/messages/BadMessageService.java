package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.groups.entity.GroupVk;
import ru.groups.entity.typeOfMessages.BadMessages;
import ru.groups.service.GroupService;

import java.util.LinkedList;
import java.util.List;

public class BadMessageService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

    public boolean isBadMessage(String message, String groupId){
        String loverCaseMessage = message.toLowerCase();
        List<BadMessages> badMessages = findBadMessages(groupId);
        boolean isBadMessage = badMessages.stream().
                anyMatch(badMessage -> loverCaseMessage.
                contains(badMessage.getBadMessage().
                        toLowerCase()));
        return isBadMessage;
    }

    // This method adding default mature words to groups bot.
    // He's invoke from controller, when user entered check-button.
    // I don't know how better to add new bad messages
    public List<BadMessages> addDefaultBadMessages(String groupId){
        List<BadMessages> matureBadMessages = new LinkedList<>();

        List<BadMessages> badMessages = findBadMessages(groupId);
        badMessages.addAll(matureBadMessages);
        return null;
    }

    // This method adding new Bad Word or Message, which added user in Form.

    public void addNewBadWord(String groupId, String word){
        BadMessages badMessage = new BadMessages();
        badMessage.setBadMessage(word);

        List<BadMessages> badMessages = findBadMessages(groupId);

        badMessages.add(badMessage);
    }

    // This method delete bad word from list of bad words this group

    public void deleteBadWord(String groupId, String word){
        List<BadMessages> badMessages = findBadMessages(groupId);
        for (BadMessages badMessageIterator : badMessages){
            if (badMessageIterator.getBadMessage().equals(word)){
                badMessages.remove(badMessageIterator);
            }
        }
    }

    private List<BadMessages> findBadMessages(String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        List<BadMessages> badMessages = groupVk.getBadMessages();
        return badMessages;
    }





}
