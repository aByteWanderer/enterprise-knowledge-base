package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Article;
import com.knowledge.base.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章Mapper
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    
    /**
     * 根据ID查询文章（带作者和分类信息）
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.id = #{id} AND a.deleted = 0")
    ArticleVO selectArticleById(@Param("id") Long id);
    
    /**
     * 查询文章列表（带分页和条件）
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.deleted = 0 " +
            "ORDER BY a.is_top DESC, a.created_at DESC")
    List<ArticleVO> selectArticleList();
    
    /**
     * 根据分类ID查询文章
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.category_id = #{categoryId} AND a.deleted = 0 " +
            "ORDER BY a.is_top DESC, a.created_at DESC")
    List<ArticleVO> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 搜索文章
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.deleted = 0 " +
            "AND (a.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.content LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.summary LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY a.view_count DESC, a.created_at DESC")
    List<ArticleVO> searchArticles(@Param("keyword") String keyword);
    
    /**
     * 查询待审核文章
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.status = 'PENDING' AND a.deleted = 0 " +
            "ORDER BY a.created_at ASC")
    List<ArticleVO> selectPendingArticles();
    
    /**
     * 查询推荐文章
     */
    @Select("SELECT a.*, u.username as author_name, c.category_name " +
            "FROM tb_articles a " +
            "LEFT JOIN tb_users u ON a.author_id = u.id " +
            "LEFT JOIN tb_categories c ON a.category_id = c.id " +
            "WHERE a.is_recommend = 1 AND a.status = 'PUBLISHED' AND a.deleted = 0 " +
            "ORDER BY a.view_count DESC " +
            "LIMIT #{limit}")
    List<ArticleVO> selectRecommendArticles(@Param("limit") Integer limit);
    
}
