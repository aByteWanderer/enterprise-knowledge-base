package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章版本实体类
 */
@Data
@TableName("tb_article_versions")
public class ArticleVersion {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long articleId;
    
    private String title;
    
    private String content;
    
    private Integer version;
    
    private Long createdBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
}
