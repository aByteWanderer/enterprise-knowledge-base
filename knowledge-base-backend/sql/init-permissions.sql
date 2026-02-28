-- 权限初始化SQL（包含正确的parent_id）
-- 执行前先清空数据: DELETE FROM tb_role_permissions; DELETE FROM tb_permissions;

-- 插入权限（第一部分：系统管理）
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('系统管理', 'system', 'menu', 0, '/system', 'Setting', 1, 1);

-- 获取系统管理ID后，插入用户管理
SET @system_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('用户管理', 'user', 'menu', @system_id, '/system/user', 'User', 1, 1);

-- 获取用户管理ID后，插入用户管理按钮
SET @user_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('用户查询', 'user:list', 'button', @user_id, '', '', 1, 1),
('用户新增', 'user:create', 'button', @user_id, '', '', 2, 1),
('用户编辑', 'user:update', 'button', @user_id, '', '', 3, 1),
('用户删除', 'user:delete', 'button', @user_id, '', '', 4, 1),
('分配角色', 'user:assignRole', 'button', @user_id, '', '', 5, 1),
('重置密码', 'user:resetPassword', 'button', @user_id, '', '', 6, 1);

-- 插入角色管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('角色管理', 'role', 'menu', @system_id, '/system/role', 'UserFilled', 2, 1);

SET @role_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('角色查询', 'role:list', 'button', @role_id, '', '', 1, 1),
('角色新增', 'role:create', 'button', @role_id, '', '', 2, 1),
('角色编辑', 'role:update', 'button', @role_id, '', '', 3, 1),
('角色删除', 'role:delete', 'button', @role_id, '', '', 4, 1),
('分配权限', 'role:assignPermission', 'button', @role_id, '', '', 5, 1);

-- 插入权限管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('权限管理', 'permission', 'menu', @system_id, '/system/permission', 'Key', 3, 1);

SET @perm_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('权限查询', 'permission:list', 'button', @perm_id, '', '', 1, 1),
('权限新增', 'permission:create', 'button', @perm_id, '', '', 2, 1),
('权限编辑', 'permission:update', 'button', @perm_id, '', '', 3, 1),
('权限删除', 'permission:delete', 'button', @perm_id, '', '', 4, 1);

-- 插入内容管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('内容管理', 'content', 'menu', 0, '/content', 'Document', 2, 1);

SET @content_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('文章管理', 'article', 'menu', @content_id, '/content/article', 'Document', 1, 1);

SET @article_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('文章查询', 'article:list', 'button', @article_id, '', '', 1, 1),
('文章新增', 'article:create', 'button', @article_id, '', '', 2, 1),
('文章编辑', 'article:update', 'button', @article_id, '', '', 3, 1),
('文章删除', 'article:delete', 'button', @article_id, '', '', 4, 1),
('提交审核', 'article:submit', 'button', @article_id, '', '', 5, 1),
('审核文章', 'article:audit', 'button', @article_id, '', '', 6, 1),
('发布文章', 'article:publish', 'button', @article_id, '', '', 7, 1),
('文章归档', 'article:archive', 'button', @article_id, '', '', 8, 1);

-- 插入分类管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('分类管理', 'category', 'menu', @content_id, '/content/category', 'Folder', 2, 1);

SET @category_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('分类查询', 'category:list', 'button', @category_id, '', '', 1, 1),
('分类新增', 'category:create', 'button', @category_id, '', '', 2, 1),
('分类编辑', 'category:update', 'button', @category_id, '', '', 3, 1),
('分类删除', 'category:delete', 'button', @category_id, '', '', 4, 1);

-- 插入标签管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('标签管理', 'tag', 'menu', @content_id, '/content/tag', 'PriceTag', 3, 1);

SET @tag_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('标签查询', 'tag:list', 'button', @tag_id, '', '', 1, 1),
('标签新增', 'tag:create', 'button', @tag_id, '', '', 2, 1),
('标签编辑', 'tag:update', 'button', @tag_id, '', '', 3, 1),
('标签删除', 'tag:delete', 'button', @tag_id, '', '', 4, 1);

-- 插入审核管理
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('审核管理', 'audit', 'menu', 0, '/audit', 'Stamp', 3, 1);

SET @audit_id = LAST_INSERT_ID();
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
('审核查询', 'audit:list', 'button', @audit_id, '', '', 1, 1),
('审核操作', 'audit:operate', 'button', @audit_id, '', '', 2, 1);

-- 分配权限给角色
INSERT INTO tb_role_permissions (role_id, permission_id) SELECT 1, id FROM tb_permissions;
INSERT INTO tb_role_permissions (role_id, permission_id) SELECT 2, id FROM tb_permissions;
INSERT INTO tb_role_permissions (role_id, permission_id) VALUES (5, 21), (5, 22), (5, 30), (5, 31), (5, 35), (5, 36);
