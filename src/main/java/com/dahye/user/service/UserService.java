package com.dahye.user.service;

import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;

import java.util.List;

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeGrades() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            Boolean changed = null;
            if (user.getGrade().equals(Grade.BASIC) && user.getLoin() >= 50) {
               user.setGrade(Grade.SILVER);
               changed = true;
            } else if (user.getGrade().equals(Grade.SILVER) && user.getRecommend() >= 30) {
                user.setGrade(Grade.GOLD);
                changed = true;
            } else if (user.getGrade().equals(Grade.GOLD)) {
                changed = false;
            } else {
                changed = false;
            }

            if(changed) userDao.update(user);
        }
    }

    public void add(User user) {
        if(user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
