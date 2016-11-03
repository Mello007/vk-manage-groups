package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.GroupService;

import java.util.LinkedList;
import java.util.List;

@Service
public class WelcomeMessagesService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

    @Transactional
    public void addNewMessageWord(String word, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getWelcomeMessage().add(word);
        sessionFactory.getCurrentSession().merge(groupVk);
    }


    // This method adding default mature words to groups bot.
    // He's invoke from controller, when user entered check-button.
    // I don't know how better to add new bad messages
    @Transactional
    public void addDefaultBadMessages(GroupVk groupVk){
        List<String> welcomeMessages = new LinkedList<>();
        welcomeMessages.add("Приветствую!");
        welcomeMessages.add("Приветик!");
        welcomeMessages.add("Привет  :)!");
        welcomeMessages.add("Добрый день!");
        welcomeMessages.add("Здравствуйте.");
        welcomeMessages.add("Привет, слушаю Вас.");
        welcomeMessages.add("Приветствую, чем мог бы помочь Вам?");
        welcomeMessages.add("Приветики!");
        welcomeMessages.add("Привет :)");
        groupVk.setBadMessage(welcomeMessages);
        // groupVk.setBadMessages(matureBadMessages);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    // This method adding new Bad Word or Message, which added user in Form.
    // This method delete bad word from list of bad words this group
    @Transactional
    public void deleteWelcomeWord(String groupId, String word){
        GroupVk groupVk = groupService.searchGroup(groupId);
        for (String welcomeMessageIterator : groupVk.getWelcomeMessage()){
            if (welcomeMessageIterator.equals(word)){
                groupVk.getBadMessage().remove(welcomeMessageIterator);
            }
        }
        sessionFactory.getCurrentSession().merge(groupVk);
    }
}
