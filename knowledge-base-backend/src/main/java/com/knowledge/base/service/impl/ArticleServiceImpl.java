package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.ArticleAuditDTO;
import com.knowledge.base.dto.ArticleDTO;
import com.knowledge.base.entity.*;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.ArticleMapper;
import com.knowledge.base.mapper.ArticleTagMapper;
import com.knowledge.base.mapper.TagMapper;
import com.knowledge.base.service.ArticleAuditService;
import com.knowledge.base.service.ArticleService;
import com.knowledge.base.service.ArticleVersionService;
import com.knowledge.base.service.AttachmentService;
import com.knowledge.base.vo.ArticleVO;
import com.knowledge.base.vo.TagVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    
    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;
    private final ArticleVersionService articleVersionService;
    private final ArticleAuditService articleAuditService;
    private final AttachmentService attachmentService;
    
    @Override
    public ArticleVO selectArticleById(Long id) {
        ArticleVO article = articleMapper.selectArticleById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        // 增加浏览次数
        incrementViewCount(id);
        
        // 获取标签
        List<Tag> tags = tagMapper.selectTagsByArticleId(id);
        article.setTags(tags.stream()
                .map(this::convertToTagVO)
                .collect(Collectors.toList()));
        
        return article;
    }
    
    @Override
    public List<ArticleVO> selectArticleList(ArticleDTO articleDTO) {
        List<ArticleVO> articles = articleMapper.selectArticleList();
        
        // 为每个文章关联标签
        for (ArticleVO article : articles) {
            List<Tag> tags = tagMapper.selectTagsByArticleId(article.getId());
            article.setTags(tags.stream()
                    .map(this::convertToTagVO)
                    .collect(Collectors.toList()));
        }
        
        // 如果指定了分类ID，过滤结果
        if (articleDTO.getCategoryId() != null) {
            articles = articles.stream()
                    .filter(a -> a.getCategoryId() != null && a.getCategoryId().equals(articleDTO.getCategoryId()))
                    .collect(Collectors.toList());
        }
        
        // 如果指定了状态，过滤结果
        if (articleDTO.getStatus() != null && !articleDTO.getStatus().isEmpty()) {
            articles = articles.stream()
                    .filter(a -> a.getStatus() != null && a.getStatus().equals(articleDTO.getStatus()))
                    .collect(Collectors.toList());
        }
        
        // 如果指定了标题关键字，过滤结果
        if (articleDTO.getTitle() != null && !articleDTO.getTitle().isEmpty()) {
            articles = articles.stream()
                    .filter(a -> a.getTitle() != null && a.getTitle().contains(articleDTO.getTitle()))
                    .collect(Collectors.toList());
        }
        
        return articles;
    }
    
    @Override
    public PageInfo<ArticleVO> selectArticlePage(Integer pageNum, Integer pageSize, ArticleDTO articleDTO) {
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleVO> articles = selectArticleList(articleDTO);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articles);
        return pageInfo;
    }
    
    @Override
    public List<ArticleVO> searchArticles(String keyword) {
        return articleMapper.searchArticles(keyword);
    }
    
    @Override
    public PageInfo<ArticleVO> searchArticlesPage(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleVO> articles = articleMapper.searchArticles(keyword);
        PageInfo<ArticleVO> pageInfo = new PageInfo<>(articles);
        return pageInfo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(ArticleDTO articleDTO, Long authorId) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        article.setAuthorId(authorId);
        article.setStatus("DRAFT");
        article.setVersion(1);
        article.setViewCount(0L);
        
        articleMapper.insert(article);
        
        // 保存标签
        if (articleDTO.getTagIds() != null && articleDTO.getTagIds().length > 0) {
            saveArticleTags(article.getId(), articleDTO.getTagIds());
        }
        
        return article.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO articleDTO, Long operatorId) {
        Article article = articleMapper.selectById(articleDTO.getId());
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        // 保存版本历史
        ArticleVersion version = new ArticleVersion();
        version.setArticleId(article.getId());
        version.setTitle(article.getTitle());
        version.setContent(article.getContent());
        version.setVersion(article.getVersion());
        version.setCreatedBy(operatorId);
        articleVersionService.createVersion(version);
        
        // 更新文章
        BeanUtils.copyProperties(articleDTO, article);
        article.setVersion(article.getVersion() + 1);
        
        articleMapper.updateById(article);
        
        // 更新标签
        if (articleDTO.getTagIds() != null) {
            articleTagMapper.deleteByArticleId(article.getId());
            saveArticleTags(article.getId(), articleDTO.getTagIds());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        article.setDeleted(1);
        articleMapper.updateById(article);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(List<Long> ids) {
        for (Long id : ids) {
            deleteArticle(id);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForReview(Long id, Long operatorId) {
        log.info("submitForReview called for article id: {}", id);
        
        Article article = articleMapper.selectById(id);
        if (article == null) {
            log.warn("Article not found with id: {}", id);
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        log.info("Article status before submit: {}", article.getStatus());
        
        if (!"DRAFT".equals(article.getStatus())) {
            log.warn("Article status is not DRAFT, current status: {}", article.getStatus());
            throw new BusinessException(ResultCode.ERROR, "只有草稿状态的文章可以提交审核");
        }
        
        article.setStatus("PENDING");
        article.setUpdatedAt(LocalDateTime.now());
        
        int rows = articleMapper.updateById(article);
        log.info("Update rows affected: {}", rows);
        
        // Verify the update
        Article updated = articleMapper.selectById(id);
        log.info("Article status after submit: {}", updated.getStatus());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditArticle(Long id, String status, String remark, Long auditorId) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        if (!"PENDING".equals(article.getStatus())) {
            throw new BusinessException(ResultCode.ERROR, "只有待审核状态的文章可以审核");
        }
        
        // 更新文章状态
        article.setStatus(status);
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
        
        // 保存审核记录
        ArticleAuditDTO auditDTO = new ArticleAuditDTO();
        auditDTO.setStatus(status);
        auditDTO.setRemark(remark);
        articleAuditService.createAudit(auditDTO, id, auditorId);
    }
    
    @Override
    public List<ArticleVO> selectPendingArticles() {
        List<ArticleVO> articles = articleMapper.selectPendingArticles();
        log.info("selectPendingArticles found {} articles", articles.size());
        for (ArticleVO article : articles) {
            log.info("Pending article: id={}, title={}, status={}", 
                    article.getId(), article.getTitle(), article.getStatus());
        }
        return articles;
    }
    
    @Override
    public List<ArticleVO> selectRecommendArticles(Integer limit) {
        return articleMapper.selectRecommendArticles(limit);
    }
    
    @Override
    public void incrementViewCount(Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            articleMapper.updateById(article);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        if (!"APPROVED".equals(article.getStatus())) {
            throw new BusinessException(ResultCode.ERROR, "只有已审核通过的文章可以发布");
        }
        
        article.setStatus("PUBLISHED");
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void archiveArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.ARTICLE_NOT_FOUND);
        }
        
        article.setStatus("ARCHIVED");
        article.setUpdatedAt(LocalDateTime.now());
        articleMapper.updateById(article);
    }
    
    @Override
    public String uploadImage(MultipartFile file) {
        return attachmentService.uploadImage(file);
    }
    
    @Override
    public Long uploadAttachment(MultipartFile file, Long articleId) {
        Attachment attachment = attachmentService.uploadAttachment(file, articleId);
        return attachment.getId();
    }
    
    private void saveArticleTags(Long articleId, Long[] tagIds) {
        LocalDateTime now = LocalDateTime.now();
        for (Long tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTag.setCreatedAt(now);
            articleTagMapper.insert(articleTag);
            
            // 更新标签文章数
            Tag tag = tagMapper.selectById(tagId);
            if (tag != null) {
                tag.setArticleCount(tag.getArticleCount() + 1);
                tagMapper.updateById(tag);
            }
        }
    }
    
    private TagVO convertToTagVO(Tag tag) {
        TagVO vo = new TagVO();
        BeanUtils.copyProperties(tag, vo);
        return vo;
    }
    
}
