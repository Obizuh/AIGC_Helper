package com.example.aigc_helper.service;

import com.example.aigc_helper.entity.ChatSessions;
import java.util.List;

public interface ChatSessionsService {

    ChatSessions createSessionIfNull(Long userId, String type);
    // 创建新的会话
    ChatSessions createSession(Long userId, String type, String chatId);

    // 获取用户的所有会话列表
    List<ChatSessions> getUserSessions(Long userId);

    // 获取指定会话
    ChatSessions getSessionByChatId(String chatId);

    // 删除会话
    boolean deleteSession(String chatId, Long userId);

    // 更新会话标题
    boolean updateById(ChatSessions session);
}
