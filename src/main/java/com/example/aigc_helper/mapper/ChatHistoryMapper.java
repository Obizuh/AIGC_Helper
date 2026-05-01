package com.example.aigc_helper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.aigc_helper.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {
}
