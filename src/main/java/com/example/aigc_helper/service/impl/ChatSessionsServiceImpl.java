package com.example.aigc_helper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aigc_helper.entity.ChatSessions;
import com.example.aigc_helper.mapper.ChatSessionsMapper;
import com.example.aigc_helper.service.ChatSessionsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatSessionsServiceImpl extends ServiceImpl<ChatSessionsMapper, ChatSessions> implements ChatSessionsService {

    @Override
    public ChatSessions createSessionIfNull(Long userId, String type) {
        ChatSessions session = new ChatSessions();
        session.setUserId(userId);
        session.setChatId(UUID.randomUUID().toString());
        session.setType(type);
        session.setCreateTime(LocalDateTime.now());
        save(session);
        return session;
    }

    @Override
    public ChatSessions createSession(Long userId, String type, String chatId) {
        ChatSessions session = new ChatSessions();
        session.setUserId(userId);
        session.setType(type);
        session.setChatId(chatId);
        session.setCreateTime(LocalDateTime.now());
        save(session);
        return session;
    }

    @Override
    public List<ChatSessions> getUserSessions(Long userId) {
        LambdaQueryWrapper<ChatSessions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatSessions::getUserId, userId)
                   .orderByDesc(ChatSessions::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public ChatSessions getSessionByChatId(String chatId) {
        LambdaQueryWrapper<ChatSessions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatSessions::getChatId, chatId);
        return getOne(queryWrapper);
    }

    @Override
    public boolean deleteSession(String chatId, Long userId) {
        LambdaQueryWrapper<ChatSessions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatSessions::getChatId, chatId)
                   .eq(ChatSessions::getUserId, userId);
        return remove(queryWrapper);
    }

    @Override
    public boolean updateById(ChatSessions session) {
        if (session == null || session.getId() == null) {
            return false;
        }
        return super.updateById(session);
    }
}
