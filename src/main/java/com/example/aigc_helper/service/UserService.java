package com.example.aigc_helper.service;

import com.example.aigc_helper.entity.User;

public interface UserService {
    boolean registerUser(User user);
    User loginUser(String username, String password);
    User getUserByUsername(String username);
}
