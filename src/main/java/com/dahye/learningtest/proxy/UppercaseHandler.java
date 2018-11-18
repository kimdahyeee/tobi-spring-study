package com.dahye.learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    Object target;

    // 어떤 종류의 인터페이스를 구현한 타겟에도 적용 가능하도록 Object타입으로 수정
    public UppercaseHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);

        //String 인 경우에만 대문자 변경하도록
        if(ret instanceof String) {
            return ((String)ret).toUpperCase();
        } else {
            return ret;
        }
    }
}
