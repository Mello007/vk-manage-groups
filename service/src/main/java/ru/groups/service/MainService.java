package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.service.longpolling.LongPollingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class MainService {

//    @Autowired SessionFactory sessionFactory;
//    @Autowired LongPollingService longPollingService;
//
//    @Transactional
//    @Scheduled(fixedRate = 50000)
//    private void checkGroupsAtMessages(){
//        Query query = sessionFactory.openSession().createQuery("from GroupVk");
//        List<GroupVk> groupsInBd = query.list();
//        groupsInBd.forEach(groupVk -> {
//            if (groupVk.getGroupNeededToCheck()){
//                try {
//                    longPollingService.getLongPolling(groupVk);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
