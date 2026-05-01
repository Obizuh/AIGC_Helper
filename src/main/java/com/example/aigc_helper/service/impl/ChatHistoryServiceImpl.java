package com.example.aigc_helper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aigc_helper.entity.ChatHistory;
import com.example.aigc_helper.mapper.ChatHistoryMapper;
import com.example.aigc_helper.service.ChatHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Override
    public boolean saveMessage(String chatId, String role, String content, String modelName) {
        ChatHistory history = new ChatHistory();
        history.setChatId(chatId);
        history.setRole(role);
        history.setContent(content);
        history.setModelName(modelName);
        history.setCreateTime(LocalDateTime.now());
        history.setUpdateTime(LocalDateTime.now());
        return save(history);
    }

    @Override
    public List<ChatHistory> getHistoryByChatId(String chatId) {
        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getChatId, chatId)
                   .orderByAsc(ChatHistory::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public boolean deleteHistoryByChatId(String chatId) {
        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getChatId, chatId);
        return remove(queryWrapper);
    }
}
