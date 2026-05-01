package com.example.aigc_helper.controller;

import com.example.aigc_helper.entity.Admin;
import com.example.aigc_helper.entity.Result;
import com.example.aigc_helper.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Admin admin) {
        if (adminService.registerAdmin(admin)) {
            return Result.success();
        }
        return Result.error("注册失败");
    }

    @PostMapping("/login")
    public Result<Admin> login(@RequestParam String username, @RequestParam String password) {
        Admin admin = adminService.loginAdmin(username, password);
        if (admin != null) {
            return Result.success(admin);
        }
        return Result.error("登录失败");
    }
}
