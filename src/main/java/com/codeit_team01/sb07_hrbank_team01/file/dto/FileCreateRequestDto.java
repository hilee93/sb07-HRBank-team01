package com.codeit_team01.sb07_hrbank_team01.file.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record FileCreateRequestDto(
        byte[] bytes,
        String name,
        String type
) {
    public static FileCreateRequestDto from(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file이 비어있습니다.");
        }
        try {
            return new FileCreateRequestDto(
                    file.getBytes(),
                    file.getName(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽을 수 없습니다.",e);
        }
    }
}
