package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.entity.ArticleVersion;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.ArticleVersionMapper;
import com.knowledge.base.service.ArticleVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章版本服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleVersionServiceImpl extends ServiceImpl<ArticleVersionMapper, ArticleVersion> implements ArticleVersionService {
    
    private final ArticleVersionMapper articleVersionMapper;
    
    @Override
    public List<ArticleVersion> selectByArticleId(Long articleId) {
        return articleVersionMapper.selectByArticleId(articleId);
    }
    
    @Override
    public Long createVersion(ArticleVersion version) {
        articleVersionMapper.insert(version);
        return version.getId();
    }
    
    @Override
    public ArticleVersion selectLatestVersion(Long articleId) {
        return articleVersionMapper.selectLatestVersion(articleId);
    }
    
    @Override
    public ArticleVersion rollbackToVersion(Long articleId, Integer targetVersion) {
        ArticleVersion version = articleVersionMapper.selectByArticleId(articleId).stream()
                .filter(v -> v.getVersion().equals(targetVersion))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResultCode.ERROR, "版本不存在"));
        return version;
    }
    
}
