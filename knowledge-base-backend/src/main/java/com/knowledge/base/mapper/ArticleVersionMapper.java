package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.ArticleVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章版本Mapper
 */
@Mapper
public interface ArticleVersionMapper extends BaseMapper<ArticleVersion> {
    
    /**
     * 根据文章ID查询版本历史
     */
    @Select("SELECT * FROM tb_article_versions WHERE article_id = #{articleId} ORDER BY version DESC")
    List<ArticleVersion> selectByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 查询最新版本
     */
    @Select("SELECT * FROM tb_article_versions WHERE article_id = #{articleId} ORDER BY version DESC LIMIT 1")
    ArticleVersion selectLatestVersion(@Param("articleId") Long articleId);
    
}
