package ru.groups.service.messages;


import lombok.Data;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.AnswerAndAsk;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.GroupService;
import java.util.List;


@Service
public class AsqAndAnswerService {

    @Autowired SessionFactory sessionFactory;
    @Autowired GroupService groupService;
//
//    @Transactional
//    public void AsqAndAnswerService(AnswerAndAsk answerAndAsk, String groupId){
//        GroupVk groupVk = groupService.searchGroup(groupId);
//        List answerAndAskList = groupVk.getAnswerAndAsksMessages();
//        answerAndAskList.add(answerAndAsk);
//    }
}
