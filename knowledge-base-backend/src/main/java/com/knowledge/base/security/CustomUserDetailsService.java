package com.knowledge.base.security;

import com.knowledge.base.entity.User;
import com.knowledge.base.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义UserDetailsService实现
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        // 获取角色编码
        Set<String> roleCodes = userService.getUserRoleCodes(user.getId());
        
        // 获取权限编码
        Set<String> permissionCodes = userService.getUserPermissionCodes(user.getId());
        
        // 合并角色和权限作为 authorities
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 添加角色 (格式: ROLE_角色编码)
        for (String role : roleCodes) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            // 同时添加角色编码本身（用于 hasRole 检查）
            authorities.add(new SimpleGrantedAuthority(role));
        }
        
        // 添加权限 (格式: permission:xxx)
        for (String permission : permissionCodes) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1,
                true,
                true,
                true,
                authorities
        );
    }
    
}
