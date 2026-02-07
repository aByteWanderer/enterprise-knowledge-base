package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.ArticleDTO;
import com.knowledge.base.entity.Article;
import com.knowledge.base.vo.ArticleVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {
    
    /**
     * 查询文章详情
     */
    ArticleVO selectArticleById(Long id);
    
    /**
     * 查询文章列表
     */
    List<ArticleVO> selectArticleList(ArticleDTO articleDTO);
    
    /**
     * 分页查询文章
     */
    PageInfo<ArticleVO> selectArticlePage(Integer pageNum, Integer pageSize, ArticleDTO articleDTO);
    
    /**
     * 搜索文章
     */
    List<ArticleVO> searchArticles(String keyword);
    
    /**
     * 分页搜索文章
     */
    PageInfo<ArticleVO> searchArticlesPage(Integer pageNum, Integer pageSize, String keyword);
    
    /**
     * 创建文章
     */
    Long createArticle(ArticleDTO articleDTO, Long authorId);
    
    /**
     * 更新文章
     */
    void updateArticle(ArticleDTO articleDTO, Long operatorId);
    
    /**
     * 删除文章
     */
    void deleteArticle(Long id);
    
    /**
     * 批量删除文章
     */
    void deleteArticles(List<Long> ids);
    
    /**
     * 提交审核
     */
    void submitForReview(Long id, Long operatorId);
    
    /**
     * 审核文章
     */
    void auditArticle(Long id, String status, String remark, Long auditorId);
    
    /**
     * 查询待审核文章
     */
    List<ArticleVO> selectPendingArticles();
    
    /**
     * 查询推荐文章
     */
    List<ArticleVO> selectRecommendArticles(Integer limit);
    
    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);
    
    /**
     * 发布文章
     */
    void publishArticle(Long id);
    
    /**
     * 归档文章
     */
    void archiveArticle(Long id);
    
    /**
     * 上传文章图片
     */
    String uploadImage(MultipartFile file);
    
    /**
     * 上传文章附件
     */
    Long uploadAttachment(MultipartFile file, Long articleId);
    
}
