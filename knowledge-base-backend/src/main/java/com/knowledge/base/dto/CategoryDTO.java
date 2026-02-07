package com.knowledge.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 分类DTO
 */
@Data
public class CategoryDTO {
    
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;
    
    private String categoryCode;
    
    private Long parentId;
    
    private String description;
    
    private String icon;
    
    private Integer orderNum;
    
    private Integer status;
    
}
