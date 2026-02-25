package com.knowledge.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.knowledge.base.common.Result;
import com.knowledge.base.dto.ArticleDTO;
import com.knowledge.base.entity.User;
import com.knowledge.base.service.ArticleService;
import com.knowledge.base.vo.ArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文章控制器
 */
@Tag(name = "文章管理")
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    
    private final ArticleService articleService;
    
    @Operation(summary = "查询文章详情")
    @GetMapping("/{id}")
    public Result<ArticleVO> getArticle(@PathVariable Long id) {
        ArticleVO article = articleService.selectArticleById(id);
        return Result.success(article);
    }
    
    @Operation(summary = "分页查询文章")
    @GetMapping
    public Result<PageInfo<ArticleVO>> listArticles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            ArticleDTO articleDTO) {
        PageInfo<ArticleVO> pageInfo = articleService.selectArticlePage(pageNum, pageSize, articleDTO);
        return Result.success(pageInfo);
    }
    
    @Operation(summary = "搜索文章")
    @GetMapping("/search")
    public Result<PageInfo<ArticleVO>> searchArticles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String keyword) {
        PageInfo<ArticleVO> pageInfo = articleService.searchArticlesPage(pageNum, pageSize, keyword);
        return Result.success(pageInfo);
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getId();
        }
        return 1L;
    }
    
    @Operation(summary = "创建文章")
    @PostMapping
    public Result<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Long articleId = articleService.createArticle(articleDTO, getCurrentUserId());
        return Result.success(articleId);
    }
    
    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    public Result<Void> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO) {
        articleDTO.setId(id);
        articleService.updateArticle(articleDTO, getCurrentUserId());
        return Result.success();
    }
    
    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }
    
    @Operation(summary = "批量删除文章")
    @DeleteMapping("/batch")
    
    public Result<Void> deleteArticles(@RequestBody List<Long> ids) {
        articleService.deleteArticles(ids);
        return Result.success();
    }
    
    @Operation(summary = "提交审核")
    @PostMapping("/{id}/submit")
    public Result<Void> submitForReview(@PathVariable Long id) {
        articleService.submitForReview(id, getCurrentUserId());
        return Result.success();
    }
    
    @Operation(summary = "审核文章")
    @PostMapping("/{id}/audit")
    
    public Result<Void> auditArticle(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String remark) {
        articleService.auditArticle(id, status, remark, getCurrentUserId());
        return Result.success();
    }
    
    @Operation(summary = "查询待审核文章")
    @GetMapping("/pending")
    
    public Result<List<ArticleVO>> getPendingArticles() {
        List<ArticleVO> articles = articleService.selectPendingArticles();
        return Result.success(articles);
    }
    
    @Operation(summary = "查询推荐文章")
    @GetMapping("/recommend")
    public Result<List<ArticleVO>> getRecommendArticles(@RequestParam(defaultValue = "10") Integer limit) {
        List<ArticleVO> articles = articleService.selectRecommendArticles(limit);
        return Result.success(articles);
    }
    
    @Operation(summary = "发布文章")
    @PostMapping("/{id}/publish")
    
    public Result<Void> publishArticle(@PathVariable Long id) {
        articleService.publishArticle(id);
        return Result.success();
    }
    
    @Operation(summary = "归档文章")
    @PostMapping("/{id}/archive")
    public Result<Void> archiveArticle(@PathVariable Long id) {
        articleService.archiveArticle(id);
        return Result.success();
    }
    
    @Operation(summary = "上传文章图片")
    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imagePath = articleService.uploadImage(file);
        return Result.success(imagePath);
    }
    
    @Operation(summary = "上传文章附件")
    @PostMapping("/{id}/upload/attachment")
    public Result<Long> uploadAttachment(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Long attachmentId = articleService.uploadAttachment(file, id);
        return Result.success(attachmentId);
    }
    
}
