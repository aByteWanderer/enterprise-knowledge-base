package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 附件服务接口
 */
public interface AttachmentService extends IService<Attachment> {
    
    /**
     * 根据文章ID查询附件
     */
    List<Attachment> selectByArticleId(Long articleId);
    
    /**
     * 上传附件
     */
    Attachment uploadAttachment(MultipartFile file, Long articleId);
    
    /**
     * 上传图片并生成缩略图
     */
    String uploadImage(MultipartFile file);
    
    /**
     * 下载附件
     */
    Attachment downloadAttachment(Long id);
    
    /**
     * 删除附件
     */
    void deleteAttachment(Long id);
    
    /**
     * 根据文件MD5查询附件
     */
    Attachment selectByFileMd5(String fileMd5);
    
}
