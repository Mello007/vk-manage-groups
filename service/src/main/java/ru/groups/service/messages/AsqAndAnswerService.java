package ru.groups.service.messages;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.typeOfMessages.AnswerAndAsk;
import ru.groups.service.GroupService;

import java.util.List;

@Service
public class AsqAndAnswerService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;

    //This method is adding AnswerAndAsk to group
    @Transactional
    public void addAnswerAndAsk(AnswerAndAsk answerAndAsk, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getAnswerAndAsksMessages().add(answerAndAsk);
        sessionFactory.getCurrentSession().save(groupVk.getAnswerAndAsksMessages());
    }

    // I don't know, work this method or no, we neet to check method REMOVE, maybe it isn't work with type of object AnswerAndAsk
    @Transactional
    public void deleteAnswerAndAsk(AnswerAndAsk answerAndAsk, String groupId){
        sessionFactory.getCurrentSession().delete(answerAndAsk);
    }

    // Bad bad bad fuck this method isn't work
    @Transactional
    public void changeAnswerAndAsk(AnswerAndAsk answerAndAsk, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getAnswerAndAsksMessages().add(answerAndAsk);
    }

    //This method find Answer from value of Ask.
    // need to write else
    public String findAnswer(String message, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        List<AnswerAndAsk> answerAndAsksList = groupVk.getAnswerAndAsksMessages();
        for (AnswerAndAsk answer : answerAndAsksList){
            if (answer.getMessageAsk().equals(message)){
                return answer.getMessageAnswer();
            }
        }
        return null;
    }
}
