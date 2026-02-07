package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.TagDTO;
import com.knowledge.base.entity.Tag;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService extends IService<Tag> {
    
    /**
     * 查询所有标签
     */
    List<Tag> selectAllTags();
    
    /**
     * 查询热门标签
     */
    List<Tag> selectHotTags(Integer limit);
    
    /**
     * 根据文章ID查询标签
     */
    List<Tag> selectTagsByArticleId(Long articleId);
    
    /**
     * 创建标签
     */
    Long createTag(TagDTO tagDTO);
    
    /**
     * 更新标签
     */
    void updateTag(TagDTO tagDTO);
    
    /**
     * 删除标签
     */
    void deleteTag(Long id);
    
    /**
     * 批量删除标签
     */
    void deleteTags(List<Long> ids);
    
    /**
     * 为文章添加标签
     */
    void addTagsToArticle(Long articleId, Long[] tagIds);
    
    /**
     * 为文章移除标签
     */
    void removeTagsFromArticle(Long articleId, Long[] tagIds);
    
    /**
     * 更新标签文章数
     */
    void updateArticleCount(Long tagId);
    
}
