package learningtest.proxy;

import com.dahye.learningtest.proxy.Hello;
import com.dahye.learningtest.proxy.HelloTarget;
import com.dahye.learningtest.proxy.HelloUppercase;
import com.dahye.learningtest.proxy.UppercaseHandler;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("dahye"), is("Hello dahye"));
        assertThat(hello.sayHi("dahye"), is("Hi dahye"));
        assertThat(hello.sayThankYou("dahye"), is("Thank You dahye"));
    }

    @Test
    public void simpleProxyUppercase() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("dahye"), is("HELLO DAHYE"));
        assertThat(hello.sayHi("dahye"), is("HI DAHYE"));
        assertThat(hello.sayThankYou("dahye"), is("THANK YOU DAHYE"));
    }

    @Test
    public void simpleProxyWithHandler() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarget()));

        assertThat(hello.sayHello("dahye"), is("HELLO DAHYE"));
        assertThat(hello.sayHi("dahye"), is("HI DAHYE"));
        assertThat(hello.sayThankYou("dahye"), is("THANK YOU DAHYE"));
    }
}
