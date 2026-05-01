package com.example.aigc_helper.interceptor;

import com.example.aigc_helper.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // 对预检请求放行并记录日志
        if ("OPTIONS".equalsIgnoreCase(method)) {
            logger.info("收到OPTIONS预检请求: {} {}", method, path);
            logger.info("来源地址: {}", request.getHeader("Origin"));
            logger.info("请求头信息: {}", request.getHeader("Access-Control-Request-Headers"));

            // 设置CORS响应头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Max-Age", "3600");

            logger.info("OPTIONS预检请求已放行");
            return true;    //直接放行预检请求，不进行JWT验证
        }

        logger.info("处理常规请求: {} {}", method, path);

        // 获取请求头中的token
        String token = request.getHeader("Authorization");

        // 检查token是否存在
        if (token == null || token.isEmpty()) {
            logger.warn("请求缺少Authorization头信息");
            writeErrorResponse(response, "用户登录状态异常，请重新登录");
            return false;
        }

        // 如果token以"Bearer "开头，则去掉前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token是否有效
        if (!jwtUtil.validateToken(token)) {
            logger.warn("无效的JWT令牌");
            writeErrorResponse(response, "JWT令牌无效或已过期，请重新登录");
            return false;
        }

        // 将用户信息存储到request中，供后续使用
        try {
            Long userId = jwtUtil.getUserIdFromToken(token);
            String username = jwtUtil.getUsernameFromToken(token);
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            logger.info("JWT验证成功，用户: {}, ID: {}", username, userId);
        } catch (Exception e) {
            logger.error("JWT解析失败", e);
            writeErrorResponse(response, "JWT令牌解析失败");
            return false;
        }

        return true;
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("message", message);
        result.put("data", null);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(result));
        logger.info("返回错误响应: {}", message);
    }
}