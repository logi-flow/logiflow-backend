package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.uploadFile.response.FileResponseDto;
import com.logi_flow.backend.entity.UploadFile;
import com.logi_flow.backend.repository.UploadFileRepository;
import com.logi_flow.backend.service.UploadFileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UploadFileServiceImpl implements UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public UploadFile uploadProfile(MultipartFile file, Long targetId) {
        try {
            UploadFile existing = uploadFileRepository.findByTargetId(targetId)
                    .orElse(null);

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String original = file.getOriginalFilename();
            String uuidName = UUID.randomUUID() + "_" + original;
            String fullPath = uploadDir + "/" + uuidName;
            file.transferTo(new File(fullPath));

            if (existing != null) {
                uploadFileRepository.delete(existing);
            }

            UploadFile uf = UploadFile.builder()
                    .originalName(original)
                    .fileName(uuidName)
                    .filePath(uploadDir + "/")
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .targetId(targetId)
                    .build();
            uploadFileRepository.save(uf);

            if (existing != null) {
                try {
                    Files.deleteIfExists(Paths.get(existing.getFilePath(), existing.getFileName()));
                } catch (IOException e) {
                    throw new EntityNotFoundException(ResponseMessage.FILE_NOT_FOUND);
                }
            }

            return uf;
        } catch (IOException e) {
            throw new EntityNotFoundException(ResponseMessage.FAILED);
        }
    }

    @Override
    public ResponseDto<FileResponseDto> getProfile(Long targetId) {
        FileResponseDto data = null;

        UploadFile uf = uploadFileRepository.findByTargetId(targetId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = FileResponseDto.builder()
                .id(uf.getId())
                .originalName(uf.getOriginalName())
                .fileName(uf.getFileName())
                .filePath(uf.getFilePath())
                .fileType(uf.getFileType())
                .fileSize(uf.getFileSize())
                .targetId(uf.getTargetId())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Void> deleteProfile(Long targetId) {
        UploadFile uf = uploadFileRepository.findByTargetId(targetId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        try {
            Files.deleteIfExists(Paths.get(uf.getFilePath(), uf.getFileName()));
        } catch (IOException e) {
            return ResponseDto.fail(ResponseCode.FAILED, ResponseMessage.FAILED);
        }

        uploadFileRepository.delete(uf);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
}
