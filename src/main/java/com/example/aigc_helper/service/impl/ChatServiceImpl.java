package com.example.aigc_helper.service.impl;

import com.example.aigc_helper.entity.ChatSessions;
import com.example.aigc_helper.entity.User;
import com.example.aigc_helper.service.ChatHistoryService;
import com.example.aigc_helper.service.ChatService;
import com.example.aigc_helper.service.ChatSessionsService;
import com.example.aigc_helper.service.FileProcessingService;
import com.example.aigc_helper.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final String model_name = "qwen-turbo-latest";
    private final ChatClient chatClient;
    private final ChatClient titleClient;
    private final ChatClient multiChatClient;
    private final ChatSessionsService chatSessionsService;
    private final ChatHistoryService chatHistoryService;
    private final UserService userService;
    private final FileProcessingService fileProcessingService;



    @Override
    public Flux<String> textChat(String prompt, String chatId, String username) {
        // 获取用户信息并验证
        User user = getUserAndValidate(username);
        Long userId = Long.valueOf(user.getId());

        // 检查会话是否存在，如果不存在则创建
        ChatSessions session = chatSessionsService.getSessionByChatId(chatId);
        if (session == null) {
            session = chatSessionsService.createSession(userId, "chat", chatId);
        } else if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("无效的聊天会话");
        }

        // 保存用户的消息
        chatHistoryService.saveMessage(chatId, "user", prompt, null);

        // 使用StringBuilder收集完整的AI响应
        StringBuilder fullResponse = new StringBuilder();

        // 获取AI响应
        return chatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    chatHistoryService.saveMessage(chatId, "assistant", fullResponse.toString(), model_name);
                })
                .doOnError(error -> {
                    throw new RuntimeException("文本聊天处理失败", error);
                });
    }

    @Override
    public Flux<String> generateTitle(String prompt, String chatId) {
        // 检查会话是否存在
        ChatSessions session = chatSessionsService.getSessionByChatId(chatId);
        if (session == null) {
            throw new RuntimeException("会话不存在");
        }

        // 使用StringBuilder收集完整的AI响应
        StringBuilder fullResponse = new StringBuilder();

        return titleClient.prompt()
                .user(prompt)
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    String title = fullResponse.toString().trim();
                    session.setTitle(title);
                    session.setCreateTime(LocalDateTime.now());
                    chatSessionsService.updateById(session);
                })
                .doOnError(error -> {
                    throw new RuntimeException("标题生成失败", error);
                });
    }

    @Override
    public Flux<String> multiModalChat(String prompt, String chatId, String username, List<MultipartFile> files) {
        // 获取用户信息并验证
        User user = getUserAndValidate(username);
        Long userId = Long.valueOf(user.getId());

        // 检查会话是否存在，如果不存在则创建
        ChatSessions session = validateOrCreateSession(chatId, userId, "files");

        // 保存用户的消息
        chatHistoryService.saveMessage(chatId, "user", prompt, null);

        // 使用StringBuilder收集完整的AI响应
        StringBuilder fullResponse = new StringBuilder();

        // 使用FileProcessingService处理文件
        List<Media> medias = processImageFiles(files, chatId);

        // 调用多模态AI模型
        return callMultiModalAI(prompt, chatId, medias, fullResponse);
    }

    /**
     * 处理多模态文件，转换为Media对象
     */
    private List<Media> processImageFiles(List<MultipartFile> files, String chatId) {
        // 预检查文件类型
        validateFileTypes(files);

        // 使用专门的文件处理服务转换文件
        return fileProcessingService.processFilesToMedia(files, chatId);
    }

    /**
     * 验证文件类型
     */
    private void validateFileTypes(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!fileProcessingService.isSupportedFileType(contentType)) {
                String errorMsg = String.format("不支持的文件类型: %s (文件: %s)",
                    contentType, file.getOriginalFilename());
                throw new IllegalArgumentException(errorMsg);
            }
        }
    }

    /**
     * 调用多模态AI模型
     */
    private Flux<String> callMultiModalAI(String prompt, String chatId, List<Media> medias, StringBuilder fullResponse) {
        return multiChatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(() -> {
                    chatHistoryService.saveMessage(chatId, "assistant", fullResponse.toString(), model_name);
                })
                .doOnError(error -> {
                    throw new RuntimeException("多模态AI调用失败", error);
                });
    }

    /**
     * 验证或创建会话
     */
    private ChatSessions validateOrCreateSession(String chatId, Long userId, String sessionType) {
        ChatSessions session = chatSessionsService.getSessionByChatId(chatId);
        if (session == null) {
            session = chatSessionsService.createSession(userId, sessionType, chatId);
        } else if (!session.getUserId().equals(userId)) {
            throw new RuntimeException("无效的聊天会话");
        }
        return session;
    }

    private User getUserAndValidate(String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
}
