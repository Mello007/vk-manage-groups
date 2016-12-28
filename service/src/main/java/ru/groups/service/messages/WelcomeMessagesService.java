package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class WelcomeMessagesService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

    @Transactional
    public void addNewMessageWord(String word, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getAnswerAtWelcomeMessage().add(word);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    // This method adding default mature words to groups bot.
    // He's invoke from controller, when user entered check-button.
    // I don't know how better to add new bad messages
    @Transactional
    public void addDefaultAnswerAtWelcomeMessages(GroupVk groupVk){
        List<String> answerAtWelcomeMessages = new ArrayList<>();
        answerAtWelcomeMessages.add("Приветствую!");
        answerAtWelcomeMessages.add("Приветик!");
        answerAtWelcomeMessages.add("Привет  :)!");
        answerAtWelcomeMessages.add("Добрый день!");
        answerAtWelcomeMessages.add("Здравствуйте.");
        answerAtWelcomeMessages.add("Привет, слушаю Вас.");
        answerAtWelcomeMessages.add("Приветствую, чем мог бы помочь Вам?");
        answerAtWelcomeMessages.add("Приветики!");
        answerAtWelcomeMessages.add("Привет :)");
        groupVk.setBadMessage(answerAtWelcomeMessages);
        // groupVk.setBadMessages(matureBadMessages);
        sessionFactory.getCurrentSession().merge(groupVk);
        addDefaultWelcomeMessages(groupVk);
    }





    @Transactional
    private void addDefaultWelcomeMessages(GroupVk groupVk){
        List<String> welcomeMessages = new ArrayList<>();
        welcomeMessages.add("прив");
        welcomeMessages.add("здаров");
        welcomeMessages.add("здравст");
        groupVk.setWelcomeMessage(welcomeMessages);
        sessionFactory.getCurrentSession().merge(groupVk);
        addDefaultAnswer(groupVk);
    }



    @Transactional
    private void addDefaultAnswer(GroupVk groupVk){
        List<String> defaultAnswer = new ArrayList<>();
        defaultAnswer.add("Я не могу понять что Вы хотите");
        defaultAnswer.add("Я не понял, повторите еще раз :) И уточните Ваш запрос пожалуйста :)");
        defaultAnswer.add("Не могу понять, попробуйте другую команду");
        groupVk.setDefaultAnswer(defaultAnswer);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    // This method adding new Bad Word or Message, which added user in Form.
    // This method delete bad word from list of bad words this group
    @Transactional
    public void deleteWelcomeWord(String groupId, String word){
        GroupVk groupVk = groupService.searchGroup(groupId);
        for (String welcomeMessageIterator : groupVk.getAnswerAtWelcomeMessage()){
            if (welcomeMessageIterator.equals(word)){
                groupVk.getBadMessage().remove(welcomeMessageIterator);
            }
        }
        sessionFactory.getCurrentSession().merge(groupVk);
    }
}
