package com.example.aigc_helper.service.impl;

import com.example.aigc_helper.entity.User;
import com.example.aigc_helper.mapper.UserMapper;
import com.example.aigc_helper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已被占用");
        }

        // 加密密码并保存用户
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 其他字段可由前端传递或此处补充
        return userMapper.insert(user) > 0;
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", username)
        );
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("username", username)
        );
    }
}
