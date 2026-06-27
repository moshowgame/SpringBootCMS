-- ============================================================
-- SpringBootCMS 示例数据
-- 用于初始化演示内容，展示系统功能
-- ============================================================

SET NAMES utf8mb4;

-- ----------------------------
-- 频道数据（4个演示频道）
-- ----------------------------
INSERT INTO `channel` (`channel_id`, `channel_name`, `slug`, `parent_channel_id`, `icon`, `cover_image`, `description`, `channel_type`, `status`, `seq`, `deleted`, `create_time`, `update_time`) VALUES
(1, '产品动态', 'news', 0, 'bi-megaphone', NULL, '最新产品更新、功能发布和重要公告', 1, 1, 1, 0, NOW(), NOW()),
(2, '技术文档', 'docs', 0, 'bi-book', NULL, '系统使用指南、开发文档和技术教程', 1, 1, 2, 0, NOW(), NOW()),
(3, '解决方案', 'solutions', 0, 'bi-lightbulb', NULL, '行业解决方案和成功案例', 1, 1, 3, 0, NOW(), NOW()),
(4, '关于我们', 'about', 0, 'bi-building', NULL, '了解我们团队、公司文化和联系方式', 2, 1, 4, 0, NOW(), NOW());

-- ----------------------------
-- 文章数据（8篇演示文章）
-- ----------------------------
INSERT INTO `article` (`article_id`, `title`, `slug`, `content`, `summary`, `cover_image`, `channel_id`, `keyword`, `create_user_id`, `create_user_name`, `status`, `is_top`, `view_count`, `publish_time`, `deleted`, `create_time`, `update_time`) VALUES
(1, 'SpringBootCMS v2.0 正式发布：全面升级至 Spring Boot 3', 'springbootcms-v2-release', '<p>我们很高兴地宣布，SpringBootCMS 正式发布 v2.0 版本！这是一次重大的技术升级，带来了众多新特性和性能优化。</p><h2>主要更新</h2><ul><li><strong>Spring Boot 3.x</strong> - 升级到最新 LTS 版本，支持 Jakarta EE 9+</li><li><strong>MyBatis 原生</strong> - 移除 MyBatis-Plus，使用原生 XML Mapper</li><li><strong>Spring Security + Session</strong> - 替代 JWT，提供更安全的认证机制</li><li><strong>Caffeine 缓存</strong> - 高性能本地缓存替代 EhCache</li><li><strong>Bootstrap 5</strong> - 现代化前端界面</li></ul><h2>安全加固</h2><p>本次更新特别加强了安全特性：CSRF 防护、Session 固定攻击防护、BCrypt 密码加密、SQL 参数化防注入等。</p><h2>升级指南</h2><p>已有项目请参考官方迁移文档，数据库结构已做好向后兼容。</p>', 'SpringBootCMS v2.0 正式发布，全面升级至 Spring Boot 3，带来性能优化和安全加固。', NULL, 1, 'SpringBoot,CMS,发布', 1, 'admin', 1, 1, 256, NOW(), 0, NOW(), NOW()),

(2, '如何快速部署 SpringBootCMS 到生产环境', 'deploy-guide', '<p>本文详细介绍如何将 SpringBootCMS 部署到生产环境。</p><h2>环境要求</h2><ul><li>JDK 17+</li><li>MySQL 8.0+</li><li>2核4G 以上服务器</li></ul><h2>部署步骤</h2><ol><li>编译打包：<code>mvn clean package -DskipTests</code></li><li>上传 jar 包到服务器</li><li>配置 application-prod.yml</li><li>使用 systemd 或 docker 运行</li></ol><h2>Nginx 配置</h2><p>建议使用 Nginx 反向代理，配置 SSL 证书和静态资源缓存。</p>', '详细的 SpringBootCMS 生产环境部署指南，包含配置优化和安全设置。', NULL, 2, '部署,教程,生产环境', 1, 'admin', 1, 0, 189, NOW(), 0, NOW(), NOW()),

(3, '企业级 CMS 选型指南：为什么选择 SpringBootCMS', 'why-choose', '<p>面对市场上众多的 CMS 系统，企业如何做出正确的选择？本文从多个维度分析为什么 SpringBootCMS 是您的最佳选择。</p><h2>技术领先</h2><p>基于 Spring Boot 3 + MyBatis + Caffeine 缓存，技术栈领先业界。</p><h2>安全可靠</h2><ul><li>Spring Security + Session 认证</li><li>BCrypt 密码加密</li><li>CSRF 防护</li><li>SQL 防注入</li></ul><h2>易于扩展</h2><p>模块化设计，支持二次开发，满足企业个性化需求。</p><h2>成本优势</h2><p>开源免费，降低企业信息化建设成本。</p>', '从技术、安全、扩展性等角度分析企业级 CMS 选型，介绍 SpringBootCMS 的核心优势。', NULL, 3, '企业,CMS,选型', 1, 'admin', 0, 0, 145, NOW(), 0, NOW(), NOW()),

