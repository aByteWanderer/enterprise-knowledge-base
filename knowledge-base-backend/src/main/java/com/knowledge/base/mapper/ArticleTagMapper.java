package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.ArticleTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章标签关联Mapper
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    
    /**
     * 删除文章的所有标签关联
     */
    @Delete("DELETE FROM tb_article_tags WHERE article_id = #{articleId}")
    int deleteByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 删除标签的所有文章关联
     */
    @Delete("DELETE FROM tb_article_tags WHERE tag_id = #{tagId}")
    int deleteByTagId(@Param("tagId") Long tagId);
    
}
