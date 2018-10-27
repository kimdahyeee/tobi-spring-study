package com.dahye.user.service;

import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext-*.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("dahye1", "김다혜1", "p1", Grade.BASIC, 49, 0),
                new User("dahye2", "김다혜2", "p2", Grade.BASIC, 50, 0),
                new User("dahye3", "김다혜3", "p3", Grade.SILVER, 60, 29),
                new User("dahye4", "김다혜4", "p4", Grade.SILVER, 60, 30),
                new User("dahye5", "김다혜5", "p5", Grade.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeGrade() {
        userDao.deleteAll();

        for(User user : users) userDao.add(user);

        userService.upgradeGrades();

        checkGrade(users.get(0), Grade.BASIC);
        checkGrade(users.get(1), Grade.SILVER);
        checkGrade(users.get(2), Grade.SILVER);
        checkGrade(users.get(3), Grade.GOLD);
        checkGrade(users.get(4), Grade.GOLD);
    }

    private void checkGrade(User user, Grade expectedGrade) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getGrade(), is(expectedGrade));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithGrade = users.get(4);
        User userWithoutGrade = users.get(0);
        userWithoutGrade.setGrade(null);

        userService.add(userWithGrade);
        userService.add(userWithoutGrade);

        User userWithGradeRead = userDao.get(userWithGrade.getId());
        User userWithoutGradeRead = userDao.get(userWithoutGrade.getId());

        assertThat(userWithGradeRead.getGrade(), is(userWithGrade.getGrade()));
        assertThat(userWithoutGradeRead.getGrade(), is(Grade.BASIC));
    }
}
