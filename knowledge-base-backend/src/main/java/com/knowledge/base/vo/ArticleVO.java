package com.knowledge.base.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章VO
 */
@Data
public class ArticleVO {
    
    private Long id;
    
    private String title;
    
    private String content;
    
    private String summary;
    
    private Long categoryId;
    
    private String categoryName;
    
    private Long authorId;
    
    private String authorName;
    
    private String status;
    
    private Integer version;
    
    private Long viewCount;
    
    private String thumbnail;
    
    private Integer isTop;
    
    private Integer isRecommend;
    
    private List<TagVO> tags;
    
    private List<AttachmentVO> attachments;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
}
