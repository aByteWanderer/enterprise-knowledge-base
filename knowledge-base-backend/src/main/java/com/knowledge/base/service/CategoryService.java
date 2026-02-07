package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.CategoryDTO;
import com.knowledge.base.entity.Category;
import com.knowledge.base.vo.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 查询所有分类（树形结构）
     */
    List<CategoryVO> selectCategoryTree();
    
    /**
     * 查询一级分类
     */
    List<Category> selectRootCategories();
    
    /**
     * 根据父ID查询子分类
     */
    List<Category> selectByParentId(Long parentId);
    
    /**
     * 创建分类
     */
    Long createCategory(CategoryDTO categoryDTO);
    
    /**
     * 更新分类
     */
    void updateCategory(CategoryDTO categoryDTO);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 更新分类状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 批量删除分类
     */
    void deleteCategories(List<Long> ids);
    
}
