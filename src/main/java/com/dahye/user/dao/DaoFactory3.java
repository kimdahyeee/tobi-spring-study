package com.dahye.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory3 {

    @Bean //오브젝트 생성을 담당하는 ioc용 메서드라는 표시
    public UserDao8 userDao8() {
        return new UserDao8(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DconnectionMaker();
    }

}
