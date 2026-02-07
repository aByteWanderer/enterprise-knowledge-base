package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.entity.Attachment;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.AttachmentMapper;
import com.knowledge.base.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

/**
 * 附件服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements AttachmentService {
    
    private final AttachmentMapper attachmentMapper;
    
    @Value("${app.upload.path:./uploads}")
    private String uploadPath;
    
    @Value("${app.upload.image-path:./uploads/images}")
    private String imagePath;
    
    @Value("${app.upload.attachment-path:./uploads/attachments}")
    private String attachmentPath;
    
    @Value("${app.upload.thumbnail-width:200}")
    private int thumbnailWidth;
    
    @Value("${app.upload.thumbnail-height:200}")
    private int thumbnailHeight;
    
    @Value("${app.upload.max-image-size:10485760}")
    private long maxImageSize;
    
    @Value("${app.upload.allowed-image-types:jpg,jpeg,png,gif,bmp,webp}")
    private String allowedImageTypes;
    
    @Override
    public List<Attachment> selectByArticleId(Long articleId) {
        return attachmentMapper.selectByArticleId(articleId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attachment uploadAttachment(MultipartFile file, Long articleId) {
        try {
            // 检查文件是否已存在（通过MD5）
            String fileMd5 = calculateFileMd5(file);
            Attachment existAttachment = attachmentMapper.selectByFileMd5(fileMd5);
            if (existAttachment != null) {
                return existAttachment;
            }
            
            // 保存原文件
            String originalName = file.getOriginalFilename();
            String fileName = generateFileName(originalName);
            String relativePath = saveFile(file, attachmentPath, fileName);
            
            // 创建附件记录
            Attachment attachment = new Attachment();
            attachment.setOriginalName(originalName);
            attachment.setFileName(fileName);
            attachment.setFilePath(relativePath);
            attachment.setFileSize(file.getSize());
            attachment.setFileType(getFileExtension(originalName));
            attachment.setFileMd5(fileMd5);
            attachment.setArticleId(articleId);
            
            attachmentMapper.insert(attachment);
            
            return attachment;
        } catch (IOException e) {
            log.error("上传附件失败", e);
            throw new BusinessException(ResultCode.UPLOAD_ERROR, "上传附件失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadImage(MultipartFile file) {
        try {
            // 检查文件大小
            if (file.getSize() > maxImageSize) {
                throw new BusinessException(ResultCode.FILE_SIZE_EXCEEDED, "图片大小不能超过10MB");
            }
            
            // 检查文件类型
            String originalName = file.getOriginalFilename();
            String extension = getFileExtension(originalName);
            if (!isAllowedImageType(extension)) {
                throw new BusinessException(ResultCode.FILE_TYPE_NOT_ALLOWED, "不支持的图片格式");
            }
            
            // 保存原图
            String fileName = generateFileName(originalName);
            String relativePath = saveFile(file, imagePath, fileName);
            
            // 生成缩略图
            String thumbnailFileName = "thumb_" + fileName;
            String thumbnailPath = imagePath + "/thumb_" + fileName;
            generateThumbnail(imagePath + "/" + fileName, thumbnailPath);
            
            // 更新附件记录
            Attachment attachment = new Attachment();
            attachment.setOriginalName(originalName);
            attachment.setFileName(fileName);
            attachment.setFilePath(relativePath);
            attachment.setThumbnailPath("thumb_" + fileName);
            attachment.setFileSize(file.getSize());
            attachment.setFileType(extension);
            attachmentMapper.insert(attachment);
            
            return relativePath;
        } catch (IOException e) {
            log.error("上传图片失败", e);
            throw new BusinessException(ResultCode.UPLOAD_ERROR, "上传图片失败: " + e.getMessage());
        }
    }
    
    @Override
    public Attachment downloadAttachment(Long id) {
        Attachment attachment = attachmentMapper.selectById(id);
        if (attachment == null) {
            throw new BusinessException(ResultCode.ERROR, "附件不存在");
        }
        return attachment;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentMapper.selectById(id);
        if (attachment == null) {
            throw new BusinessException(ResultCode.ERROR, "附件不存在");
        }
        
        // 删除文件
        deleteFile(attachmentPath + "/" + attachment.getFilePath());
        if (attachment.getThumbnailPath() != null) {
            deleteFile(imagePath + "/" + attachment.getThumbnailPath());
        }
        
        attachment.setDeleted(1);
        attachmentMapper.updateById(attachment);
    }
    
    @Override
    public Attachment selectByFileMd5(String fileMd5) {
        return attachmentMapper.selectByFileMd5(fileMd5);
    }
    
    private String saveFile(MultipartFile file, String basePath, String fileName) throws IOException {
        // 创建日期目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullPath = basePath + "/" + datePath;
        Path directory = Paths.get(fullPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        
        // 保存文件
        Path filePath = directory.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return datePath + "/" + fileName;
    }
    
    private void generateThumbnail(String sourcePath, String targetPath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            return;
        }
        
        Thumbnails.of(sourceFile)
                .size(thumbnailWidth, thumbnailHeight)
                .toFile(targetPath);
    }
    
    private String generateFileName(String originalName) {
        String extension = getFileExtension(originalName);
        return UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    
    private String calculateFileMd5(MultipartFile file) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(file.getBytes());
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
    
    private boolean isAllowedImageType(String extension) {
        String[] types = allowedImageTypes.split(",");
        for (String type : types) {
            if (type.trim().equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    private void deleteFile(String path) {
        try {
            Path filePath = Paths.get(path);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("删除文件失败: {}", path);
        }
    }
    
}
