-- 企业知识库系统数据库初始化脚本
-- Database: knowledge_base
-- Charset: utf8mb4
-- Collation: utf8mb4_unicode_ci

CREATE DATABASE IF NOT EXISTS knowledge_base DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE knowledge_base;

-- ==================== 用户表 ====================
DROP TABLE IF EXISTS tb_user_roles;
DROP TABLE IF EXISTS tb_role_permissions;
DROP TABLE IF EXISTS tb_article_tags;
DROP TABLE IF EXISTS tb_article_audits;
DROP TABLE IF EXISTS tb_article_versions;
DROP TABLE IF EXISTS tb_attachments;
DROP TABLE IF EXISTS tb_tags;
DROP TABLE IF EXISTS tb_articles;
DROP TABLE IF EXISTS tb_categories;
DROP TABLE IF EXISTS tb_permissions;
DROP TABLE IF EXISTS tb_roles;
DROP TABLE IF EXISTS tb_users;

CREATE TABLE tb_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(500) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ==================== 角色表 ====================
CREATE TABLE tb_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(200) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除',
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ==================== 权限表 ====================
CREATE TABLE tb_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    type VARCHAR(20) COMMENT '类型: menu-菜单, button-按钮',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    path VARCHAR(200) COMMENT '路由路径',
    icon VARCHAR(100) COMMENT '图标',
    order_num INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ==================== 用户角色关联表 ====================
CREATE TABLE tb_user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ==================== 角色权限关联表 ====================
CREATE TABLE tb_role_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ==================== 分类表 ====================
CREATE TABLE tb_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(50) COMMENT '分类编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
    level INT DEFAULT 1 COMMENT '层级',
    path VARCHAR(500) COMMENT '路径',
    description VARCHAR(200) COMMENT '描述',
    icon VARCHAR(100) COMMENT '图标',
    order_num INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ==================== 文章表 ====================
CREATE TABLE tb_articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content LONGTEXT COMMENT '内容',
    summary VARCHAR(500) COMMENT '摘要',
    category_id BIGINT COMMENT '分类ID',
    author_id BIGINT COMMENT '作者ID',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PENDING-审核中, APPROVED-已通过, REJECTED-已拒绝, PUBLISHED-已发布, ARCHIVED-已归档',
    version INT DEFAULT 1 COMMENT '版本号',
    view_count BIGINT DEFAULT 0 COMMENT '浏览次数',
    thumbnail VARCHAR(500) COMMENT '缩略图',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐: 0-否, 1-是',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除',
    INDEX idx_category (category_id),
    INDEX idx_author (author_id),
    INDEX idx_status (status),
    FULLTEXT INDEX idx_content (title, content) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ==================== 标签表 ====================
