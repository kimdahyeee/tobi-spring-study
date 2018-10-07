package com.dahye.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DconnectionMaker implements ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.jdbc.driver.OracleDriver");
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/dahye:orcl", "dahye", "dahye");

        return c;
    }
}
