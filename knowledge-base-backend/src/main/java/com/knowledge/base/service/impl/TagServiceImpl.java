package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.TagDTO;
import com.knowledge.base.entity.ArticleTag;
import com.knowledge.base.entity.Tag;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.ArticleTagMapper;
import com.knowledge.base.mapper.TagMapper;
import com.knowledge.base.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 标签服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    
    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;
    
    @Override
    public List<Tag> selectAllTags() {
        return tagMapper.selectAllTags();
    }
    
    @Override
    public List<Tag> selectHotTags(Integer limit) {
        return tagMapper.selectHotTags(limit);
    }
    
    @Override
    public List<Tag> selectTagsByArticleId(Long articleId) {
        return tagMapper.selectTagsByArticleId(articleId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDTO, tag);
        tag.setArticleCount(0);
        tagMapper.insert(tag);
        return tag.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(TagDTO tagDTO) {
        Tag tag = tagMapper.selectById(tagDTO.getId());
        if (tag == null) {
            throw new BusinessException(ResultCode.TAG_NOT_FOUND);
        }
        BeanUtils.copyProperties(tagDTO, tag);
        tagMapper.updateById(tag);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        Tag tag = tagMapper.selectById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.TAG_NOT_FOUND);
        }
        tag.setDeleted(1);
        tagMapper.updateById(tag);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTags(List<Long> ids) {
        for (Long id : ids) {
            deleteTag(id);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTagsToArticle(Long articleId, Long[] tagIds) {
        LocalDateTime now = LocalDateTime.now();
        for (Long tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTag.setCreatedAt(now);
            articleTagMapper.insert(articleTag);
            
            // 更新标签文章数
            updateArticleCount(tagId);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTagsFromArticle(Long articleId, Long[] tagIds) {
        for (Long tagId : tagIds) {
            // 删除关联
            articleTagMapper.deleteById(tagId);
            // 更新标签文章数
            updateArticleCount(tagId);
        }
    }
    
    @Override
    public void updateArticleCount(Long tagId) {
        Tag tag = tagMapper.selectById(tagId);
        if (tag != null) {
            // 统计文章数
            Long count = articleTagMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ArticleTag>()
                            .eq(ArticleTag::getTagId, tagId)
            );
            tag.setArticleCount(count.intValue());
            tagMapper.updateById(tag);
        }
    }
    
}
