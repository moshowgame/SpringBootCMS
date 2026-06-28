-- ============================================================
-- SpringBootCMS 示例数据 - 企业云通信风格
-- ============================================================

SET NAMES utf8mb4;

-- ----------------------------
-- 后台管理菜单数据
-- ----------------------------
INSERT INTO `menu` (`menu_id`, `title`, `href`, `icon`, `target`, `parent_menu_id`, `role_id`, `sort`, `deleted`, `create_time`, `update_time`) VALUES
-- 顶级菜单 - 内容管理
(1, '内容管理', '/admin/article/list', 'bi-file-text', '_self', 0, '1,9', 1, 0, NOW(), NOW()),
-- 顶级菜单 - 栏目管理
(2, '栏目管理', '/admin/channel/list', 'bi-grid', '_self', 0, '1,9', 2, 0, NOW(), NOW()),
-- 顶级菜单 - 用户管理
(3, '用户管理', '/admin/user/list', 'bi-people', '_self', 0, '1,9', 3, 0, NOW(), NOW()),
-- 顶级菜单 - 活动管理
(4, '活动管理', '/admin/activity/list', 'bi-calendar', '_self', 0, '1,9', 4, 0, NOW(), NOW()),
-- 顶级菜单 - 表单管理
(5, '表单管理', '/admin/form/list', 'bi-list-checks', '_self', 0, '1,9', 5, 0, NOW(), NOW()),
-- 顶级菜单 - 媒体管理
(6, '媒体管理', '/admin/media/list', 'bi-image', '_self', 0, '1,9', 6, 0, NOW(), NOW()),
-- 顶级菜单 - 系统设置
(7, '系统设置', '/admin/menu/list', 'bi-gear', '_self', 0, '1,9', 7, 0, NOW(), NOW()),

-- 内容管理子菜单
(8, '文章列表', '/admin/article/list', 'bi-file-text', '_self', 1, '1,9', 1, 0, NOW(), NOW()),
(9, '添加文章', '/admin/article/edit?id=0', 'bi-plus-circle', '_self', 1, '1,9', 2, 0, NOW(), NOW()),
(10, '标签管理', '/admin/tag/list', 'bi-tags', '_self', 1, '1,9', 3, 0, NOW(), NOW()),

-- 栏目管理子菜单
(11, '栏目列表', '/admin/channel/list', 'bi-grid', '_self', 2, '1,9', 1, 0, NOW(), NOW()),
(12, '添加栏目', '/admin/channel/edit?id=0', 'bi-plus-circle', '_self', 2, '1,9', 2, 0, NOW(), NOW()),

-- 用户管理子菜单
(13, '用户列表', '/admin/user/list', 'bi-people', '_self', 3, '1,9', 1, 0, NOW(), NOW()),
(14, '添加用户', '/admin/user/edit?id=0', 'bi-plus-circle', '_self', 3, '1,9', 2, 0, NOW(), NOW()),

-- 活动管理子菜单
(15, '活动列表', '/admin/activity/list', 'bi-calendar', '_self', 4, '1,9', 1, 0, NOW(), NOW()),
(16, '添加活动', '/admin/activity/edit?id=0', 'bi-plus-circle', '_self', 4, '1,9', 2, 0, NOW(), NOW()),
(17, '活动报名', '/admin/activitySign/list', 'bi-list-checks', '_self', 4, '1,9', 3, 0, NOW(), NOW()),

-- 表单管理子菜单
(18, '表单列表', '/admin/form/list', 'bi-list-checks', '_self', 5, '1,9', 1, 0, NOW(), NOW()),
(19, '添加表单', '/admin/form/edit?id=0', 'bi-plus-circle', '_self', 5, '1,9', 2, 0, NOW(), NOW()),
(20, '表单数据', '/admin/formSubmit/list', 'bi-database', '_self', 5, '1,9', 3, 0, NOW(), NOW()),

-- 媒体管理子菜单
(21, '媒体列表', '/admin/media/list', 'bi-image', '_self', 6, '1,9', 1, 0, NOW(), NOW()),

