package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限实体类
 */
@Data
@TableName("tb_permissions")
public class Permission {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String permissionName;
    
    private String permissionCode;
    
    private String type;
    
    private Long parentId;
    
    private String path;
    
    private String icon;
    
    private Integer orderNum;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private List<Permission> children;
    
}
