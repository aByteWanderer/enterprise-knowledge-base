package com.knowledge.base.vo;

import lombok.Data;
import java.util.List;

/**
 * 分类VO（树形结构）
 */
@Data
public class CategoryVO {
    
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
    
    private List<CategoryVO> children;
    
}
