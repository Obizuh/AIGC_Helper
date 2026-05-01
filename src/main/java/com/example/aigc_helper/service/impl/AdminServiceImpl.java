package com.example.aigc_helper.service.impl;

import com.example.aigc_helper.entity.Admin;
import com.example.aigc_helper.mapper.AdminMapper;
import com.example.aigc_helper.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean registerAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminMapper.insert(admin) > 0;
    }

    @Override
    public Admin loginAdmin(String username, String password) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }
}
