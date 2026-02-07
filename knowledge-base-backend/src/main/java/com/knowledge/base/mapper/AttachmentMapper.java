package com.knowledge.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knowledge.base.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 附件Mapper
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
    
    /**
     * 根据文章ID查询附件
     */
    @Select("SELECT * FROM tb_attachments WHERE article_id = #{articleId} AND deleted = 0 ORDER BY created_at DESC")
    List<Attachment> selectByArticleId(@Param("articleId") Long articleId);
    
    /**
     * 根据文件MD5查询附件
     */
    @Select("SELECT * FROM tb_attachments WHERE file_md5 = #{fileMd5} AND deleted = 0 LIMIT 1")
    Attachment selectByFileMd5(@Param("fileMd5") String fileMd5);
    
}
