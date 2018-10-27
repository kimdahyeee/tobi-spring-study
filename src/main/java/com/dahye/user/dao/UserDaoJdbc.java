package com.dahye.user.dao;

import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import com.dahye.user.exception.DuplicateUserIdException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getString("id"));
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setGrade(Grade.valueOf(resultSet.getInt("grade")));
                    user.setLoin(resultSet.getInt("login"));
                    user.setRecommend(resultSet.getInt("recommend"));
                    return user;
                }
            };

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password, grade, login, recommend) values (?,?,?,?,?,?)",
                user.getId(), user.getName(), user.getPassword(), user.getGrade().intValue(), user.getLoin(), user.getRecommend());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                    new Object[]{id},
                    this.userMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
                this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, grade = ?, login = ?," +
                    "recommend = ? where id = ? ", user.getName(), user.getPassword(), user.getGrade().intValue(),
                    user.getLoin(), user.getRecommend(), user.getId());
    }
}