-- 系统设置子菜单
(22, '菜单管理', '/admin/menu/list', 'bi-menu-button-wide', '_self', 7, '1,9', 1, 0, NOW(), NOW()),
(23, '模板管理', '/admin/template/list', 'bi-layout', '_self', 7, '1,9', 2, 0, NOW(), NOW()),
(24, '站点配置', '/admin/siteConfig/list', 'bi-globe', '_self', 7, '1,9', 3, 0, NOW(), NOW()),
(25, '操作日志', '/admin/auditLog/list', 'bi-journal-text', '_self', 7, '1,9', 4, 0, NOW(), NOW());

-- ----------------------------
-- 频道数据（4个演示频道）
-- ----------------------------
INSERT INTO `channel` (`channel_id`, `channel_name`, `slug`, `parent_channel_id`, `icon`, `cover_image`, `description`, `channel_type`, `status`, `seq`, `deleted`, `create_time`, `update_time`) VALUES
(1, '产品服务', 'services', 0, 'bi-box-seam', NULL, '短信通知、语音验证、国际短信等核心产品服务', 1, 1, 1, 0, NOW(), NOW()),
(2, '新闻动态', 'news', 0, 'bi-newspaper', NULL, '公司新闻、行业资讯、技术动态', 1, 1, 2, 0, NOW(), NOW()),
(3, '解决方案', 'solutions', 0, 'bi-lightbulb', NULL, '金融、电商、医疗等行业解决方案', 1, 1, 3, 0, NOW(), NOW()),
(4, '关于我们', 'about', 0, 'bi-building', NULL, '公司介绍、团队、联系方式', 2, 1, 4, 0, NOW(), NOW());

-- ----------------------------
-- 文章数据（8篇演示文章）
-- ----------------------------
INSERT INTO `article` (`article_id`, `title`, `slug`, `content`, `summary`, `cover_image`, `channel_id`, `keyword`, `create_user_id`, `create_user_name`, `status`, `is_top`, `view_count`, `publish_time`, `deleted`, `create_time`, `update_time`) VALUES
(1, '智慧云通信平台全新升级，打造新一代企业通信解决方案', 'platform-upgrade', '<p>智慧云通信平台完成全新升级，为企业提供更稳定、更高效、更安全的通信服务。</p><h2>核心升级内容</h2><ul><li><strong>三网合一专属通道</strong> - 与三大运营商深度合作，确保短信秒级送达</li><li><strong>智能路由优化</strong> - 根据运营商状态自动切换最优通道</li><li><strong>实时监控系统</strong> - 全链路监控，7x24小时技术支持</li><li><strong>API接口升级</strong> - 支持HTTP、HTTPS、WebSocket等多种协议</li></ul><h2>性能提升</h2><p>升级后平台处理能力提升300%，日均处理短信量突破10亿条，99.99%服务可用性保障。</p>', '智慧云通信平台全新升级，处理能力提升300%，99.99%服务可用性保障。', NULL, 1, '云通信,平台升级,企业服务', 1, 'admin', 1, 1, 526, NOW(), 0, NOW(), NOW()),

(2, '短信验证码安全性分析：如何防范短信轰炸攻击', 'sms-security', '<p>随着互联网业务的发展，短信验证码成为重要的身份验证手段，但也面临着短信轰炸攻击的威胁。</p><h2>攻击原理</h2><p>攻击者通过自动化脚本批量发送验证码请求，消耗企业短信资源，影响正常用户体验。</p><h2>防护方案</h2><ol><li><strong>图形验证码</strong> - 发送验证码前先验证图形验证码</li><li><strong>频率限制</strong> - 同一手机号60秒内只能发送一次</li><li><strong>IP限制</strong> - 同一IP每分钟最多请求10次</li><li><strong>黑名单机制</strong> - 识别恶意IP和手机号并加入黑名单</li></ol><h2>智慧云通信解决方案</h2><p>我们提供一站式安全防护，内置智能风控系统，有效识别和拦截恶意请求。</p>', '深度分析短信轰炸攻击原理及防范措施，智慧云通信提供一站式安全防护方案。', NULL, 2, '短信验证码,安全,攻击防护', 1, 'admin', 0, 0, 318, NOW(), 0, NOW(), NOW()),

