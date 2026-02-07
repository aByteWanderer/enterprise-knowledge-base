package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章审核实体类
 */
@Data
@TableName("tb_article_audits")
public class ArticleAudit {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long articleId;
    
    private String status;
    
    private String remark;
    
    private Long auditorId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
}
