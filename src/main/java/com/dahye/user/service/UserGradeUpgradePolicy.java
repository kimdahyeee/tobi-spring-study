package com.dahye.user.service;

import com.dahye.user.domain.User;

public interface UserGradeUpgradePolicy {
    boolean canUpgradeGrade(User user);
    void upgradeGrade(User user);
}
