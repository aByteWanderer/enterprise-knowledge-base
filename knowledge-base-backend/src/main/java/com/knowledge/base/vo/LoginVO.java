package com.knowledge.base.vo;

import lombok.Data;
import java.util.List;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {
    
    private String token;
    
    private String tokenType;
    
    private Long expiresIn;
    
    private UserInfoVO user;
    
}
