package com.dahye.dao;

import com.dahye.domain.User;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.*;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

//    private Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.jdbc.driver.OracleDriver");
//        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/dahye:orcl", "dahye", "dahye");
//
//        return c;
//    }

    // 해당 메소드의 구현은 서브클래스가 담당
    // 템플릿 메소드 패턴
//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