(3, '金融行业短信应用解决方案', 'finance-solution', '<p>金融行业对短信服务有极高的安全性和可靠性要求，智慧云通信为金融机构提供专业解决方案。</p><h2>应用场景</h2><ul><li><strong>交易通知</strong> - 账户变动、交易确认实时通知</li><li><strong>身份验证</strong> - 登录验证、支付验证、找回密码</li><li><strong>营销推广</strong> - 理财产品推荐、活动通知</li><li><strong>风险预警</strong> - 异常交易、账户安全预警</li></ul><h2>安全保障</h2><ul><li>金融级加密传输</li><li>数据脱敏处理</li><li>合规审计日志</li><li>等保三级认证</li></ul><h2>成功案例</h2><p>已服务超过200家银行、证券、保险机构，累计发送短信超百亿条。</p>', '针对金融行业的短信应用解决方案，金融级安全保障，服务200+金融机构。', NULL, 3, '金融,解决方案,短信通知', 1, 'admin', 0, 0, 245, NOW(), 0, NOW(), NOW()),

(4, '语音验证码：比短信更安全的身份验证方式', 'voice-verification', '<p>语音验证码通过电话语音播报验证码，比短信更安全可靠。</p><h2>优势特点</h2><ul><li><strong>防窃取</strong> - 验证码通过语音播报，无法被恶意软件截取</li><li><strong>高到达率</strong> - 不受短信拦截影响，确保用户收到</li><li><strong>灵活配置</strong> - 支持自定义语音内容、语速、语种</li><li><strong>多语言支持</strong> - 支持中文、英文、日语、韩语等多语种</li></ul><h2>应用场景</h2><ol><li>账户登录验证</li><li>支付安全验证</li><li>重要操作确认</li><li>国际用户验证</li></ol><h2>技术参数</h2><p>响应时间<3秒，接通率>99%，支持大容量并发呼叫。</p>', '语音验证码通过电话播报，防窃取、高到达率，支持多语种，响应时间<3秒。', NULL, 1, '语音验证码,安全验证,多语言', 1, 'admin', 0, 0, 178, NOW(), 0, NOW(), NOW()),

(5, '国际短信服务：助力企业出海全球化', 'international-sms', '<p>智慧云通信国际短信服务覆盖全球200+国家和地区，支持多语种发送。</p><h2>覆盖范围</h2><ul><li><strong>亚太地区</strong> - 中国、日本、韩国、东南亚等</li><li><strong>欧美地区</strong> - 美国、加拿大、欧洲各国</li><li><strong>其他地区</strong> - 中东、非洲、南美等</li></ul><h2>核心优势</h2><ol><li><strong>本地化通道</strong> - 与当地运营商直连，确保送达率</li><li><strong>多语种支持</strong> - 支持中文、英文、本地语言</li><li><strong>实时状态反馈</strong> - 短信发送状态实时回调</li><li><strong>合规保障</strong> - 遵守当地通信法规</li></ol><h2>计费方式</h2><p>按发送成功条数计费，不同国家费率不同，支持预付费和后付费模式。</p>', '国际短信服务覆盖全球200+国家，本地化通道，实时状态反馈，助力企业出海。', NULL, 1, '国际短信,全球化,出海', 1, 'admin', 0, 0, 145, NOW(), 0, NOW(), NOW()),

(6, '彩信营销：图文并茂提升营销效果', 'mms-marketing', '<p>彩信营销结合图片、文字、视频等多媒体内容，提升营销转化率。</p><h2>彩信优势</h2><ul><li><strong>视觉冲击力强</strong> - 图文并茂，吸引用户注意力</li><li><strong>信息承载量大</strong> - 支持图片、视频、文字组合</li><li><strong>转化率高</strong> - 用户打开率和转化率比普通短信高3倍</li><li><strong>互动性强</strong> - 支持点击跳转、表单填写等互动功能</li></ul><h2>应用场景</h2><ol><li>新品发布宣传</li><li>促销活动通知</li><li>会员关怀服务</li><li>节日祝福问候</li></ol><h2>技术参数</h2><p>支持单条彩信最大500KB，支持JPG、PNG、GIF、MP4等格式。</p>', '彩信营销图文并茂，用户打开率比普通短信高3倍，支持图片、视频等多媒体内容。', NULL, 1, '彩信营销,多媒体,营销推广', 1, 'admin', 0, 0, 89, NOW(), 0, NOW(), NOW()),

