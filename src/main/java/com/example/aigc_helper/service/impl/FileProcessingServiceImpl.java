package com.example.aigc_helper.service.impl;

import com.example.aigc_helper.service.FileProcessingService;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    // 支持的图片格式
    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp"
    );

    @Override
    public List<Media> processFilesToMedia(List<MultipartFile> files, String chatId) {
        return files.stream()
                .map(this::processSingleFileToMedia)
                .collect(Collectors.toList());
    }

    private Media processSingleFileToMedia(MultipartFile file) {
        String contentType = normalizeContentType(file.getContentType());

        if (!isSupportedFileType(contentType)) {
            throw new IllegalArgumentException("不支持的文件类型: " + contentType);
        }

        try {
            return createDirectMedia(file, contentType);
        } catch (Exception e) {
            throw new RuntimeException("文件处理失败: " + file.getOriginalFilename(), e);
        }
    }

    private Media createDirectMedia(MultipartFile file, String contentType) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        return new Media(MimeType.valueOf(contentType), resource);
    }

    @Override
    public String convertToBase64DataUrl(MultipartFile file) {
        throw new UnsupportedOperationException("图片文件不需要base64编码");
    }

    @Override
    public boolean isSupportedFileType(String contentType) {
        if (contentType == null) {
            return false;
        }

        String normalizedType = normalizeContentType(contentType);
        return SUPPORTED_IMAGE_TYPES.contains(normalizedType);
    }

    @Override
    public boolean requiresBase64Encoding(String contentType) {
        return false;
    }

    /**
     * 标准化内容类型，处理一些常见的变体
     */
    private String normalizeContentType(String contentType) {
        if (contentType == null) {
            return "application/octet-stream";
        }

        // 转换为小写并去除空格
        contentType = contentType.toLowerCase().trim();

        // 处理一些常见的变体
        if ("image/jpg".equals(contentType)) {
            return "image/jpeg";
        }

        return contentType;
    }
}
