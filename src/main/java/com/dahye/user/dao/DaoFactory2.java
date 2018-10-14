package com.dahye.user.dao;

public class DaoFactory2 {

    public UserDao7 userDao7() {
        return new UserDao7(connectionMaker());
    }

    //예를 들어 다른 accountDao, ..등등을 만들게 된다면..new DconnectionMaker()라는 코드가 중복되게 됨.
    // 메서드 분리
    public ConnectionMaker connectionMaker() {
        return new DconnectionMaker();
    }
}
