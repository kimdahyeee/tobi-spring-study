package com.dahye.user.dao;

import com.dahye.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao8 userDao8() {
        return new UserDao8(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DconnectionMaker();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao8 dao = context.getBean("userDao8", UserDao8.class);

        User user = new User();
        user.setId("dahyekim15");
        user.setName("김다혜");
        user.setPassword("dahye");

        dao.add(user);

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("connection counter : " + ccm.getCounter());
    }
}
