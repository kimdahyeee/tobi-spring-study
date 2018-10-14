package com.dahye.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker {

    int counter = 0;
    private ConnectionMaker connectionMaker;

    public CountingConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return connectionMaker.makeConnection();
    }

    public int getCounter() {
        return counter;
    }
}