(4, 'SpringBootCMS 富文本编辑器使用教程', 'editor-tutorial', '<p>本文介绍如何使用 SpringBootCMS 内置的富文本编辑器。</p><h2>编辑器特性</h2><ul><li>TinyMCE 8 内核</li><li>支持图片、视频、表格</li><li>代码高亮</li><li>Markdown 兼容</li></ul><h2>快捷键</h2><table class="table"><tr><td>Ctrl+B</td><td>加粗</td></tr><tr><td>Ctrl+I</td><td>斜体</td></tr><tr><td>Ctrl+U</td><td>下划线</td></tr></table><h2>图片上传</h2><p>支持拖拽上传、粘贴上传、拖拽调整大小。</p>', '详细介绍了 SpringBootCMS 富文本编辑器的使用方法，包括快捷键和高级功能。', NULL, 2, '编辑器,教程,TinyMCE', 1, 'admin', 0, 0, 98, NOW(), 0, NOW(), NOW()),

(5, '政府机构内容管理解决方案', 'government-solution', '<p>针对政府机构的内容管理需求，SpringBootCMS 提供完整的解决方案。</p><h2>核心需求</h2><ul><li>信息发布与管理</li><li>多部门协同</li><li>安全合规</li><li>数据统计</li></ul><h2>方案特点</h2><ol><li><strong>权限分级</strong> - 超级管理员、部门管理员、编辑等多级权限</li><li><strong>审批流程</strong> - 文章发布需经过审核</li><li><strong>操作审计</strong> - 完整的日志记录，可追溯</li><li><strong>数据备份</strong> - 自动备份，保障数据安全</li></ol><h2>成功案例</h2><p>已服务于多家政府单位，获得一致好评。</p>', '针对政府机构的内容管理需求，提供多部门协同、审批流程、数据安全等完整解决方案。', NULL, 3, '政府,解决方案', 1, 'admin', 0, 0, 76, NOW(), 0, NOW(), NOW()),

(6, '关于我们的团队', 'about-team', '<p>我们是专注于企业级内容管理系统的技术团队。</p><h2>团队介绍</h2><p>团队成员来自知名互联网公司，拥有丰富的 CMS 开发经验。</p><h2>技术实力</h2><ul><li>10+ 年 Java 开发经验</li><li>5+ 年 CMS 产品研发</li><li>多项软件著作权</li></ul><h2>联系方式</h2><p>邮箱：info@springbootcms.com</p><p>电话：400-888-8888</p><p>地址：中国·上海</p>', '了解 SpringBootCMS 团队、技术实力和发展历程。', NULL, 4, '团队,关于我们', 1, 'admin', 0, 0, 54, NOW(), 0, NOW(), NOW()),

(7, 'SpringBootCMS 活动管理功能介绍', 'activity-feature', '<p>SpringBootCMS 内置强大的活动管理功能，支持在线签到、数据统计等。</p><h2>功能特性</h2><ul><li>活动发布与展示</li><li>在线签到</li><li>请假申请</li><li>签到统计</li><li>数据导出</li></ul><h2>使用场景</h2><ol><li>线下会议签到</li><li>培训活动管理</li><li>团建活动报名</li><li>年会签到</li></ol><h2>数据安全</h2><p>签到数据实时同步，支持批量导出 Excel。</p>', '详细介绍 SpringBootCMS 的活动管理和在线签到功能，适用于各类线下活动场景。', NULL, 1, '活动,签到,功能', 1, 'admin', 0, 0, 67, NOW(), 0, NOW(), NOW()),

(8, '如何优化 CMS 系统的访问性能', 'performance-optimization', '<p>本文介绍如何优化 SpringBootCMS 的访问性能。</p><h2>缓存策略</h2><ul><li>文章内容缓存（15分钟）</li><li>频道列表缓存（60分钟）</li><li>页面模板缓存（30分钟）</li></ul><h2>数据库优化</h2><ol><li>添加适当索引</li><li>使用连接池</li><li>避免深度分页</li></ol><h2>前端优化</h2><ul><li>静态资源本地化</li><li>图片懒加载</li><li>CDN 加速</li></ul><h2>监控告警</h2><p>建议配置 APM 监控，及时发现性能瓶颈。</p>', '从缓存、数据库、前端等多个层面介绍 CMS 系统的性能优化技巧。', NULL, 2, '性能,优化,缓存', 1, 'admin', 0, 0, 112, NOW(), 0, NOW(), NOW());

