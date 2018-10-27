package com.dahye.user.service;

import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;

public class UserGradeUpgradeService implements UserGradeUpgradePolicy {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean canUpgradeGrade(User user) {
        Grade currentGrade = user.getGrade();
        switch (currentGrade) {
            case BASIC: return (user.getLoin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw  new IllegalArgumentException("unknown level : " + currentGrade);
        }
    }

    public void upgradeGrade(User user) {
        user.upgradeGrade();
        userDao.update(user);
    }
}
