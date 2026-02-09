package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.dto.TagDTO;
import com.knowledge.base.entity.Tag;
import com.knowledge.base.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理")
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    
    private final TagService tagService;
    
    @Operation(summary = "查询所有标签")
    @GetMapping
    public Result<List<Tag>> listTags() {
        List<Tag> tags = tagService.selectAllTags();
        return Result.success(tags);
    }
    
    @Operation(summary = "查询热门标签")
    @GetMapping("/hot")
    public Result<List<Tag>> getHotTags(@RequestParam(defaultValue = "10") Integer limit) {
        List<Tag> tags = tagService.selectHotTags(limit);
        return Result.success(tags);
    }
    
    @Operation(summary = "查询标签详情")
    @GetMapping("/{id}")
    public Result<Tag> getTag(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        return Result.success(tag);
    }
    
    @Operation(summary = "创建标签")
    @PostMapping
    
    public Result<Long> createTag(@Valid @RequestBody TagDTO tagDTO) {
        Long tagId = tagService.createTag(tagDTO);
        return Result.success(tagId);
    }
    
    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    
    public Result<Void> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
        tagDTO.setId(id);
        tagService.updateTag(tagDTO);
        return Result.success();
    }
    
    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
    
    @Operation(summary = "批量删除标签")
    @DeleteMapping("/batch")
    
    public Result<Void> deleteTags(@RequestBody List<Long> ids) {
        tagService.deleteTags(ids);
        return Result.success();
    }
    
}