(7, '电商行业短信应用最佳实践', 'ecommerce-solution', '<p>电商行业短信应用广泛，从用户注册到售后通知全覆盖。</p><h2>全流程应用</h2><ul><li><strong>注册验证</strong> - 手机号验证、邮箱验证</li><li><strong>订单通知</strong> - 下单、付款、发货、签收通知</li><li><strong>促销活动</strong> - 优惠券发放、限时折扣提醒</li><li><strong>售后关怀</strong> - 售后回访、满意度调查</li></ul><h2>最佳实践</h2><ol><li><strong>个性化内容</strong> - 使用用户昵称，增加亲切感</li><li><strong>合适时间发送</strong> - 避免打扰用户休息时间</li><li><strong>退订机制</strong> - 提供便捷的退订方式</li><li><strong>数据跟踪</strong> - 分析短信效果，优化内容</li></ol><h2>成功案例</h2><p>已服务多家知名电商平台，订单通知到达率99.9%。</p>', '电商行业短信应用全流程覆盖，注册验证、订单通知、促销活动、售后关怀，到达率99.9%。', NULL, 3, '电商,短信应用,最佳实践', 1, 'admin', 0, 0, 167, NOW(), 0, NOW(), NOW()),

(8, '关于智慧云通信：专注企业通信服务10年', 'about-us', '<p>智慧云通信成立于2014年，专注为企业提供专业的云通信服务。</p><h2>公司介绍</h2><p>智慧云通信是国内领先的云通信服务提供商，致力于为企业提供安全、高效、稳定的通信解决方案。</p><h2>核心团队</h2><ul><li>创始人 - 15年通信行业经验</li><li>技术团队 - 来自知名互联网公司</li><li>运营团队 - 7x24小时技术支持</li></ul><h2>资质荣誉</h2><ol><li>国家高新技术企业</li><li>ISO27001信息安全认证</li><li>等保三级认证</li><li>多项软件著作权</li></ol><h2>联系方式</h2><p>服务热线：400-888-8888<br>商务邮箱：contact@cloudcom.com</p>', '智慧云通信成立于2014年，国家高新技术企业，专注企业通信服务10年。', NULL, 4, '关于我们,公司介绍,团队', 1, 'admin', 0, 0, 124, NOW(), 0, NOW(), NOW());

-- ----------------------------
-- 模板配置数据（首页展示配置）
-- ----------------------------
INSERT INTO `template` (`template_name`, `template_value`, `description`, `page`, `deleted`, `create_time`, `update_time`) VALUES
-- Hero Banner
('heroTag', '专业云通信服务', '首页横幅标签', 'index', 0, NOW(), NOW()),
('bannerTitle', '通信能力创新，让通讯更简单', '首页横幅标题', 'index', 0, NOW(), NOW()),
('bannerDescription', '为企业提供安全、高效、稳定的一站式云通信解决方案，助力业务创新发展', '首页横幅描述', 'index', 0, NOW(), NOW()),
('bannerButton', '立即体验', '首页横幅按钮', 'index', 0, NOW(), NOW()),
('bannerUrl', '/page/channel/1', '首页横幅链接', 'index', 0, NOW(), NOW()),
('contactButton', '联系我们', '联系按钮', 'index', 0, NOW(), NOW()),

-- Features（优势展示）
('feature1title', '安全高效', '优势1标题', 'index', 0, NOW(), NOW()),
('feature1desc', '多年技术积累，平台及软件全部自主研发，通过国家知识产权认定', '优势1描述', 'index', 0, NOW(), NOW()),
('feature2title', '高性能高并发', '优势2标题', 'index', 0, NOW(), NOW()),
('feature2desc', '掌握高性能、高并发、大数据处理技术，电信级运维保障', '优势2描述', 'index', 0, NOW(), NOW()),
('feature3title', '全球覆盖', '优势3标题', 'index', 0, NOW(), NOW()),
('feature3desc', '三网合一专属通道，支持国际短信，覆盖全球主要地区', '优势3描述', 'index', 0, NOW(), NOW()),

