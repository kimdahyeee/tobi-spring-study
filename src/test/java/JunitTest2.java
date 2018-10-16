import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class JunitTest2 {
    static Set<JunitTest2> junitTest = new HashSet<JunitTest2>();

    @Test
    public void test1() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);
    }

    @Test
    public void test2() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);
    }

    @Test
    public void test3() {
        assertThat(junitTest, not(hasItem(this)));
        junitTest.add(this);
    }
}
