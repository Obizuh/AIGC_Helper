package com.example.aigc_helper.controller;

import com.example.aigc_helper.entity.ChatHistory;
import com.example.aigc_helper.entity.ChatSessions;
import com.example.aigc_helper.entity.Result;
import com.example.aigc_helper.entity.User;
import com.example.aigc_helper.service.ChatHistoryService;
import com.example.aigc_helper.service.ChatSessionsService;
import com.example.aigc_helper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatSessionsService chatSessionsService;
    private final ChatHistoryService chatHistoryService;
    private final UserService userService;

    /**
     * 获取用户的所有聊天会话列表
     * @param username 用户名
     * @return 会话列表
     */
    @GetMapping("/sessions/{username}")
    public Result<List<ChatSessions>> getUserSessions(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(chatSessionsService.getUserSessions(Long.valueOf(user.getId())));
    }

    /**
     * 创建新的聊天会话
     * @param username 用户名
     * @param type 会话类型
     * @return 新创建的会话信息
     */
    @PostMapping("/sessions")
    public Result<ChatSessions> createSession(@RequestParam String username, @RequestParam String type) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(chatSessionsService.createSessionIfNull(Long.valueOf(user.getId()), type));
    }

    /**
     * 获取指定会话的聊天记录
     * @param chatId 会话ID
     * @return 聊天记录列表
     */
    @GetMapping("/messages/{chatId}")
    public Result<List<ChatHistory>> getChatHistory(@PathVariable("chatId") String chatId) {
        return Result.success(chatHistoryService.getHistoryByChatId(chatId));
    }

    /**
     * 删除会话及其聊天记录
     * @param chatId 会话ID
     * @param username 用户名
     * @return 操作结果
     */
    @DeleteMapping("/sessions/{chatId}")
    public Result<Boolean> deleteSession(@PathVariable("chatId") String chatId, @RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        chatHistoryService.deleteHistoryByChatId(chatId);
        return Result.success(chatSessionsService.deleteSession(chatId, Long.valueOf(user.getId())));
    }
}
