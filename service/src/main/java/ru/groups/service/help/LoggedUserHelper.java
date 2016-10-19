package ru.groups.service.help;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.UserVk;
import ru.groups.service.security.Session;

import java.util.List;

@Service
public class LoggedUserHelper {


    @Autowired Session session;
    @Autowired SessionFactory sessionFactory;


    @Transactional
    public UserVk getUserFromBD(){
        long userId = session.getLoggedUserId();
        UserVk userVk = sessionFactory.openSession().get(UserVk.class, userId);
        return userVk;
    }
}
