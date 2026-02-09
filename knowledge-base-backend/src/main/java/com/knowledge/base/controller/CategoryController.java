package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.dto.CategoryDTO;
import com.knowledge.base.entity.Category;
import com.knowledge.base.service.CategoryService;
import com.knowledge.base.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@Tag(name = "分类管理")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @Operation(summary = "查询分类树")
    @GetMapping("/tree")
    public Result<List<CategoryVO>> getCategoryTree() {
        List<CategoryVO> tree = categoryService.selectCategoryTree();
        return Result.success(tree);
    }
    
    @Operation(summary = "查询一级分类")
    @GetMapping("/root")
    public Result<List<Category>> getRootCategories() {
        List<Category> categories = categoryService.selectRootCategories();
        return Result.success(categories);
    }
    
    @Operation(summary = "查询所有分类")
    @GetMapping
    
    public Result<List<Category>> listCategories() {
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }
    
    @Operation(summary = "查询分类详情")
    @GetMapping("/{id}")
    
    public Result<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.success(category);
    }
    
    @Operation(summary = "创建分类")
    @PostMapping
    
    public Result<Long> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Long categoryId = categoryService.createCategory(categoryDTO);
        return Result.success(categoryId);
    }
    
    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    
    public Result<Void> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
    
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
    
    @Operation(summary = "批量删除分类")
    @DeleteMapping("/batch")
    
    public Result<Void> deleteCategories(@RequestBody List<Long> ids) {
        categoryService.deleteCategories(ids);
        return Result.success();
    }
    
    @Operation(summary = "更新状态")
    @PutMapping("/{id}/status")
    
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return Result.success();
    }
    
}
