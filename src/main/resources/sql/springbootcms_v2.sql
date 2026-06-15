-- ============================================================
-- SpringBootCMS DDL v2.0
-- Engine: InnoDB | Charset: utf8mb4 | Soft Delete: deleted
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table: user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(100) NOT NULL COMMENT '账号名称',
  `password` varchar(255) NOT NULL COMMENT '密码(BCrypt hash)',
  `show_name` varchar(100) DEFAULT NULL COMMENT '显示名称',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `company_name` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `company_address` varchar(255) DEFAULT NULL COMMENT '公司地址',
  `open_id` varchar(100) DEFAULT NULL COMMENT '微信openid',
  `role_id` int DEFAULT 1 COMMENT '角色id',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用:1启用 0停用',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_name` (`user_name`),
  UNIQUE KEY `uk_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Table: role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- ----------------------------
-- Table: menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `title` varchar(100) NOT NULL COMMENT '名字',
  `href` varchar(255) DEFAULT '/cms/index' COMMENT '地址',
  `icon` varchar(100) DEFAULT 'bi-grid' COMMENT '图标',
  `target` varchar(20) DEFAULT '_self' COMMENT '目标',
  `parent_menu_id` int NOT NULL DEFAULT 0 COMMENT '父菜单id',
  `role_id` varchar(255) DEFAULT '1,9' COMMENT '权限(逗号分隔角色id)',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';

-- ----------------------------
-- Table: channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `channel_id` int NOT NULL AUTO_INCREMENT COMMENT '频道ID',
  `channel_name` varchar(100) NOT NULL COMMENT '频道名称',
  `slug` varchar(100) DEFAULT NULL COMMENT 'URL友好别名',
  `parent_channel_id` int NOT NULL DEFAULT 0 COMMENT '父频道ID',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图',
  `description` varchar(500) DEFAULT NULL COMMENT '频道描述',
  `channel_type` tinyint NOT NULL DEFAULT 1 COMMENT '频道类型:1列表 2单页 3外链',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '发布状态:1发布 0未发布',
  `seq` int NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`channel_id`),
  UNIQUE KEY `uk_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='频道/栏目表';

-- ----------------------------
-- Table: article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `article_id` int NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `slug` varchar(255) DEFAULT NULL COMMENT 'URL友好别名',
  `content` longtext DEFAULT NULL COMMENT '内容',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图',
  `channel_id` int DEFAULT NULL COMMENT '频道ID',
  `keyword` varchar(255) DEFAULT NULL COMMENT '关键字,逗号分隔',
  `create_user_id` int DEFAULT NULL COMMENT '创建者ID',
  `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建者名称',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '发布状态:1发布 0未发布',
  `is_top` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶:0否 1是',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间(支持定时发布)',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`article_id`),
  KEY `idx_channel_id` (`channel_id`),
  KEY `idx_create_user_id` (`create_user_id`),
  KEY `idx_publish_time` (`publish_time`),
  FULLTEXT KEY `idx_full_keyword` (`keyword`),
  FULLTEXT KEY `idx_full_content` (`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章表';

