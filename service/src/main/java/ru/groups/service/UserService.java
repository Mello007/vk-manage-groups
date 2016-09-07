package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.security.CustomUser;
import ru.groups.service.security.MyUserDetailService;
import ru.groups.service.security.Session;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;
    @Autowired MyUserDetailService myUserDetailService;
    @Autowired Session session;

    @Transactional
    public void searchUserName(UserVk user){
        sessionFactory.getCurrentSession().save(user);
        myUserDetailService.loadUserByUsername(user);
    }

    @Transactional
    public UserDTO getUserName(){
        UserDTO userDTO = new UserDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        long userId = customUser.getId();
        Query query = sessionFactory.getCurrentSession().createQuery("from UserVk where id = :id"); //Делаем запрос в БД с помощью HQL
        query.setParameter("id", userId); //Указываем что в запросе login будет принимаемый login
        UserVk user = (UserVk)query.uniqueResult();
        userDTO.setUserLastName(user.getUserLastName());
        userDTO.setUserName(user.getUserName());
        return userDTO;
    }
}
