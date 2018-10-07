package com.dahye.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());

        return userDao;
    }

    // 따로 구현해 둠으로써 connectionMaker에 대해 수정이 필요할 때
    // 이 곳만 수정하면 됨.
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DconnectionMaker();
    }
}
