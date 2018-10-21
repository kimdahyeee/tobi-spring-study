package com.dahye.user.dao;

import com.dahye.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("classpath:applicationContext-*.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("dahyekim16");
        user.setName("김다혜");
        user.setPassword("dahye");

        dao.add(user);

        System.out.println(user.getId() +" 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