-- Services（服务展示）
('serviceTag', '核心服务', '服务区块标签', 'index', 0, NOW(), NOW()),
('serviceTitle', '我们的服务', '服务区块标题', 'index', 0, NOW(), NOW()),
('serviceDescription', '全方位满足企业通信需求', '服务区块副标题', 'index', 0, NOW(), NOW()),
('service1title', '短信通知', '服务1标题', 'index', 0, NOW(), NOW()),
('service1description', '3秒可达，三网合一专属通道，99%到达率，支持大容量高并发', '服务1描述', 'index', 0, NOW(), NOW()),
('service1url', '/page/channel/1', '服务1链接', 'index', 0, NOW(), NOW()),
('service2title', '语音验证', '服务2标题', 'index', 0, NOW(), NOW()),
('service2description', '灵活支持业务场景，语音验证难窃取，安全有保障', '服务2描述', 'index', 0, NOW(), NOW()),
('service2url', '/page/channel/1', '服务2链接', 'index', 0, NOW(), NOW()),
('service3title', '国际短信', '服务3标题', 'index', 0, NOW(), NOW()),
('service3description', '全球主要国家和地区覆盖，支持多语种，专业本地化服务', '服务3描述', 'index', 0, NOW(), NOW()),
('service3url', '/page/channel/1', '服务3链接', 'index', 0, NOW(), NOW()),
('service4title', '彩信营销', '服务4标题', 'index', 0, NOW(), NOW()),
('service4description', '图文并茂，提升营销效果，支持大容量多媒体内容', '服务4描述', 'index', 0, NOW(), NOW()),
('service4url', '/page/channel/1', '服务4链接', 'index', 0, NOW(), NOW()),
('service5title', '数字营销', '服务5标题', 'index', 0, NOW(), NOW()),
('service5description', '手机话费充值、流量充值，数字奖励营销解决方案', '服务5描述', 'index', 0, NOW(), NOW()),
('service6title', '数据分析', '服务6标题', 'index', 0, NOW(), NOW()),
('service6description', '实时监控、数据统计、报表分析，助力业务决策', '服务6描述', 'index', 0, NOW(), NOW()),

-- News（新闻资讯）
('newsTag', '最新动态', '资讯区块标签', 'index', 0, NOW(), NOW()),
('newsTitle', '新闻中心', '资讯区块标题', 'index', 0, NOW(), NOW()),
('newsDescription', '了解最新行业资讯和公司动态', '资讯区块副标题', 'index', 0, NOW(), NOW()),

-- Partners（合作伙伴）
('partnerTag', '合作伙伴', '合作伙伴标签', 'index', 0, NOW(), NOW()),
('partnerTitle', '信赖之选', '合作伙伴标题', 'index', 0, NOW(), NOW()),
('partnerDesc', '服务超过10000家企业客户', '合作伙伴描述', 'index', 0, NOW(), NOW()),

-- Contact（联系我们）
('contactTitle', '立即开始您的云通信之旅', '联系区块标题', 'index', 0, NOW(), NOW()),
('contactDesc', '专业团队为您提供一对一服务，快速接入，即刻体验', '联系区块描述', 'index', 0, NOW(), NOW()),
('workTime', '7x24小时', '工作时间', 'index', 0, NOW(), NOW()),

-- Base（基础信息）
('companyName', '智慧云通信', '公司名称', 'base', 0, NOW(), NOW()),
('siteDescription', '专业的云通信服务提供商，为企业提供短信验证码、语音通知、国际短信等一站式通信解决方案', '站点描述', 'base', 0, NOW(), NOW()),
('siteKeywords', '云通信,短信服务,语音验证,企业服务,国际短信', 'SEO关键词', 'base', 0, NOW(), NOW()),
('location', '中国 · 杭州', '公司地址', 'base', 0, NOW(), NOW()),
('phone', '400-888-8888', '联系电话', 'base', 0, NOW(), NOW()),
('email', 'contact@cloudcom.com', '联系邮箱', 'base', 0, NOW(), NOW()),
('siteCopyright', '基于 Spring Boot 3 + MyBatis 构建', '版权信息', 'base', 0, NOW(), NOW()),
('readMore', '了解更多', '查看更多按钮', 'index', 0, NOW(), NOW());

