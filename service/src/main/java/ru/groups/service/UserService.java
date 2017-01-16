package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.security.Session;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;
    @Autowired Session securityServiceContext;

    @Transactional
    public void saveUserVk(UserVk user){
        sessionFactory.getCurrentSession().save(user);
    }

    @Transactional
        public UserVk getUserByName(String name){
        Query query = sessionFactory.getCurrentSession().createQuery("from UserVk where login = :login");
        query.setParameter("login", name);
        UserVk user = (UserVk)query.uniqueResult();
        if (user == null){
            throw new BadCredentialsException("User not found");
        }
        return user;
    }

}
