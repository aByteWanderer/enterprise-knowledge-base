package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 分类Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    /**
     * 查询所有分类（树形结构）
     */
    @Select("SELECT * FROM tb_categories WHERE deleted = 0 ORDER BY order_num")
    List<Category> selectAllCategories();
    
    /**
     * 根据父ID查询子分类
     */
    @Select("SELECT * FROM tb_categories WHERE parent_id = #{parentId} AND deleted = 0 ORDER BY order_num")
    List<Category> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询一级分类
     */
    @Select("SELECT * FROM tb_categories WHERE parent_id = 0 AND deleted = 0 ORDER BY order_num")
    List<Category> selectRootCategories();
    
    /**
     * 查询分类路径
     */
    @Select("SELECT path FROM tb_categories WHERE id = #{id}")
    String selectPathById(@Param("id") Long id);
    
}
