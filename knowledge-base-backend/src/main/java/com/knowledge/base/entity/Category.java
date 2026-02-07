package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类实体类
 */
@Data
@TableName("tb_categories")
public class Category {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String categoryName;
    
    private String categoryCode;
    
    private Long parentId;
    
    private Integer level;
    
    private String path;
    
    private String description;
    
    private String icon;
    
    private Integer orderNum;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
    
}
