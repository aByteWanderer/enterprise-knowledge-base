package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.CategoryDTO;
import com.knowledge.base.entity.Category;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.CategoryMapper;
import com.knowledge.base.service.CategoryService;
import com.knowledge.base.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分类服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    private final CategoryMapper categoryMapper;
    
    @Override
    public List<CategoryVO> selectCategoryTree() {
        List<Category> categories = categoryMapper.selectAllCategories();
        
        // 转换为 VO
        List<CategoryVO> categoryVOS = categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 构建树形结构
        return buildCategoryTree(categoryVOS, 0L);
    }
    
    @Override
    public List<Category> selectRootCategories() {
        return categoryMapper.selectRootCategories();
    }
    
    @Override
    public List<Category> selectByParentId(Long parentId) {
        return categoryMapper.selectByParentId(parentId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        
        // 设置父分类路径
        if (categoryDTO.getParentId() == null || categoryDTO.getParentId() == 0) {
            category.setParentId(0L);
            category.setLevel(1);
            category.setPath("/" + categoryDTO.getCategoryCode());
        } else {
            Category parent = categoryMapper.selectById(categoryDTO.getParentId());
            if (parent == null) {
                throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND, "父分类不存在");
            }
            category.setLevel(parent.getLevel() + 1);
            category.setPath(parent.getPath() + "/" + categoryDTO.getCategoryCode());
        }
        
        category.setStatus(categoryDTO.getStatus() != null ? categoryDTO.getStatus() : 1);
        categoryMapper.insert(category);
        
        return category.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.selectById(categoryDTO.getId());
        if (category == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }
        
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateById(category);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }
        
        // 检查是否有子分类
        List<Category> children = categoryMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new BusinessException(ResultCode.ERROR, "请先删除子分类");
        }
        
        category.setDeleted(1);
        categoryMapper.updateById(category);
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.CATEGORY_NOT_FOUND);
        }
        
        category.setStatus(status);
        category.setUpdatedAt(LocalDateTime.now());
        categoryMapper.updateById(category);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategories(List<Long> ids) {
        for (Long id : ids) {
            deleteCategory(id);
        }
    }
    
    private CategoryVO convertToVO(Category category) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
    
    private List<CategoryVO> buildCategoryTree(List<CategoryVO> categories, Long parentId) {
        return categories.stream()
                .filter(c -> c.getParentId().equals(parentId))
                .peek(c -> {
                    List<CategoryVO> children = buildCategoryTree(categories, c.getId());
                    if (!children.isEmpty()) {
                        c.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }
    
}
