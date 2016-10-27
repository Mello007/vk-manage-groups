package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@Service
public class BadMessageService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;


    @Transactional
    public void addNewBadWord(String word, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getBadMessage().add(word);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    public String isBadMessage(String message, String groupId){
        String loverCaseMessage = message.toLowerCase();
        GroupVk groupVk = groupService.searchGroup(groupId);
        boolean isBadMessage = groupVk.getBadMessage().stream().
                anyMatch(badMessage -> loverCaseMessage.
                contains(badMessage.
                        toLowerCase()));
        if (isBadMessage){
            Random random = new Random();
            //Here method return random answer from BD to BAD question
            return groupVk.getStopWords().get(random.nextInt(groupVk.getStopWords().size()));
        }
        return null;
    }




    //Add answers else
    @Transactional
    public void addStopDefaultWords(String groupId){
        List<String> stopWords = new LinkedList<>();
        stopWords.add("Извините, я не могу ответить на данное сообщение");

        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.setStopWords(stopWords);
        sessionFactory.getCurrentSession().merge(groupVk);
    }


    // This method adding default mature words to groups bot.
    // He's invoke from controller, when user entered check-button.
    // I don't know how better to add new bad messages
    @Transactional
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
        groupVk.setBadMessage(matureBadMessages);
       // groupVk.setBadMessages(matureBadMessages);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    // This method adding new Bad Word or Message, which added user in Form.



    // This method delete bad word from list of bad words this group
    @Transactional
    public void deleteBadWord(String groupId, String word){
        GroupVk groupVk = groupService.searchGroup(groupId);
        for (String badMessageIterator : groupVk.getBadMessage()){
            if (badMessageIterator.equals(word)){
                groupVk.getBadMessage().remove(badMessageIterator);
            }
        }
        sessionFactory.getCurrentSession().merge(groupVk);
    }
}
