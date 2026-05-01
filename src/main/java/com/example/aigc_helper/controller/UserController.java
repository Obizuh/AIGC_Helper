package com.example.aigc_helper.controller;

import com.example.aigc_helper.entity.Result;
import com.example.aigc_helper.entity.User;
import com.example.aigc_helper.service.UserService;
import com.example.aigc_helper.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        try {
            boolean success = userService.registerUser(user);
            if (success) {
                return Result.success();
            }
            return Result.error("注册失败");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("注册过程中发生错误");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.loginUser(username, password);
        if (user != null) {
            // 生成JWT令牌
            String token = jwtUtil.generateToken(Long.valueOf(user.getId()), user.getUsername());

            return Result.success(token);
        }
        return Result.error("登录失败");
    }
}