-- ----------------------------
-- 模板配置数据（首页展示配置）
-- ----------------------------
INSERT INTO `template` (`template_name`, `template_value`, `description`, `page`, `deleted`, `create_time`, `update_time`) VALUES
-- 首页 Banner
('bannerTitle', '专业的内容管理解决方案', '首页横幅标题', 'index', 0, NOW(), NOW()),
('bannerDescription', '基于 Spring Boot 3 + MyBatis 的现代化 CMS 系统，让您的网站建设更高效', '首页横幅描述', 'index', 0, NOW(), NOW()),
('bannerButton', '立即体验', '首页横幅按钮', 'index', 0, NOW(), NOW()),
('bannerUrl', '/page/channel/1', '首页横幅链接', 'index', 0, NOW(), NOW()),
('adminButton', '管理后台', '管理后台按钮', 'index', 0, NOW(), NOW()),

-- 服务区块
('serviceTitle', '核心功能', '服务区块标题', 'index', 0, NOW(), NOW()),
('serviceDescription', '提供完整的文章管理、栏目管理、活动管理、表单管理等功能', '服务区块副标题', 'index', 0, NOW(), NOW()),
('service1title', '文章管理', '服务1标题', 'index', 0, NOW(), NOW()),
('service1description', '富文本编辑、SEO优化、标签管理、专题管理', '服务1描述', 'index', 0, NOW(), NOW()),
('service1url', '/page/channel/1', '服务1链接', 'index', 0, NOW(), NOW()),
('service2title', '栏目管理', '服务2标题', 'index', 0, NOW(), NOW()),
('service2description', '多级栏目、权限控制、模板配置、灵活排序', '服务2描述', 'index', 0, NOW(), NOW()),
('service2url', '/page/channel/2', '服务2链接', 'index', 0, NOW(), NOW()),
('service3title', '活动管理', '服务3标题', 'index', 0, NOW(), NOW()),
('service3description', '活动发布、在线签到、签到统计、导出报表', '服务3描述', 'index', 0, NOW(), NOW()),
('service3url', '/page/activity/search', '服务3链接', 'index', 0, NOW(), NOW()),
('service4title', '表单管理', '服务4标题', 'index', 0, NOW(), NOW()),
('service4description', '自定义表单、表单设计、数据收集、在线审批', '服务4描述', 'index', 0, NOW(), NOW()),
('service4url', '/page/form/search', '服务4链接', 'index', 0, NOW(), NOW()),
('service5title', '安全可靠', '服务5标题', 'index', 0, NOW(), NOW()),
('service5description', 'Spring Security、BCrypt加密、CSRF防护、XSS过滤', '服务5描述', 'index', 0, NOW(), NOW()),
('service6title', '高性能', '服务6标题', 'index', 0, NOW(), NOW()),
('service6description', 'Caffeine缓存、懒加载、CDN加速、数据库优化', '服务6描述', 'index', 0, NOW(), NOW()),

-- 最新资讯
('newsTitle', '最新资讯', '资讯区块标题', 'index', 0, NOW(), NOW()),
('newsDescription', '了解最新的动态与文章', '资讯区块副标题', 'index', 0, NOW(), NOW()),
('readMore', '了解更多', '查看更多按钮', 'index', 0, NOW(), NOW()),

-- 解决方案
('solutionTitle', '一站式内容管理平台', '解决方案标题', 'index', 0, NOW(), NOW()),
('solutionDescription', '从文章发布到数据收集，从模板配置到用户管理，满足您的所有需求', '解决方案描述', 'index', 0, NOW(), NOW()),

-- 公司信息
('companyName', 'SpringBootCMS', '公司名称', 'base', 0, NOW(), NOW()),
('siteDescription', '基于 Spring Boot 3 的现代化内容管理系统，让您的网站建设更高效。', '站点描述', 'base', 0, NOW(), NOW()),
('siteKeywords', 'CMS,SpringBoot,内容管理,文章系统,活动管理', 'SEO关键词', 'base', 0, NOW(), NOW()),
('location', '中国 · 上海', '公司地址', 'base', 0, NOW(), NOW()),
('phone', '400-888-8888', '联系电话', 'base', 0, NOW(), NOW()),
('email', 'info@springbootcms.com', '联系邮箱', 'base', 0, NOW(), NOW()),
('siteCopyright', '基于 Spring Boot 3 + MyBatis 构建', '版权信息', 'base', 0, NOW(), NOW());

