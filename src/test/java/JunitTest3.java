import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.either;
import static org.junit.matchers.JUnitMatchers.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/junit-test.xml")
public class JunitTest3 {

    @Autowired
    ApplicationContext context;

    static Set<JunitTest3> junitTest = new HashSet<JunitTest3>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

    @Test
    public void test3() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);

        assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        contextObject = this.context;
    }
}
