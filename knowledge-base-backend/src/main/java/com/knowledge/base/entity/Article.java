package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章实体类
 */
@Data
@TableName("tb_articles")
public class Article {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String content;
    
    private String summary;
    
    private Long categoryId;
    
    private Long authorId;
    
    private String status;
    
    private Integer version;
    
    private Long viewCount;
    
    private String thumbnail;
    
    private Integer isTop;
    
    private Integer isRecommend;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
    
}
