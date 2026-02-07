package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    
    /**
     * 查询所有标签
     */
    @Select("SELECT * FROM tb_tags WHERE deleted = 0 ORDER BY article_count DESC, created_at DESC")
    List<Tag> selectAllTags();
    
    /**
     * 根据文章ID查询标签
     */
    @Select("SELECT t.* FROM tb_tags t " +
            "INNER JOIN tb_article_tags at ON t.id = at.tag_id " +
            "WHERE at.article_id = #{articleId}")
    List<Tag> selectTagsByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 查询热门标签
     */
    @Select("SELECT * FROM tb_tags WHERE deleted = 0 ORDER BY article_count DESC LIMIT #{limit}")
    List<Tag> selectHotTags(@Param("limit") Integer limit);
    
}
