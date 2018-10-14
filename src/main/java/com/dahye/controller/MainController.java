package com.dahye.controller;

import com.dahye.dao.*;
import com.dahye.user.dao.UserDao1;
import com.dahye.user.domain.User;
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

//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory1.class);
//        UserDao1 userDao = applicationContext.getBean("userDao", UserDao1.class);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingConnectionMaker.class);
        UserDao1 userDao = context.getBean("userDao", UserDao1.class);

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("connection counter : " + ccm.getCounter());
        User user = new User();
        user.setId("dahyekim");
        user.setName("김다혜");
        user.setPassword("dahye");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

    }
}