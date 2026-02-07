package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章标签关联实体类
 */
@Data
@TableName("tb_article_tags")
public class ArticleTag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long articleId;
    
    private Long tagId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
}
