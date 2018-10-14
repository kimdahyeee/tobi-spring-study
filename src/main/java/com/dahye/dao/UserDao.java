package com.dahye.dao;

import com.dahye.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private ConnectionMaker connectionMaker;

    //의존 관계 주입
    public UserDao() {
//        DaoFactory1 daoFactory = new DaoFactory1();
//        this.connectionMaker = daoFactory.connectionMaker();

        //애플리케이션 컨텍스트 이용
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory1.class);
//        this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);

//        this.connectionMaker = connectionMaker;
    }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
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
