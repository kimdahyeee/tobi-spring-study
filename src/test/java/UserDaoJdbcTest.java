import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext-*.xml")
public class UserDaoJdbcTest {

    @Autowired
    private UserDao dao;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setup() {
        this.user = new User("dahyekim", "김다혜", "dahye", Grade.BASIC, 1, 0, "dahye1@dahye.com");
        this.user2 = new User("dahyekim1", "김다혜", "dahye", Grade.SILVER, 55, 10, "dahye2@dahye.com");
        this.user3 = new User("dahyekim3", "김다혜", "dahye", Grade.GOLD, 100, 40, "dahye3@dahye.com");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userGet1 = dao.get(user.getId());
        checkSameUser(user, userGet1);

        User userGet2 = dao.get(user2.getId());
        checkSameUser(user2, userGet2);
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users = dao.getAll();
        assertThat(users.size(), is(0));

        dao.add(user);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getGrade(), is(user2.getGrade()));
        assertThat(user1.getLoin(), is(user2.getLoin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
        assertThat(user1.getEmail(), is(user2.getEmail()));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user);
        dao.add(user);
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user);
        dao.add(user2);

        user.setName("수정다혜");
        user.setPassword("update");
        user.setGrade(Grade.GOLD);
        user.setLoin(1000);
        user.setRecommend(999);
        dao.update(user);

        User userUpdate = dao.get(user.getId());
        checkSameUser(user, userUpdate);

        User user2Same = dao.get(user2.getId());
        checkSameUser(user2, user2Same);
    }
}
