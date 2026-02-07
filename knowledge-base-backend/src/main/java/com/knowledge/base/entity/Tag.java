package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签实体类
 */
@Data
@TableName("tb_tags")
public class Tag {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String tagName;
    
    private String color;
    
    private Integer articleCount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
    
}
