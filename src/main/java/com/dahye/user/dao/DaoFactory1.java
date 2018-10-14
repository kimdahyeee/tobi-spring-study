package com.dahye.user.dao;

//object 구성, 그 관계를 정의하는 책임을 맡게됨
public class DaoFactory1 {

    public UserDao7 userDao7() {
        ConnectionMaker connectionMaker = new DconnectionMaker();
        UserDao7 userDao7 = new UserDao7(connectionMaker);

        return userDao7;
    }
}
