package com.dahye.controller;

import com.dahye.dao.ConnectionMaker;
import com.dahye.dao.DaoFactory;
import com.dahye.dao.DconnectionMaker;
import com.dahye.dao.UserDao;
import com.dahye.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
public class MainController {

    @RequestMapping(value = "/")
    public String test() {

        return "index";
    }

    @RequestMapping(value = "/test")
    public void testJDBC() throws ClassNotFoundException, SQLException {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("dahyekim");
        user.setName("김다혜");
        user.setPassword("dahye");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

    }
}