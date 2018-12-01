package learningtest.jdk.proxy;

import com.dahye.learningtest.proxy.Hello;
import com.dahye.learningtest.proxy.HelloTarget;
import com.dahye.learningtest.proxy.HelloUppercase;
import com.dahye.learningtest.proxy.UppercaseHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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
        com.dahye.learningtest.proxy.Hello hello = new HelloUppercase(new com.dahye.learningtest.proxy.HelloTarget());
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

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        //싱글톤으로 호출 가능
        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("dahye"), is("HELLO DAHYE"));
        assertThat(proxiedHello.sayHi("dahye"), is("HI DAHYE"));
        assertThat(proxiedHello.sayThankYou("dahye"), is("THANK YOU DAHYE"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("dahye"), is("HELLO DAHYE"));
        assertThat(proxiedHello.sayHi("dahye"), is("HI DAHYE"));
        assertThat(proxiedHello.sayThankYou("dahye"), is("Thank You dahye"));
    }

    static class UppercaseAdvice implements MethodInterceptor {

        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }

    static interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello {
        public String sayHello(String name) {
            return "Hello " + name;
        }

        public String sayHi(String name) {
            return "Hi " + name;
        }

        public String sayThankYou(String name) {
            return "Thank You " + name;
        }
    }

}
