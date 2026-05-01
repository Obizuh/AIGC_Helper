package com.example.aigc_helper.controller;

import com.example.aigc_helper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatService chatService;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam String prompt,
                             @RequestParam String chatId,
                             @RequestParam String username,
                             @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            return chatService.textChat(prompt, chatId, username);
        } else {
            return chatService.multiModalChat(prompt, chatId, username, files);
        }
    }

    @PostMapping(value = "/generate-title", produces = "text/plain;charset=utf-8")
    public Flux<String> generateTitle(@RequestParam String prompt,
                                    @RequestParam String chatId) {
        return chatService.generateTitle(prompt, chatId);
    }


    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {

        String errorMessage = String.format(
            "文件上传失败：文件大小超过限制。最大支持单文件50MB，总请求大小200MB。错误详情: %s",
            e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(errorMessage);
    }

    /**
     * 处理其他文件上传相关异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        if (e.getMessage() != null && e.getMessage().contains("upload")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("文件上传处理失败: " + e.getMessage());
        }

        // 其他异常继续抛出
        throw new RuntimeException(e);
    }
}
