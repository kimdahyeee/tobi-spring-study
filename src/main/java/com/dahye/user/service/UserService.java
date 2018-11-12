package com.dahye.user.service;

import com.dahye.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeGrades();
}
