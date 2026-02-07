package com.knowledge.base.service;

import com.knowledge.base.dto.LoginRequest;
import com.knowledge.base.vo.LoginVO;
import com.knowledge.base.vo.UserInfoVO;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginRequest loginRequest);
    
    /**
     * 用户登出
     */
    void logout();
    
    /**
     * 获取当前用户信息
     */
    UserInfoVO getCurrentUserInfo();
    
    /**
     * 刷新Token
     */
    LoginVO refreshToken(String refreshToken);
    
}
