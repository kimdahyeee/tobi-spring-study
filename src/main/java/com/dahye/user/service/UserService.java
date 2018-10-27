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
            if(canUpgradeGrade(user)) {
                upgradeGrade(user);
            }
        }
    }

    private boolean canUpgradeGrade(User user) {
        Grade currentGrade = user.getGrade();
        switch (currentGrade) {
            case BASIC: return (user.getLoin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw  new IllegalArgumentException("unknown level : " + currentGrade);
        }
    }

    private void upgradeGrade(User user) {
        if(user.getGrade().equals(Grade.BASIC)) user.setGrade(Grade.SILVER);
        else if(user.getGrade().equals(Grade.SILVER)) user.setGrade(Grade.GOLD);

        userDao.update(user);
    }

    public void add(User user) {
        if(user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
