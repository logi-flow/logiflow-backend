package com.logi_flow.backend.dto.uploadFile.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FileResponseDto {
    private Long id;
    private String originalName;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private Long targetId;
}
