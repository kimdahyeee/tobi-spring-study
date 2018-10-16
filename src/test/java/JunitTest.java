import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class JunitTest {
    static JunitTest junitTest;

    @Test
    public void test1() {
        assertThat(this, is(not(sameInstance(junitTest))));
        junitTest = this;
    }

    @Test
    public void test2() {
        assertThat(this, is(not(sameInstance(junitTest))));
        junitTest = this;
    }

    @Test
    public void test3() {
        assertThat(this, is(not(sameInstance(junitTest))));
        junitTest = this;
    }
}
