package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.ArticleAuditDTO;
import com.knowledge.base.entity.ArticleAudit;

import java.util.List;

/**
 * 文章审核服务接口
 */
public interface ArticleAuditService extends IService<ArticleAudit> {
    
    /**
     * 根据文章ID查询审核记录
     */
    List<ArticleAudit> selectByArticleId(Long articleId);
    
    /**
     * 创建审核记录
     */
    Long createAudit(ArticleAuditDTO auditDTO, Long articleId, Long auditorId);
    
    /**
     * 查询审核人的审核记录
     */
    List<ArticleAudit> selectByAuditorId(Long auditorId);
    
    /**
     * 查询待审核数量
     */
    Long countPendingAudits();
    
}