-- ----------------------------
-- Table: tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `tag_id` int NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(100) NOT NULL COMMENT '标签名称',
  `tag_type` varchar(50) DEFAULT 'article' COMMENT '标签类型:article文章 activity活动',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `uk_tag_name_type` (`tag_name`, `tag_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='标签表';

-- ----------------------------
-- Table: article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int NOT NULL COMMENT '文章ID',
  `tag_id` int NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章-标签关联表';

-- ----------------------------
-- Table: template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `template_id` int NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(100) NOT NULL COMMENT '配置key',
  `template_value` varchar(500) DEFAULT NULL COMMENT '配置值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `page` varchar(50) DEFAULT 'base' COMMENT '所属页面',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`template_id`),
  KEY `idx_page` (`page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板配置表';

-- ----------------------------
-- Table: activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activity_id` int NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `activity_name` varchar(255) NOT NULL COMMENT '活动名称',
  `activity_desc` text DEFAULT NULL COMMENT '活动简介',
  `start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `create_user_id` int DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建用户姓名',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '活动状态:0未发布 1已发布',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动表';

-- ----------------------------
-- Table: activity_sign
-- ----------------------------
DROP TABLE IF EXISTS `activity_sign`;
CREATE TABLE `activity_sign` (
  `sign_id` int NOT NULL AUTO_INCREMENT COMMENT '签到id',
  `activity_id` int NOT NULL COMMENT '活动id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '账号',
  `show_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `sign_type` tinyint NOT NULL DEFAULT 1 COMMENT '签到类型:1签到 2请假',
  `leave_reason` varchar(255) DEFAULT NULL COMMENT '请假理由',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `company` varchar(255) DEFAULT NULL COMMENT '公司名',
  `sign_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sign_id`),
  KEY `idx_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动签到表';

-- ----------------------------
-- Table: form
-- ----------------------------
DROP TABLE IF EXISTS `form`;
CREATE TABLE `form` (
  `form_id` int NOT NULL AUTO_INCREMENT COMMENT '表单id',
  `form_name` varchar(255) NOT NULL COMMENT '表单名称',
  `form_type` tinyint DEFAULT 1 COMMENT '表单类型:1普通 2专项(限一次)',
  `create_user_id` int DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建用户名称',
  `form_desc` text DEFAULT NULL COMMENT '表单描述',
  `attachment` varchar(500) DEFAULT NULL COMMENT '附件',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态:0未发布 1已发布',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单模板表';

-- ----------------------------
-- Table: form_item
-- ----------------------------
DROP TABLE IF EXISTS `form_item`;
CREATE TABLE `form_item` (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT '表单项id',
  `form_id` int NOT NULL COMMENT '表单id',
  `item_name` varchar(255) NOT NULL COMMENT '表单项名称',
  `item_type` varchar(50) NOT NULL DEFAULT 'input' COMMENT '类型:input/textarea/select/radio/checkbox/fileupload',
  `default_value` varchar(500) DEFAULT NULL COMMENT '默认值(select类型用;分隔选项)',
  `item_tip` varchar(255) DEFAULT NULL COMMENT '提示文字',
  `placeholder` varchar(255) DEFAULT NULL COMMENT '占位提示',
  `required` tinyint NOT NULL DEFAULT 0 COMMENT '是否必填:0否 1是',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`item_id`),
  KEY `idx_form_id` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单字段表';

-- ----------------------------
-- Table: form_submit
-- ----------------------------
DROP TABLE IF EXISTS `form_submit`;
CREATE TABLE `form_submit` (
  `submit_id` int NOT NULL AUTO_INCREMENT COMMENT '提交id',
  `form_id` int NOT NULL COMMENT '表单id',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '账号',
  `show_name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机',
  `company` varchar(255) DEFAULT NULL COMMENT '公司',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:0审核未通过 1待审核 2审核通过',
  `audit_user_id` int DEFAULT NULL COMMENT '审核用户ID',
  `audit_user_name` varchar(100) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`submit_id`),
  KEY `idx_form_id` (`form_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单提交表';

-- ----------------------------
-- Table: form_submit_value
-- ----------------------------
DROP TABLE IF EXISTS `form_submit_value`;
CREATE TABLE `form_submit_value` (
  `value_id` int NOT NULL AUTO_INCREMENT COMMENT '值id',
  `item_id` int NOT NULL COMMENT '表单项id',
  `form_id` int NOT NULL COMMENT '表单id',
  `value_text` text DEFAULT NULL COMMENT '值',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `submit_id` int NOT NULL COMMENT '提交id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`value_id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_submit_id` (`submit_id`),
  KEY `idx_form_id` (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单提交值表';

-- ----------------------------
-- Table: media (新增)
-- ----------------------------
DROP TABLE IF EXISTS `media`;
CREATE TABLE `media` (
  `media_id` int NOT NULL AUTO_INCREMENT COMMENT '媒体ID',
  `file_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `file_path` varchar(500) NOT NULL COMMENT '存储路径',
  `file_size` bigint DEFAULT 0 COMMENT '文件大小(字节)',
  `file_type` varchar(50) DEFAULT NULL COMMENT 'MIME类型',
  `file_ext` varchar(20) DEFAULT NULL COMMENT '文件扩展名',
  `media_type` varchar(20) NOT NULL DEFAULT 'image' COMMENT '媒体类型:image/video/audio/document/other',
  `width` int DEFAULT NULL COMMENT '图片宽度',
  `height` int DEFAULT NULL COMMENT '图片高度',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `alt_text` varchar(255) DEFAULT NULL COMMENT '替代文本',
  `upload_user_id` int DEFAULT NULL COMMENT '上传用户ID',
  `upload_user_name` varchar(100) DEFAULT NULL COMMENT '上传用户名',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`media_id`),
  KEY `idx_media_type` (`media_type`),
  KEY `idx_upload_user_id` (`upload_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='媒体资源表';

-- ----------------------------
-- Table: audit_log (新增)
-- ----------------------------
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int DEFAULT NULL COMMENT '操作用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '操作用户名',
  `action` varchar(50) NOT NULL COMMENT '操作类型:CREATE/UPDATE/DELETE/LOGIN/LOGOUT/PUBLISH',
  `module` varchar(50) DEFAULT NULL COMMENT '模块:article/channel/user/form/activity等',
  `target_id` varchar(100) DEFAULT NULL COMMENT '操作对象ID',
  `target_name` varchar(255) DEFAULT NULL COMMENT '操作对象名称',
  `detail` text DEFAULT NULL COMMENT '操作详情(JSON)',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '浏览器UA',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_action` (`action`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- ----------------------------
-- Table: site_config (新增)
-- ----------------------------
DROP TABLE IF EXISTS `site_config`;
CREATE TABLE `site_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置key',
  `config_value` text DEFAULT NULL COMMENT '配置值',
  `config_group` varchar(50) NOT NULL DEFAULT 'general' COMMENT '配置分组:general/seo/email/upload等',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除:0正常 1已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='站点全局配置表';

-- ----------------------------
-- Initial Data
-- ----------------------------
INSERT INTO `role` VALUES (1, '普通用户', '普通用户权限', 0, NOW(), NOW());
INSERT INTO `role` VALUES (9, '管理员', '管理员权限', 0, NOW(), NOW());

-- admin密码为BCrypt加密后的123456
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', '18588888888', 'softdev@xxx.com', 'egag', NULL, NULL, 9, 1, 0, NOW(), NOW());

INSERT INTO `site_config` VALUES
(1, 'site_name', 'SpringBootCMS', 'general', '站点名称', 1, 0, NOW(), NOW()),
(2, 'site_description', '基于SpringBoot的内容管理系统', 'general', '站点描述', 2, 0, NOW(), NOW()),
(3, 'site_keywords', 'CMS,SpringBoot,内容管理', 'seo', 'SEO关键词', 1, 0, NOW(), NOW()),
(4, 'site_copyright', 'Copyright © SpringBootCMS', 'general', '版权信息', 3, 0, NOW(), NOW()),
(5, 'upload_max_size', '10485760', 'upload', '上传文件大小限制(字节)', 1, 0, NOW(), NOW()),
(6, 'upload_allowed_types', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx', 'upload', '允许上传的文件类型', 2, 0, NOW(), NOW()),
(7, 'upload_image_max_width', '1920', 'upload', '图片最大宽度', 3, 0, NOW(), NOW()),
(8, 'smtp_host', '', 'email', 'SMTP服务器', 1, 0, NOW(), NOW()),
(9, 'smtp_port', '465', 'email', 'SMTP端口', 2, 0, NOW(), NOW()),
(10, 'smtp_username', '', 'email', 'SMTP用户名', 3, 0, NOW(), NOW()),
(11, 'smtp_password', '', 'email', 'SMTP密码', 4, 0, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;
