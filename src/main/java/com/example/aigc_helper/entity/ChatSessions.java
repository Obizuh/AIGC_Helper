package com.example.aigc_helper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("chat_sessions")
public class ChatSessions {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String chatId;
    private String type;
    private LocalDateTime createTime;
    private String title;
}
