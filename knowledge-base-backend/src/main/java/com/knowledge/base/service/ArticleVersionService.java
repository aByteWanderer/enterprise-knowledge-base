package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.entity.ArticleVersion;

import java.util.List;

/**
 * 文章版本服务接口
 */
public interface ArticleVersionService extends IService<ArticleVersion> {
    
    /**
     * 根据文章ID查询版本历史
     */
    List<ArticleVersion> selectByArticleId(Long articleId);
    
    /**
     * 创建版本记录
     */
    Long createVersion(ArticleVersion version);
    
    /**
     * 查询最新版本
     */
    ArticleVersion selectLatestVersion(Long articleId);
    
    /**
     * 回滚到指定版本
     */
    ArticleVersion rollbackToVersion(Long articleId, Integer targetVersion);
    
}
