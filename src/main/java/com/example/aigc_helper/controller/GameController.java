package com.example.aigc_helper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class GameController {

    private final ChatClient gameClient;

    public GameController(@Qualifier("gameClient") ChatClient gameClient) {
        this.gameClient = gameClient;
    }

    @RequestMapping(value = "/game",produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt, String chatId){
        return gameClient.prompt()
                .user(prompt)
                .advisors( advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,chatId))
                .stream()
                .content();
    }

}