CREATE TABLE tb_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    color VARCHAR(20) DEFAULT '#409EFF' COMMENT '颜色',
    article_count INT DEFAULT 0 COMMENT '文章数量',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除',
    UNIQUE KEY uk_tag_name (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- ==================== 文章标签关联表 ====================
CREATE TABLE tb_article_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    article_id BIGINT NOT NULL COMMENT '文章ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_article_tag (article_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签关联表';

-- ==================== 附件表 ====================
CREATE TABLE tb_attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '附件ID',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_md5 VARCHAR(32) COMMENT '文件MD5',
    article_id BIGINT COMMENT '文章ID',
    thumbnail_path VARCHAR(500) COMMENT '缩略图路径',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志: 0-未删除, 1-已删除',
    INDEX idx_article (article_id),
    INDEX idx_md5 (file_md5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='附件表';

-- ==================== 文章审核记录表 ====================
CREATE TABLE tb_article_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    article_id BIGINT NOT NULL COMMENT '文章ID',
    status VARCHAR(20) NOT NULL COMMENT '审核状态: APPROVED-通过, REJECTED-拒绝',
    remark VARCHAR(500) COMMENT '审核备注',
    auditor_id BIGINT COMMENT '审核人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_article (article_id),
    INDEX idx_auditor (auditor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章审核记录表';

-- ==================== 文章版本表 ====================
CREATE TABLE tb_article_versions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    article_id BIGINT NOT NULL COMMENT '文章ID',
    title VARCHAR(200) COMMENT '标题',
    content LONGTEXT COMMENT '内容',
    version INT NOT NULL COMMENT '版本号',
    created_by BIGINT COMMENT '创建人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_article (article_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章版本表';

-- ==================== 初始化数据 ====================

-- 插入管理员用户 (密码: 123456)
INSERT INTO tb_users (username, password, email, status) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'admin@knowledge.com', 1),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnu6Z5EHTJ8iAtsM8lE9lBOsl7iAt6Z5EH', 'test@knowledge.com', 1);

-- 插入角色
INSERT INTO tb_roles (role_name, role_code, description) VALUES 
('超级管理员', 'superadmin', '拥有所有权限'),
('管理员', 'admin', '系统管理员'),
('编辑', 'editor', '文章编辑人员'),
('审核员', 'auditor', '文章审核人员'),
('普通用户', 'user', '普通用户');

-- 插入权限（菜单 + 按钮）
INSERT INTO tb_permissions (permission_name, permission_code, type, parent_id, path, icon, order_num, status) VALUES 
-- 系统管理菜单
('系统管理', 'system', 'menu', 0, '/system', 'Setting', 1, 1),
('用户管理', 'user', 'menu', 1, '/system/user', 'User', 1, 1),
('用户查询', 'user:list', 'button', 2, '', '', 1, 1),
('用户新增', 'user:create', 'button', 2, '', '', 2, 1),
('用户编辑', 'user:update', 'button', 2, '', '', 3, 1),
('用户删除', 'user:delete', 'button', 2, '', '', 4, 1),
('分配角色', 'user:assignRole', 'button', 2, '', '', 5, 1),
('重置密码', 'user:resetPassword', 'button', 2, '', '', 6, 1),
('角色管理', 'role', 'menu', 1, '/system/role', 'UserFilled', 2, 1),
('角色查询', 'role:list', 'button', 9, '', '', 1, 1),
('角色新增', 'role:create', 'button', 9, '', '', 2, 1),
('角色编辑', 'role:update', 'button', 9, '', '', 3, 1),
('角色删除', 'role:delete', 'button', 9, '', '', 4, 1),
('分配权限', 'role:assignPermission', 'button', 9, '', '', 5, 1),
('权限管理', 'permission', 'menu', 1, '/system/permission', 'Key', 3, 1),
('权限查询', 'permission:list', 'button', 15, '', '', 1, 1),
('权限新增', 'permission:create', 'button', 15, '', '', 2, 1),
('权限编辑', 'permission:update', 'button', 15, '', '', 3, 1),
('权限删除', 'permission:delete', 'button', 15, '', '', 4, 1),
-- 内容管理菜单
('内容管理', 'content', 'menu', 0, '/content', 'Document', 2, 1),
('文章管理', 'article', 'menu', 20, '/content/article', 'Document', 1, 1),
('文章查询', 'article:list', 'button', 21, '', '', 1, 1),
('文章新增', 'article:create', 'button', 21, '', '', 2, 1),
('文章编辑', 'article:update', 'button', 21, '', '', 3, 1),
('文章删除', 'article:delete', 'button', 21, '', '', 4, 1),
('提交审核', 'article:submit', 'button', 21, '', '', 5, 1),
('审核文章', 'article:audit', 'button', 21, '', '', 6, 1),
('发布文章', 'article:publish', 'button', 21, '', '', 7, 1),
('文章归档', 'article:archive', 'button', 21, '', '', 8, 1),
('分类管理', 'category', 'menu', 20, '/content/category', 'Folder', 2, 1),
('分类查询', 'category:list', 'button', 30, '', '', 1, 1),
('分类新增', 'category:create', 'button', 30, '', '', 2, 1),
('分类编辑', 'category:update', 'button', 30, '', '', 3, 1),
('分类删除', 'category:delete', 'button', 30, '', '', 4, 1),
('标签管理', 'tag', 'menu', 20, '/content/tag', 'PriceTag', 3, 1),
('标签查询', 'tag:list', 'button', 35, '', '', 1, 1),
('标签新增', 'tag:create', 'button', 35, '', '', 2, 1),
('标签编辑', 'tag:update', 'button', 35, '', '', 3, 1),
('标签删除', 'tag:delete', 'button', 35, '', '', 4, 1),
-- 审核管理
('审核管理', 'audit', 'menu', 0, '/audit', 'Stamp', 3, 1),
('审核查询', 'audit:list', 'button', 40, '', '', 1, 1),
('审核操作', 'audit:operate', 'button', 40, '', '', 2, 1);

-- 分配权限给超级管理员角色（所有权限）
INSERT INTO tb_role_permissions (role_id, permission_id) VALUES 
-- 系统管理权限
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9),
(1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19),
-- 内容管理权限
(1, 20), (1, 21), (1, 22), (1, 23), (1, 24), (1, 25), (1, 26), (1, 27), (1, 28), (1, 29),
(1, 30), (1, 31), (1, 32), (1, 33), (1, 34), (1, 35), (1, 36), (1, 37), (1, 38), (1, 39),
-- 审核管理权限
(1, 40), (1, 41), (1, 42);

-- 分配权限给管理员角色（大部分权限）
INSERT INTO tb_role_permissions (role_id, permission_id) VALUES 
-- 系统管理权限
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9),
(2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16), (2, 17), (2, 18), (2, 19),
-- 内容管理权限
(2, 20), (2, 21), (2, 22), (2, 23), (2, 24), (2, 25), (2, 26), (2, 27), (2, 28), (2, 29),
(2, 30), (2, 31), (2, 32), (2, 33), (2, 34), (2, 35), (2, 36), (2, 37), (2, 38), (2, 39),
-- 审核管理权限
(2, 40), (2, 41), (2, 42);

