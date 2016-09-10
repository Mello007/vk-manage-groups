package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.security.CustomAuthenticationManager;
import ru.groups.service.security.MyUserDetailService;
import ru.groups.service.security.SecurityServiceContext;
import java.io.IOException;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;
    @Autowired MyUserDetailService myUserDetailService;
    @Autowired SecurityServiceContext context;
    @Autowired CustomAuthenticationManager customAuthenticationManager;

    @Transactional
    public Authentication searchUserName(UserVk user){
        sessionFactory.getCurrentSession().save(user);
        return customAuthenticationManager.loadUserByUsername(user);
    }

    @Transactional
    public UserDTO getUserDTO(){
        UserDTO userDTO = new UserDTO();
        long userId = context.getLoggedUserId();
        Query query = sessionFactory.getCurrentSession().createQuery("from UserVk where id = :id");
        query.setParameter("id", userId);//Делаем запрос в БД с помощью HQL
        UserVk user = (UserVk)query.uniqueResult();
        userDTO.setUserLastName(user.getUserLastName());
        userDTO.setUserName(user.getUserName());
        return userDTO;
    }

    @Transactional
        public UserVk getUserByName(String name){
        Query query = sessionFactory.getCurrentSession().createQuery("from UserVk where userName = :userName");
        query.setParameter("userName", name);
        UserVk user = (UserVk)query.uniqueResult();
        if (user == null){
            throw new BadCredentialsException("User not found");
        }
        return user;
    }
}
