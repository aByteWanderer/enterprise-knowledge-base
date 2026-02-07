package com.knowledge.base.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 附件VO
 */
@Data
public class AttachmentVO {
    
    private Long id;
    
    private String originalName;
    
    private String fileName;
    
    private Long fileSize;
    
    private String fileType;
    
    private String filePath;
    
    private String thumbnailPath;
    
    private LocalDateTime createdAt;
    
}
