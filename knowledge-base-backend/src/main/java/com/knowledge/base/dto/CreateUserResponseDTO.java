package com.knowledge.base.dto;

import lombok.Data;

/**
 * 创建用户响应DTO
 */
@Data
public class CreateUserResponseDTO {
    
    private Long userId;
    
    private String initialPassword;
    
    public CreateUserResponseDTO(Long userId, String initialPassword) {
        this.userId = userId;
        this.initialPassword = initialPassword;
    }
}
