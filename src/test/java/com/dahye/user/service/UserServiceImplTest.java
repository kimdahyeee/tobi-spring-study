package com.dahye.user.service;

import com.dahye.main.MailSender;
import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dahye.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.dahye.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext-*.xml")
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @Autowired
    ApplicationContext context;

    @Autowired
    UserService testUserService;

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "dahye4";

        protected void upgradeGrade(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeGrade(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }

    static class MockMailSender implements MailSender {
        private List<String> req = new ArrayList<String>();

        public List<String> getReq() {
            return req;
        }

        public void send(SimpleMailMessage simpleMessage) throws MailException {
            req.add(simpleMessage.getTo()[0]);
        }

        public void send(SimpleMailMessage[] simpleMessages) throws MailException {

        }
    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<User>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        public List<User> getAll() {
            return this.users;
        }

        public void update(User user) {
            updated.add(user);
        }

        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        public int getCount() {
            throw new UnsupportedOperationException();
        }

    }

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("dahye1", "김다혜1", "p1", Grade.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "dahye1@dahye.com"),
                new User("dahye2", "김다혜2", "p2", Grade.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "dahye2@dahye.com"),
                new User("dahye3", "김다혜3", "p3", Grade.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "dahye3@dahye.com"),
                new User("dahye4", "김다혜4", "p4", Grade.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "dahye4@dahye.com"),
                new User("dahye5", "김다혜5", "p5", Grade.GOLD, 100, Integer.MAX_VALUE, "dahye5@dahye.com")
        );
    }

    @Test
    @DirtiesContext
    public void upgradeGrade() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeGrades();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndGrade(updated.get(0), "dahye2", Grade.SILVER);
        checkUserAndGrade(updated.get(1), "dahye4", Grade.GOLD);

        List<String> request = mockMailSender.getReq();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndGrade(User updated, String expectedId, Grade expectedGrade) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getGrade(), is(expectedGrade));
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

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeGrades();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {

        }

        checkGradeUpgrade(users.get(1), false);
    }

    @Test
    public void mockUpgradeGrades() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeGrades();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getGrade(), is(Grade.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getGrade(), is(Grade.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }
}
