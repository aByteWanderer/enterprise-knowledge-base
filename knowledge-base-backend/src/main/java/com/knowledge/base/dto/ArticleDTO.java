package com.knowledge.base.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章DTO
 */
@Data
public class ArticleDTO {
    
    private Long id;
    
    @NotBlank(message = "文章标题不能为空")
    private String title;
    
    @NotBlank(message = "文章内容不能为空")
    private String content;
    
    private String summary;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    private String thumbnail;
    
    private Integer isTop;
    
    private Integer isRecommend;

    private String status;
    
    private Long[] tagIds;
    
}
