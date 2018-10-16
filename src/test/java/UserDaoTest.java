import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/applicationContext-*.xml")
public class UserDaoTest {

//    @Autowired
//    private ApplicationContext context;

    @Autowired
    private UserDao dao;

    private User user;
    private User user2;
    private User user3;

    @Before
    public void setup() {
        this.user = new User("dahyekim", "김다혜", "dahye");
        this.user2 = new User("dahyekim1", "김다혜", "dahye");
        this.user3 = new User("dahyekim3", "김다혜", "dahye");
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userGet1 = dao.get(user.getId());
        assertThat(userGet1.getId(), is(user.getId()));
        assertThat(userGet1.getPassword(), is(user.getPassword()));

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getId(), is(user2.getId()));
        assertThat(userGet2.getPassword(), is(user2.getPassword()));
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

}
