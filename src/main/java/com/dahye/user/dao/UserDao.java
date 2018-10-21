package com.dahye.user.dao;

import com.dahye.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                    new Object[]{id},
                    new RowMapper<User>() {
                        public User mapRow(ResultSet resultSet, int i) throws SQLException {
                            User user = new User();
                            user.setId(resultSet.getString("id"));
                            user.setName(resultSet.getString("name"));
                            user.setPassword(resultSet.getString("password"));
                            return user;
                        }
                });
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
                new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setId(resultSet.getString("id"));
                        user.setName(resultSet.getString("name"));
                        user.setPassword(resultSet.getString("password"));
                        return user;
                    }
                });
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}
