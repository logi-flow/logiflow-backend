package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.uploadFile.response.FileResponseDto;
import com.logi_flow.backend.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    UploadFile uploadProfile(MultipartFile file, Long targetId);

    ResponseDto<FileResponseDto> getProfile(Long targetId);

    ResponseDto<Void> deleteProfile(Long targetId);
}
