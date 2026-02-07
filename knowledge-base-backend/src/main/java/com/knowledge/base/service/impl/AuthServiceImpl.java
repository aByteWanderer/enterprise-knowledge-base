package com.knowledge.base.service.impl;

import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.LoginRequest;
import com.knowledge.base.entity.User;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.security.JwtTokenProvider;
import com.knowledge.base.service.AuthService;
import com.knowledge.base.service.UserService;
import com.knowledge.base.vo.LoginVO;
import com.knowledge.base.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    
    @Override
    public LoginVO login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);
        
        User user = userService.selectByUsername(loginRequest.getUsername());
        UserInfoVO userInfo = userService.getUserInfo(user.getId());
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(86400L);
        loginVO.setUser(userInfo);
        
        return loginVO;
    }
    
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
    
    @Override
    public UserInfoVO getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        
        String username = authentication.getName();
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        return userService.getUserInfo(user.getId());
    }
    
    @Override
    public LoginVO refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        
        String newToken = jwtTokenProvider.generateToken(username, claims);
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(newToken);
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(86400L);
        
        return loginVO;
    }
    
}
