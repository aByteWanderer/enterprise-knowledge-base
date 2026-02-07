package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.common.ResultCode;
import com.knowledge.base.dto.UserDTO;
import com.knowledge.base.entity.User;
import com.knowledge.base.entity.UserRole;
import com.knowledge.base.exception.BusinessException;
import com.knowledge.base.mapper.UserMapper;
import com.knowledge.base.mapper.UserRoleMapper;
import com.knowledge.base.service.UserRoleService;
import com.knowledge.base.service.UserService;
import com.knowledge.base.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public User selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserDTO userDTO) {
        // 检查用户名是否存在
        User existUser = selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_EXISTS, "用户名已存在");
        }
        
        // 检查邮箱是否存在
        User existEmail = selectByEmail(userDTO.getEmail());
        if (existEmail != null) {
            throw new BusinessException(ResultCode.USER_EXISTS, "邮箱已被注册");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 设置默认密码
        String password = userDTO.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : 1);
        
        userMapper.insert(user);
        
        // 分配角色
        if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }
        
        return user.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 检查用户名是否被其他用户使用
        User existUser = selectByUsername(userDTO.getUsername());
        if (existUser != null && !existUser.getId().equals(userDTO.getId())) {
            throw new BusinessException(ResultCode.USER_EXISTS, "用户名已存在");
        }
        
        // 检查邮箱是否被其他用户使用
        User existEmail = selectByEmail(userDTO.getEmail());
        if (existEmail != null && !existEmail.getId().equals(userDTO.getId())) {
            throw new BusinessException(ResultCode.USER_EXISTS, "邮箱已被注册");
        }
        
        BeanUtils.copyProperties(userDTO, user, "password");
        userMapper.updateById(user);
        
        // 更新角色
        if (userDTO.getRoleIds() != null) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setDeleted(1);
        userMapper.updateById(user);
    }
    
    @Override
    public void resetPassword(Long id, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.ERROR, "原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }
    
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());
        
        // 获取角色
        Set<String> roles = getUserRoleCodes(userId);
        userInfo.setRoles(new ArrayList<>(roles));
        
        // 获取权限
        Set<String> permissions = getUserPermissionCodes(userId);
        userInfo.setPermissions(new ArrayList<>(permissions));
        
        return userInfo;
    }
    
    @Override
    public Set<String> getUserRoleCodes(Long userId) {
        return userMapper.selectRoleCodesByUserId(userId);
    }
    
    @Override
    public Set<String> getUserPermissionCodes(Long userId) {
        return userMapper.selectPermissionCodesByUserId(userId);
    }
    
    @Override
    public List<User> selectUsersWithRoles() {
        return userMapper.selectUsersWithRoles();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, Long[] roleIds) {
        // 删除原有角色
        userRoleMapper.deleteByUserId(userId);
        
        // 分配新角色
        if (roleIds != null && roleIds.length > 0) {
            LocalDateTime now = LocalDateTime.now();
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreatedAt(now);
                userRoleMapper.insert(userRole);
            }
        }
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }
    
}
