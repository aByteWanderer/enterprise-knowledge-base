package com.knowledge.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 文章审核DTO
 */
@Data
public class ArticleAuditDTO {
    
    private Long id;
    
    @NotBlank(message = "审核状态不能为空")
    private String status;
    
    private String remark;
    
}
