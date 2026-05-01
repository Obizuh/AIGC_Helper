package com.example.aigc_helper.service;

import com.example.aigc_helper.entity.ChatHistory;
import java.util.List;

public interface ChatHistoryService {
    // 保存聊天记录
    boolean saveMessage(String chatId, String role, String content, String modelName);

    // 获取指定会话的所有聊天记录
    List<ChatHistory> getHistoryByChatId(String chatId);

    // 删除指定会话的所有聊天记录
    boolean deleteHistoryByChatId(String chatId);
}
