package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.UserVk;
import ru.groups.service.security.CustomAuthenticationManager;
import ru.groups.service.security.SecurityServiceContext;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;
    @Autowired SecurityServiceContext securityServiceContext;

    @Transactional
    public void saveUserVk(UserVk user){
        sessionFactory.getCurrentSession().save(user);
    }

    @Transactional
    public UserDTO getUserDTO(Long userId){
        UserVk userVk = sessionFactory.getCurrentSession().get(UserVk.class, userId);
        return new UserDTO(userVk);
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
