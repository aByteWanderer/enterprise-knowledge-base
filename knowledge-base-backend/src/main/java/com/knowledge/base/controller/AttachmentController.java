package com.knowledge.base.controller;

import com.knowledge.base.common.Result;
import com.knowledge.base.entity.Attachment;
import com.knowledge.base.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 附件控制器
 */
@Tag(name = "附件管理")
@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController {
    
    private final AttachmentService attachmentService;
    
    @Operation(summary = "根据文章ID查询附件")
    @GetMapping("/article/{articleId}")
    public Result<List<Attachment>> getAttachmentsByArticleId(@PathVariable Long articleId) {
        List<Attachment> attachments = attachmentService.selectByArticleId(articleId);
        return Result.success(attachments);
    }
    
    @Operation(summary = "下载附件")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        Attachment attachment = attachmentService.downloadAttachment(id);
        
        File file = new File(attachment.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + attachment.getOriginalName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(attachment.getFileSize())
                .body(resource);
    }
    
    @Operation(summary = "删除附件")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return Result.success();
    }
    
}