-- ----------------------------
-- 标签数据
-- ----------------------------
INSERT INTO `tag` (`tag_name`, `tag_type`, `deleted`, `create_time`, `update_time`) VALUES
('云通信', 'article', 0, NOW(), NOW()),
('短信通知', 'article', 0, NOW(), NOW()),
('语音验证', 'article', 0, NOW(), NOW()),
('国际短信', 'article', 0, NOW(), NOW()),
('金融', 'article', 0, NOW(), NOW()),
('电商', 'article', 0, NOW(), NOW()),
('安全', 'article', 0, NOW(), NOW()),
('营销', 'article', 0, NOW(), NOW()),
('企业服务', 'article', 0, NOW(), NOW()),
('技术动态', 'article', 0, NOW(), NOW());

-- ----------------------------
-- 文章-标签关联
-- ----------------------------
INSERT INTO `article_tag` (`article_id`, `tag_id`, `create_time`) VALUES
(1, 1, NOW()), (1, 9, NOW()), (1, 10, NOW()),
(2, 2, NOW()), (2, 7, NOW()),
(3, 5, NOW()), (3, 2, NOW()),
(4, 3, NOW()), (4, 7, NOW()),
(5, 4, NOW()), (5, 9, NOW()),
(6, 8, NOW()), (6, 2, NOW()),
(7, 6, NOW()), (7, 2, NOW()),
(8, 9, NOW()), (8, 10, NOW());

-- ----------------------------
-- 示例活动数据
-- ----------------------------
INSERT INTO `activity` (`activity_name`, `activity_desc`, `start_time`, `end_time`, `create_user_id`, `create_user_name`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('2024产品发布会', '智慧云通信年度产品发布会，展示全新功能和解决方案', DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 'admin', 1, 0, NOW(), NOW()),
('技术沙龙', '云通信技术分享沙龙，探讨行业趋势和技术创新', DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY), 1, 'admin', 1, 0, NOW(), NOW());

-- ----------------------------
-- 示例表单数据
-- ----------------------------
INSERT INTO `form` (`form_name`, `form_type`, `create_user_id`, `create_user_name`, `form_desc`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('产品试用申请', 1, 1, 'admin', '申请试用智慧云通信产品和服务', 1, 0, NOW(), NOW()),
('商务合作咨询', 1, 1, 'admin', '商务合作咨询，洽谈合作事宜', 1, 0, NOW(), NOW());

-- ----------------------------
-- 表单字段数据
-- ----------------------------
INSERT INTO `form_item` (`form_id`, `item_name`, `item_type`, `default_value`, `item_tip`, `placeholder`, `required`, `sort`, `deleted`, `create_time`, `update_time`) VALUES
-- 产品试用申请表单字段
(1, '公司名称', 'input', NULL, '请输入公司全称', '请输入公司名称', 1, 1, 0, NOW(), NOW()),
(1, '联系人', 'input', NULL, '请输入联系人姓名', '请输入联系人', 1, 2, 0, NOW(), NOW()),
(1, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 3, 0, NOW(), NOW()),
(1, '邮箱', 'input', NULL, '请输入公司邮箱', '请输入邮箱', 1, 4, 0, NOW(), NOW()),
(1, '预计发送量', 'select', '1-10万条/月', NULL, '请选择预计发送量', 1, 5, 0, NOW(), NOW()),
(1, '使用场景', 'textarea', NULL, '请描述您的使用场景和需求', '请输入使用场景', 1, 6, 0, NOW(), NOW()),

-- 商务合作咨询表单字段
(2, '公司名称', 'input', NULL, '请输入公司全称', '请输入公司名称', 1, 1, 0, NOW(), NOW()),
(2, '联系人', 'input', NULL, '请输入联系人姓名', '请输入联系人', 1, 2, 0, NOW(), NOW()),
(2, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 3, 0, NOW(), NOW()),
(2, '合作类型', 'select', '短信服务', NULL, '请选择合作类型', 1, 4, 0, NOW(), NOW()),
(2, '合作需求', 'textarea', NULL, '请描述您的合作需求', '请输入合作需求', 1, 5, 0, NOW(), NOW());

-- 更新表单字段的选项值
UPDATE `form_item` SET `default_value` = '1-10万条/月;10-50万条/月;50-100万条/月;100万条以上/月' WHERE `form_id` = 1 AND `item_name` = '预计发送量';
UPDATE `form_item` SET `default_value` = '短信服务;语音服务;国际短信;彩信服务;定制开发' WHERE `form_id` = 2 AND `item_name` = '合作类型';