-- 分配权限给普通用户（只读权限）
INSERT INTO tb_role_permissions (role_id, permission_id) VALUES 
(5, 21), (5, 22), (5, 30), (5, 31), (5, 35), (5, 36);

-- 分配角色给管理员用户
INSERT INTO tb_user_roles (user_id, role_id) VALUES 
(1, 1), (1, 2), (2, 4);

-- 插入一级分类
INSERT INTO tb_categories (category_name, category_code, parent_id, level, path, icon, order_num) VALUES 
('技术文档', 'tech', 0, 1, '/tech', 'Monitor', 1),
('产品文档', 'product', 0, 1, '/product', 'Goods', 2),
('运营文档', 'operation', 0, 1, '/operation', 'DataLine', 3),
('其他', 'other', 0, 1, '/other', 'Document', 4);

-- 插入二级分类
INSERT INTO tb_categories (category_name, category_code, parent_id, level, path, icon, order_num) VALUES 
('Java开发', 'java', 1, 2, '/tech/java', 'Coffee', 1),
('Python开发', 'python', 1, 2, '/tech/python', 'Snake', 2),
('前端开发', 'frontend', 1, 2, '/tech/frontend', 'Monitor', 3),
('产品需求', 'requirement', 2, 2, '/product/requirement', 'Edit', 1),
('产品设计', 'design', 2, 2, '/product/design', 'Brush', 2),
('运营活动', 'activity', 3, 2, '/operation/activity', 'Promotion', 1),
('数据分析', 'analysis', 3, 2, '/operation/analysis', 'DataAnalysis', 2);

-- 插入标签
INSERT INTO tb_tags (tag_name, color) VALUES 
('Java', '#409EFF'),
('Spring Boot', '#67C23A'),
('MySQL', '#E6A23C'),
('Redis', '#F56C6C'),
('Vue', '#909399'),
('Docker', '#409EFF'),
('Kubernetes', '#67C23A'),
('微服务', '#E6A23C');

COMMIT;
