package com.example.aigc_helper.config;

import com.example.aigc_helper.constants.SystemConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 配置聊天客户端
@Configuration
public class CommonConfiguration {

    /*
     * 每一个client都支持自定义模型配置，使用.defaultOptions(ChatOptions.build().model("qwen-omni-turbo").build())
     * */
    @Bean
    public ChatClient chatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.CHAT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        // 此处的 MessageChatMemoryAdvisor 环绕增强主要用于上下文记忆，将上文对话拼接进prompt中
                        MessageChatMemoryAdvisor.builder(
                                chatMemory
                        ).build()
                ) // 配置日志Advisor
                .build();   // 构建Client实例
    }

    @Bean
    public ChatClient titleClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder().model("qwen-max").build())
                .defaultSystem(SystemConstants.TITLE_GENERATION_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        // 此处的 MessageChatMemoryAdvisor 环绕增强主要用于上下文记忆，将上文对话拼接进prompt中
                        MessageChatMemoryAdvisor.builder(
                                chatMemory
                        ).build()
                ) // 配置日志Advisor
                .build();   // 构建Client实例
    }

    @Bean
    public ChatClient gameClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        // 此处的 MessageChatMemoryAdvisor 环绕增强主要用于上下文记忆，将上文对话拼接进prompt中
                        MessageChatMemoryAdvisor.builder(
                                chatMemory
                        ).build()
                ) // 配置日志Advisor
                .build();   // 构建Client实例
    }


    @Bean
    public ChatClient multiChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient
                .builder(model)
                .defaultOptions(ChatOptions.builder().model("qwen-omni-turbo").build())
                .defaultSystem(SystemConstants.CHAT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        // 此处的 MessageChatMemoryAdvisor 环绕增强主要用于上下文记忆，将上文对话拼接进prompt中
                        MessageChatMemoryAdvisor.builder(
                                chatMemory
                        ).build()
                ) // 配置日志Advisor
                .build();   // 构建Client实例
    }

    /*@Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)
                .defaultSystem("请根据上下文回答问题，遇到上下文没有的问题，不要随意编造。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(
                                chatMemory
                        ).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .similarityThreshold(0.6)
                                        .topK(2)
                                        .build())
                                .order(0) // 保留默认顺序，可省略
                                .protectFromBlocking(true) // 替代旧版 protectFromBlocking 参数
                                .build()
                )
                .build();
    }

    @Bean
    public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel){
        return SimpleVectorStore.builder(embeddingModel).build();
    }*/
}
