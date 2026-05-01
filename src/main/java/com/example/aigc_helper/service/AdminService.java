package com.example.aigc_helper.service;

import com.example.aigc_helper.entity.Admin;

public interface AdminService {
    boolean registerAdmin(Admin admin);
    Admin loginAdmin(String username, String password);
}
