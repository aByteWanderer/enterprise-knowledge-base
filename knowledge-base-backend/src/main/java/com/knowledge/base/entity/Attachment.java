package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 附件实体类
 */
@Data
@TableName("tb_attachments")
public class Attachment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String originalName;
    
    private String fileName;
    
    private String filePath;
    
    private Long fileSize;
    
    private String fileType;
    
    private String fileMd5;
    
    private Long articleId;
    
    private String thumbnailPath;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableLogic
    private Integer deleted;
    
}
