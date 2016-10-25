package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;

import java.util.LinkedList;
import java.util.List;

public class BadMessageService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

//    public boolean isBadMessage(String message, String groupId){
//        String loverCaseMessage = message.toLowerCase();
//      //  List<String> badMessages = findBadMessages(groupId);
//        boolean isBadMessage = badMessages.stream().
//                anyMatch(badMessage -> loverCaseMessage.
//                contains(badMessage.
//                        toLowerCase()));
//        return isBadMessage;
//    }

    // This method adding default mature words to groups bot.
    // He's invoke from controller, when user entered check-button.
    // I don't know how better to add new bad messages
    public void addDefaultBadMessages(String groupId){
        List<String> matureBadMessages = new LinkedList<>();
        matureBadMessages.add("хуй");
        matureBadMessages.add("пзд");
        matureBadMessages.add("пизд");
        matureBadMessages.add("ебат");
        matureBadMessages.add("еба");
        matureBadMessages.add("пидор");
        matureBadMessages.add("выеб");
        matureBadMessages.add("дерьмо");
        matureBadMessages.add("sheet");
        matureBadMessages.add("доеб");
        matureBadMessages.add("поеб");
        matureBadMessages.add("хуе");
        matureBadMessages.add("хуя");
        matureBadMessages.add("хую");
        matureBadMessages.add("гондон");
        matureBadMessages.add("гандон");
        matureBadMessages.add("пидра");
        matureBadMessages.add("пидор");
        matureBadMessages.add("ёба");
        matureBadMessages.add("хуё");




        GroupVk groupVk = groupService.searchGroup(groupId);
       // groupVk.setBadMessages(matureBadMessages);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    // This method adding new Bad Word or Message, which added user in Form.

    public void addNewBadWord(String groupId, String word){
       // List<String> badMessages = findBadMessages(groupId);
        GroupVk groupVk = groupService.searchGroup(groupId);
        sessionFactory.getCurrentSession().merge(groupVk);

    }

    // This method delete bad word from list of bad words this group

//    public void deleteBadWord(String groupId, String word){
//        List<String> badMessages = findBadMessages(groupId);
//        for (String badMessageIterator : badMessages){
//            if (badMessageIterator.equals(word)){
//                badMessages.remove(badMessageIterator);
//            }
//        }
//    }

//    private List<String> findBadMessages(String groupId){
//        GroupVk groupVk = groupService.searchGroup(groupId);
//      //  List<String> badMessages = groupVk.getBadMessages();
//      //  return badMessages;
//    }





}
