package com.knowledge.base.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 */
@Data
@TableName("tb_user_roles")
public class UserRole {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long roleId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
}
