import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.User;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("file:src/main/resources/applicationContext-*.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("dahyekim19");
        user.setName("김다혜");
        user.setPassword("dahye");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user2.getId(), is(user.getId()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }

    public static void main(String[] args) {
        JUnitCore.main("UserDaoTest");
    }
}
