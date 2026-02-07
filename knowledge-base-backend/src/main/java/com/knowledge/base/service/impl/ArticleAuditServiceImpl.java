package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.dto.ArticleAuditDTO;
import com.knowledge.base.entity.ArticleAudit;
import com.knowledge.base.mapper.ArticleAuditMapper;
import com.knowledge.base.service.ArticleAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章审核服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleAuditServiceImpl extends ServiceImpl<ArticleAuditMapper, ArticleAudit> implements ArticleAuditService {
    
    private final ArticleAuditMapper articleAuditMapper;
    
    @Override
    public List<ArticleAudit> selectByArticleId(Long articleId) {
        return articleAuditMapper.selectByArticleId(articleId);
    }
    
    @Override
    public Long createAudit(ArticleAuditDTO auditDTO, Long articleId, Long auditorId) {
        ArticleAudit audit = new ArticleAudit();
        audit.setArticleId(articleId);
        audit.setStatus(auditDTO.getStatus());
        audit.setRemark(auditDTO.getRemark());
        audit.setAuditorId(auditorId);
        articleAuditMapper.insert(audit);
        return audit.getId();
    }
    
    @Override
    public List<ArticleAudit> selectByAuditorId(Long auditorId) {
        return articleAuditMapper.selectByAuditorId(auditorId);
    }
    
    @Override
    public Long countPendingAudits() {
        return articleAuditMapper.selectCount(null);
    }
    
}
