package com.knowledge.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knowledge.base.entity.UserRole;
import com.knowledge.base.mapper.UserRoleMapper;
import com.knowledge.base.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色服务实现
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    
}
