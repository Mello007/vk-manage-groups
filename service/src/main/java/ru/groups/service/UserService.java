package ru.groups.service;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.DTO.UserDTO;
import ru.groups.entity.User;

@Service
public class UserService {

    @Autowired SessionFactory sessionFactory;

    public boolean searchUserName(User user){
        boolean findUser = true;
        Query query = sessionFactory.getCurrentSession().createQuery("from User where userId = :userId"); //Делаем запрос в БД с помощью HQL
        query.setParameter("userId", user.getUserId()); //Указываем что в запросе login будет принимаемый login
        if (query.uniqueResult() == null){
            System.out.println("Такого пользователя не существует!");
            findUser = false;
            sessionFactory.getCurrentSession().save(user);
        }
        return findUser;
    }

    public void makeUserDTO(){

    }
}
