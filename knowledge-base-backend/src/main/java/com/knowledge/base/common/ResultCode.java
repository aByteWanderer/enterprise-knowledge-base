package com.knowledge.base.common;

/**
 * 响应状态码枚举
 */
public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 参数错误 400-499
    PARAM_ERROR(400, "参数错误"),
    PARAM_IS_NULL(401, "参数为空"),
    PARAM_TYPE_ERROR(402, "参数类型错误"),
    PARAM_VALID_ERROR(403, "参数校验失败"),
    
    // 认证错误 1000-1099
    UNAUTHORIZED(1000, "未登录或token已过期"),
    TOKEN_INVALID(1001, "token无效"),
    TOKEN_EXPIRED(1002, "token已过期"),
    LOGIN_ERROR(1003, "用户名或密码错误"),
    ACCOUNT_DISABLED(1004, "账户已被禁用"),
    ACCOUNT_LOCKED(1005, "账户已被锁定"),
    
    // 权限错误 1100-1199
    FORBIDDEN(1100, "无权限访问"),
    ACCESS_DENIED(1101, "访问被拒绝"),
    
    // 业务错误 2000-2999
    USER_NOT_FOUND(2000, "用户不存在"),
    USER_EXISTS(2001, "用户已存在"),
    ROLE_NOT_FOUND(2002, "角色不存在"),
    ROLE_EXISTS(2003, "角色已存在"),
    PERMISSION_NOT_FOUND(2004, "权限不存在"),
    
    ARTICLE_NOT_FOUND(2100, "文章不存在"),
    CATEGORY_NOT_FOUND(2101, "分类不存在"),
    TAG_NOT_FOUND(2102, "标签不存在"),
    
    UPLOAD_ERROR(2200, "上传失败"),
    FILE_TYPE_NOT_ALLOWED(2201, "文件类型不允许"),
    FILE_SIZE_EXCEEDED(2202, "文件大小超过限制"),
    
    // 服务器错误 5000-5999
    SERVER_ERROR(5000, "服务器内部错误"),
    SERVICE_UNAVAILABLE(5001, "服务不可用");
    
    private final Integer code;
    
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
}
