package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.security.MyUserDetailService;
import ru.groups.service.security.Session;

import java.util.List;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;
    @Autowired MyUserDetailService myUserDetailService;


    @Transactional
    public void searchUserName(UserVk user){
        sessionFactory.getCurrentSession().save(user);
        myUserDetailService.loadUserByUsername(user);
    }

    @Transactional
    public UserDTO getUserName(){
        UserDTO userDTO = new UserDTO();
        Session session = new Session();
        long userId = session.getLoggedUserId();
        Query query = sessionFactory.getCurrentSession().createQuery("from UserVk");
        List<UserVk> user = query.list();
        for (UserVk userFromBd : user){
            if (userFromBd.getId().equals(userId)){
                userDTO.setUserName(userFromBd.getUserName());
                userDTO.setUserLastName(userFromBd.getUserLastName());
            }
        }
        return userDTO;
    }
}
