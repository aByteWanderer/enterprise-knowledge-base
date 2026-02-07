package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.ArticleAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章审核Mapper
 */
@Mapper
public interface ArticleAuditMapper extends BaseMapper<ArticleAudit> {
    
    /**
     * 根据文章ID查询审核记录
     */
    @Select("SELECT * FROM tb_article_audits WHERE article_id = #{articleId} ORDER BY created_at DESC")
    List<ArticleAudit> selectByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 查询审核人的审核记录
     */
    @Select("SELECT * FROM tb_article_audits WHERE auditor_id = #{auditorId} ORDER BY created_at DESC")
    List<ArticleAudit> selectByAuditorId(@Param("auditorId") Long auditorId);
    
}
