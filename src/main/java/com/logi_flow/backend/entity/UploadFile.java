package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.tool.schema.TargetType;

@Entity
@Table(name = "upload_files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UploadFile extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    public void updateFile(String originalName, String fileName, String fileType, Long fileSize) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}
