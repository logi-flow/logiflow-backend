package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.admin.response.AdminResetPasswordResponseDto;
import com.logi_flow.backend.dto.uploadFile.response.FileResponseDto;
import com.logi_flow.backend.entity.UploadFile;
import com.logi_flow.backend.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.TargetType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.FILE_API)
public class UploadFileController {
    private final UploadFileService uploadFileService;

    private static final String FILE_API = "/{targetId}";

    @PostMapping
    public UploadFile uploadProfile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("targetId") Long targetId
    ) {
        return uploadFileService.uploadProfile(file, targetId);
    }

    @GetMapping(FILE_API)
    public ResponseEntity<ResponseDto<FileResponseDto>> getProfile(
            @PathVariable Long targetId
    ) {
        ResponseDto<FileResponseDto> response = uploadFileService.getProfile(targetId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(FILE_API)
    public ResponseEntity<ResponseDto<Void>> deleteProfile(
            @PathVariable Long targetId
    ) {
        ResponseDto<Void> response = uploadFileService.deleteProfile(targetId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
