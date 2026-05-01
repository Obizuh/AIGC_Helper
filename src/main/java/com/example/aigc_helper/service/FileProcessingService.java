package com.example.aigc_helper.service;

import org.springframework.ai.content.Media;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件处理服务接口
 * 负责处理多媒体文件的格式转换和验证
 */
public interface FileProcessingService {

    /**
     * 将上传的文件列表转换为Media对象列表
     * 对于音频和视频文件，会转换为base64编码格式
     *
     * @param files 上传的文件列表
     * @param chatId 会话ID，用于日志记录
     * @return 转换后的Media对象列表
     */
    List<Media> processFilesToMedia(List<MultipartFile> files, String chatId);

    /**
     * 检查文件类型是否支持
     *
     * @param contentType 文件MIME类型
     * @return 是否支持该文件类型
     */
    boolean isSupportedFileType(String contentType);

    /**
     * 检查文件是否需要base64编码
     *
     * @param contentType 文件MIME类型
     * @return 是否需要base64编码
     */
    boolean requiresBase64Encoding(String contentType);

    /**
     * 将文件转换为base64编码的Data URL格式
     *
     * @param file 原始文件
     * @return base64编码的Data URL字符串
     */
    String convertToBase64DataUrl(MultipartFile file);
}