-- ----------------------------
-- 标签数据
-- ----------------------------
INSERT INTO `tag` (`tag_name`, `tag_type`, `deleted`, `create_time`, `update_time`) VALUES
('SpringBoot', 'article', 0, NOW(), NOW()),
('CMS', 'article', 0, NOW(), NOW()),
('Java', 'article', 0, NOW(), NOW()),
('MyBatis', 'article', 0, NOW(), NOW()),
('教程', 'article', 0, NOW(), NOW()),
('发布', 'article', 0, NOW(), NOW()),
('安全', 'article', 0, NOW(), NOW()),
('性能优化', 'article', 0, NOW(), NOW()),
('活动', 'activity', 0, NOW(), NOW()),
('签到', 'activity', 0, NOW(), NOW());

-- ----------------------------
-- 文章-标签关联
-- ----------------------------
INSERT INTO `article_tag` (`article_id`, `tag_id`, `create_time`) VALUES
(1, 1, NOW()), (1, 2, NOW()), (1, 6, NOW()),
(2, 5, NOW()), (2, 1, NOW()),
(3, 2, NOW()), (3, 7, NOW()),
(4, 5, NOW()), (4, 1, NOW()),
(5, 2, NOW()),
(7, 9, NOW()), (7, 10, NOW()),
(8, 8, NOW()), (8, 1, NOW()), (8, 3, NOW());

-- ----------------------------
-- 示例活动数据
-- ----------------------------
INSERT INTO `activity` (`activity_name`, `activity_desc`, `start_time`, `end_time`, `create_user_id`, `create_user_name`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('2024年度技术大会', '年度技术大会，汇聚业内专家，分享最新技术趋势。', DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 'admin', 1, 0, NOW(), NOW()),
('产品发布会', '新产品发布会，现场演示和体验。', DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY), 1, 'admin', 1, 0, NOW(), NOW());

-- ----------------------------
-- 示例表单数据
-- ----------------------------
INSERT INTO `form` (`form_name`, `form_type`, `create_user_id`, `create_user_name`, `form_desc`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('意见反馈', 1, 1, 'admin', '收集用户意见和反馈，持续改进产品', 1, 0, NOW(), NOW()),
('产品试用申请', 1, 1, 'admin', '申请试用我们的产品和服务', 1, 0, NOW(), NOW());

-- ----------------------------
-- 表单字段数据
-- ----------------------------
INSERT INTO `form_item` (`form_id`, `item_name`, `item_type`, `default_value`, `item_tip`, `placeholder`, `required`, `sort`, `deleted`, `create_time`, `update_time`) VALUES
-- 意见反馈表单字段
(1, '姓名', 'input', NULL, '请输入您的姓名', '请输入姓名', 1, 1, 0, NOW(), NOW()),
(1, '邮箱', 'input', NULL, '请输入您的邮箱', '请输入邮箱', 1, 2, 0, NOW(), NOW()),
(1, '手机', 'input', NULL, '请输入您的手机号', '请输入手机号', 0, 3, 0, NOW(), NOW()),
(1, '反馈类型', 'select', '功能建议', NULL, '请选择反馈类型', 1, 4, 0, NOW(), NOW()),
(1, '反馈内容', 'textarea', NULL, '请详细描述您的问题或建议', '请输入反馈内容', 1, 5, 0, NOW(), NOW()),

-- 产品试用申请表单字段
(2, '公司名称', 'input', NULL, '请输入公司全称', '请输入公司名称', 1, 1, 0, NOW(), NOW()),
(2, '联系人', 'input', NULL, '请输入联系人姓名', '请输入联系人', 1, 2, 0, NOW(), NOW()),
(2, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 3, 0, NOW(), NOW()),
(2, '邮箱', 'input', NULL, '请输入公司邮箱', '请输入邮箱', 1, 4, 0, NOW(), NOW()),
(2, '使用场景', 'textarea', NULL, '请描述您的使用场景和需求', '请输入使用场景', 1, 5, 0, NOW(), NOW());

-- 更新表单字段的选项值
UPDATE `form_item` SET `default_value` = '功能建议;Bug反馈;体验优化;其他' WHERE `form_id` = 1 AND `item_name` = '反馈类型';
