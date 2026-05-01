package com.example.aigc_helper.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {
    /**
     * 处理聊天请求
     *
     * @param prompt   用户输入的提示
     * @param chatId   会话ID
     * @param username 用户名
     * @return AI响应的流
     */
    Flux<String> textChat(String prompt, String chatId, String username);

    /**
     * 生成会话标题
     *
     * @param prompt 用户输入的提示词
     * @param chatId 会话ID
     * @return 生成的标题
     */
    Flux<String> generateTitle(String prompt, String chatId);

    /**
     * 处理多模态聊天请求
     *
     * @param prompt   用户输入的提示
     * @param chatId   会话ID
     * @param username 用户名
     * @param files    附件文件列表
     * @return AI响应的流
     */
    Flux<String> multiModalChat(String prompt, String chatId,String username, List<MultipartFile> files);

}
