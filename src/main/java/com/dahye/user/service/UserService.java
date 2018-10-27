package com.dahye.user.service;

import com.dahye.user.dao.UserDao;
import com.dahye.user.domain.Grade;
import com.dahye.user.domain.User;

import java.util.List;

public class UserService {

    UserDao userDao;

    UserGradeUpgradePolicy userGradeUpgradeService;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserGradeUpgradeService(UserGradeUpgradePolicy userGradeUpgradeService) {
        this.userGradeUpgradeService = userGradeUpgradeService;
    }

    public void upgradeGrades() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            if(userGradeUpgradeService.canUpgradeGrade(user)) {
                userGradeUpgradeService.upgradeGrade(user);
            }
        }
    }

    public void add(User user) {
        if(user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
