package com.dahye.user.service;

import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import static com.dahye.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.dahye.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext-*.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    List<User> users;

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeGrade(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeGrade(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("dahye1", "김다혜1", "p1", Grade.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("dahye2", "김다혜2", "p2", Grade.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("dahye3", "김다혜3", "p3", Grade.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
                new User("dahye4", "김다혜4", "p4", Grade.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("dahye5", "김다혜5", "p5", Grade.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeGrade() throws Exception {
        userDao.deleteAll();

        for(User user : users) userDao.add(user);

        userService.upgradeGrades();

        checkGradeUpgrade(users.get(0), false);
        checkGradeUpgrade(users.get(1), true);
        checkGradeUpgrade(users.get(2), false);
        checkGradeUpgrade(users.get(3), true);
        checkGradeUpgrade(users.get(4), false);
    }

    private void checkGradeUpgrade(User user, boolean changed) {
        User userUpdate = userDao.get(user.getId());
        if (changed) {
            assertThat(userUpdate.getGrade(), is(user.getGrade().nextGrade()));
        } else {
            assertThat(userUpdate.getGrade(), is(user.getGrade()));
        }
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

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(this.transactionManager);
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeGrades();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {

        }

        checkGradeUpgrade(users.get(1), false);
    }
}
