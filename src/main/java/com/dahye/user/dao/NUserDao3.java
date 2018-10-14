package com.dahye.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao3 extends UserDao3 {

    //상속을 통해 확장

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@172.30.1.24:1521:orcl", "dahye", "dahye");

        return c;
    }
}
