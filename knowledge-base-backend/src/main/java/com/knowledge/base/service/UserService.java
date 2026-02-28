package com.knowledge.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knowledge.base.dto.CreateUserResponseDTO;
import com.knowledge.base.dto.UserDTO;
import com.knowledge.base.entity.User;
import com.knowledge.base.vo.UserInfoVO;

import java.util.List;
import java.util.Set;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     */
    User selectByEmail(String email);
    
    /**
     * 创建用户
     */
    Long createUser(UserDTO userDTO);
    
    /**
     * 创建用户（返回初始密码）
     */
    CreateUserResponseDTO createUserWithPassword(UserDTO userDTO);
    
    /**
     * 重置密码（返回新密码）
     */
    String resetPasswordAndGet(Long id);
    
    /**
     * 更新用户
     */
    void updateUser(UserDTO userDTO);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 重置密码
     */
    void resetPassword(Long id, String newPassword);
    
    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 获取用户信息（带角色和权限）
     */
    UserInfoVO getUserInfo(Long userId);
    
    /**
     * 获取用户角色编码
     */
    Set<String> getUserRoleCodes(Long userId);
    
    /**
     * 获取用户权限编码
     */
    Set<String> getUserPermissionCodes(Long userId);
    
    /**
     * 查询所有用户（带角色）
     */
    List<User> selectUsersWithRoles();
    
    /**
     * 分配角色
     */
    void assignRoles(Long userId, Long[] roleIds);
    
    /**
     * 更新用户状态
     */
    void updateStatus(Long id, Integer status);
    
}
