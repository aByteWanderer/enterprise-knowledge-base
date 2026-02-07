package com.knowledge.base.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户DTO
 */
@Data
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    private String password;
    
    @NotBlank(message = "邮箱不能为空")
    private String email;
    
    private String phone;
    
    private String avatar;
    
    private Integer status;
    
    private Long[] roleIds;
    
}
