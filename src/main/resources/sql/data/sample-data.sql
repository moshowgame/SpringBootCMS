-- ============================================================
-- SpringBootCMS 示例数据 - 广东省数字政务协会风格
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
-- 频道数据（协会主要栏目）
-- ----------------------------
INSERT INTO `channel` (`channel_id`, `channel_name`, `slug`, `parent_channel_id`, `icon`, `cover_image`, `description`, `channel_type`, `status`, `seq`, `deleted`, `create_time`, `update_time`) VALUES
(1, '政策研究', 'policy-research', 0, 'bi-book', NULL, '政策宣传贯彻、调查研究、政策法规建议', 1, 1, 1, 0, NOW(), NOW()),
(2, '标准规范', 'standards', 0, 'bi-file-earmark-text', NULL, '团体标准制定与推广，政务信息化标准建设', 1, 1, 2, 0, NOW(), NOW()),
(3, '行业活动', 'activities', 0, 'bi-calendar-event', NULL, '数字政务发展论坛、智慧城市发展大会等品牌活动', 1, 1, 3, 0, NOW(), NOW()),
(4, '评估咨询', 'evaluation', 0, 'bi-clipboard-data', NULL, '项目效果评估、优秀案例征集评审、造价指导', 1, 1, 4, 0, NOW(), NOW()),
(5, '人才培养', 'training', 0, 'bi-mortarboard', NULL, '专业培训、技能认证、职称评审服务', 1, 1, 5, 0, NOW(), NOW()),
(6, '关于协会', 'about', 0, 'bi-building', NULL, '协会介绍、组织架构、联系方式', 2, 1, 6, 0, NOW(), NOW());

-- ----------------------------
-- 文章数据（协会业务相关内容）
-- ----------------------------
INSERT INTO `article` (`article_id`, `title`, `slug`, `content`, `summary`, `cover_image`, `channel_id`, `keyword`, `create_user_id`, `create_user_name`, `status`, `is_top`, `view_count`, `publish_time`, `deleted`, `create_time`, `update_time`) VALUES
(1, '广东省数字政务协会2025年度工作会议顺利召开', 'annual-meeting-2025', '<p>2025年1月，广东省数字政务协会年度工作会议在广州顺利召开。会议总结了2024年工作成果，部署了2025年重点任务。</p><h2>会议主要内容</h2><ul><li><strong>工作报告</strong> - 会长作2024年度工作报告，回顾协会在政策研究、标准制定、行业交流等方面取得的成效</li><li><strong>标准建设</strong> - 《政务信息化项目监理服务规范》获评"标准实施效果卓越组织"称号</li><li><strong>年度计划</strong> - 部署2025年重点工作，包括新标准立项、品牌活动筹备、人才培养计划等</li><li><strong>表彰先进</strong> - 对2024年度优秀会员单位、先进个人进行表彰</li></ul><h2>2024年工作亮点</h2><p>全年发布团体标准4项，举办品牌活动12场，培训学员超5000人次，服务会员单位超300家。</p>', '广东省数字政务协会2025年度工作会议顺利召开，部署全年重点任务。', NULL, 1, '数字政务,协会动态,工作会议', 1, 'admin', 1, 1, 1256, NOW(), 0, NOW(), NOW()),

(2, '《政务服务大厅人工智能数字人应用规范》团体标准正式发布', 'digital-human-standard', '<p>广东省数字政务协会发布的《政务服务大厅人工智能数字人应用规范》（T/DGAG 042—2025）正式实施，为政务服务大厅数字人应用提供标准指引。</p><h2>标准主要内容</h2><ul><li><strong>技术要求</strong> - 规定数字人的外观设计、语音交互、动作表现等技术参数</li><li><strong>服务场景</strong> - 明确数字人在政务咨询、业务办理、政策解读等场景的应用规范</li><li><strong>安全保障</strong> - 要求数据隐私保护、内容审核机制、应急响应预案</li><li><strong>效果评估</strong> - 建立服务质量评价指标体系，定期开展效果评估</li></ul><h2>推广意义</h2><p>该标准的发布填补了政务服务领域数字人应用规范的空白，为各地政务服务大厅数字化转型提供技术支撑和规范指引。</p>', '《政务服务大厅人工智能数字人应用规范》团体标准正式发布，填补行业空白。', NULL, 2, '团体标准,数字人,政务服务', 1, 'admin', 1, 0, 856, NOW(), 0, NOW(), NOW()),

(3, '第九届广东数字政务发展论坛成功举办', 'digital-gov-forum-9th', '<p>第九届广东数字政务发展论坛于2024年12月在广州成功举办，来自政府部门、企业、高校、科研机构的300余位代表参会。</p><h2>论坛主题</h2><p>本届论坛以"数字政府建设新格局·智慧城市发展新路径"为主题，探讨数字政务发展趋势和技术创新应用。</p><h2>主要议题</h2><ul><li><strong>数字政府建设</strong> - 省级数字政府建设经验分享与路径探讨</li><li><strong>智慧城市</strong> - 新型智慧城市建设模式与实践案例</li><li><strong>数据治理</strong> - 公共数据开放共享与数据安全保障</li><li><strong>人工智能</strong> - AI技术在政务服务中的应用探索</li></ul><h2>成果发布</h2><p>论坛期间发布了《广东数字政府建设产业发展研究报告》，为产业发展提供智力支持。</p>', '第九届广东数字政务发展论坛成功举办，300余位代表参会探讨数字政务发展。', NULL, 3, '数字政务论坛,行业活动,智慧城市', 1, 'admin', 0, 0, 678, NOW(), 0, NOW(), NOW()),

(4, '2024年广东省政务信息化第三方服务优秀案例评选结果公布', 'excellent-cases-2024', '<p>广东省数字政务协会组织开展的2024年"广东政务信息化第三方服务优秀案例"征集与评审活动圆满完成，评选出优秀案例25个。</p><h2>评选范围</h2><ul><li><strong>系统集成</strong> - 政务信息系统集成服务优秀案例</li><li><strong>运维服务</strong> - 政务云平台运维服务优秀案例</li><li><strong>安全服务</strong> - 政务网络安全服务优秀案例</li><li><strong>咨询服务</strong> - 政务信息化咨询服务优秀案例</li></ul><h2>评审标准</h2><ol><li>技术创新性 - 采用先进技术，具有创新亮点</li><li>实施规范性 - 符合标准规范，流程规范完整</li><li>效果显著性 - 项目成效明显，用户满意度高</li><li>推广价值性 - 具有推广价值，可复制性强</li></ol><h2>名单公布</h2><p>优秀案例名单已在协会官网公布，案例经验将在行业活动中进行推广分享。</p>', '2024年政务信息化第三方服务优秀案例评选出25个优秀案例，名单已公布。', NULL, 4, '优秀案例,评估评审,政务信息化', 1, 'admin', 0, 0, 534, NOW(), 0, NOW(), NOW()),

(5, '软件工程造价师培训班开班报名', 'software-cost-training', '<p>广东省数字政务协会组织开展"软件工程造价师"专业培训，为政务信息化项目造价管理提供专业人才培养。</p><h2>培训内容</h2><ul><li><strong>造价理论</strong> - 软件工程造价基本理论与方法</li><li><strong>估算技术</strong> - 功能点估算、敏捷估算等技术方法</li><li><strong>成本控制</strong> - 项目成本控制与预算管理实践</li><li><strong>案例分析</strong> - 政务信息化项目造价案例分析</li></ul><h2>培训安排</h2><ol><li>培训时间：2025年3月-4月（周末班）</li><li>培训地点：广州市天河区</li><li>培训费用：会员单位优惠价</li><li>证书认证：培训合格颁发软件工程造价师证书</li></ol><h2>报名方式</h2><p>可通过协会官网在线报名或联系培训部咨询。</p>', '软件工程造价师培训班开班报名，为政务信息化项目造价管理培养专业人才。', NULL, 5, '专业培训,造价师,人才培养', 1, 'admin', 0, 0, 389, NOW(), 0, NOW(), NOW()),

(6, '《政务信息化项目监理服务规范》获评"标准实施效果卓越组织"称号', 'standard-honor-2025', '<p>2025年，广东省数字政务协会因《政务信息化项目监理服务规范》（T/EGAG 010—2022）推广成效显著，获评广东省"标准实施效果卓越组织"称号。</p><h2>标准简介</h2><p>《政务信息化项目监理服务规范》是协会发布的首项团体标准，规范了政务信息化项目监理的服务流程、技术要求、质量控制等内容。</p><h2>实施成效</h2><ul><li><strong>应用广泛</strong> - 已在全省100+政务信息化项目中应用</li><li><strong>效果显著</strong> - 项目监理规范率提升60%，质量投诉下降40%</li><li><strong>行业认可</strong> - 获得监理机构、建设单位广泛认可</li><li><strong>政府采纳</strong> - 多地政府将本标准纳入采购要求</li></ul><h2>荣誉意义</h2><p>此次获评体现了协会在标准制定与推广方面的专业能力和社会认可度，将激励协会继续推进团体标准建设工作。</p>', '《政务信息化项目监理服务规范》推广成效显著，协会获评"标准实施效果卓越组织"称号。', NULL, 2, '团体标准,监理规范,荣誉表彰', 1, 'admin', 0, 0, 712, NOW(), 0, NOW(), NOW()),

(7, '广东省大数据工程技术人才职称评审服务指南', 'big-data-title-review', '<p>广东省数字政务协会负责广东省大数据工程技术人才职称评审的政策解读、申报咨询与考核认定等相关服务工作。</p><h2>职称层级</h2><ul><li><strong>初级职称</strong> - 大数据工程技术员、助理工程师</li><li><strong>中级职称</strong> - 大数据工程师</li><li><strong>高级职称</strong> - 大数据高级工程师</li></ul><h2>评审条件</h2><ol><li>学历要求 - 大专及以上学历，相关专业</li><li>工作年限 - 按职称层级要求相应工作年限</li><li>专业能力 - 具备大数据技术相关专业知识</li><li>业绩成果 - 有相关项目经验或研究成果</li></ol><h2>服务内容</h2><p>协会提供政策解读、申报材料指导、考核认定咨询等服务，助力专业技术人才顺利通过职称评审。</p>', '广东省大数据工程技术人才职称评审服务指南，提供政策解读与申报咨询。', NULL, 5, '职称评审,大数据,人才服务', 1, 'admin', 0, 0, 456, NOW(), 0, NOW(), NOW()),

(8, '关于广东省数字政务协会', 'about-association', '<p>广东省数字政务协会（原广东省电子政务协会）是广东省数字政务领域的专业性社会团体，致力于服务政府决策、促进行业规范发展、推动数字政府与智慧城市建设。</p><h2>协会宗旨</h2><p>宣传贯彻国家和省电子政务（数字政务）工作的政策方针，开展调查研究，向主管部门反映社会各界的意见和建议，为政府制定政策法规和标准规范提供参考意见。</p><h2>主要业务</h2><ul><li><strong>政策研究</strong> - 开展数字政务领域政策研究，为政府决策提供支撑</li><li><strong>标准制定</strong> - 组织制定并推广政务信息化、数字政府相关团体标准</li><li><strong>行业交流</strong> - 举办论坛、讲座等活动，促进产学研用交流合作</li><li><strong>评估咨询</strong> - 开展项目效果评估、优秀案例征集评审</li><li><strong>人才培养</strong> - 组织专业培训、技能认证、职称评审服务</li><li><strong>产业协同</strong> - 密切政府、企业、高校与科研机构的联系</li></ul><h2>联系方式</h2><p>地址：广东省广州市天河区<br>电话：020-12345678<br>邮箱：info@egag.org.cn</p>', '广东省数字政务协会是数字政务领域专业性社会团体，服务政府决策，推动数字政府建设。', NULL, 6, '关于协会,协会介绍,数字政务', 1, 'admin', 0, 0, 324, NOW(), 0, NOW(), NOW());

-- ----------------------------
-- 模板配置数据（首页展示配置 - 协会风格）
-- ----------------------------
INSERT INTO `template` (`template_name`, `template_value`, `description`, `page`, `deleted`, `create_time`, `update_time`) VALUES
-- Hero Banner
('heroTag', '广东省数字政务领域专业性社会团体', '首页横幅标签', 'index', 0, NOW(), NOW()),
('bannerTitle', '服务政府决策 · 推动数字政府建设', '首页横幅标题', 'index', 0, NOW(), NOW()),
('bannerDescription', '广东省数字政务协会致力于政策研究、标准制定、行业交流、评估咨询、人才培养及产业协同，促进行业规范发展', '首页横幅描述', 'index', 0, NOW(), NOW()),
('bannerButton', '了解更多', '首页横幅按钮', 'index', 0, NOW(), NOW()),
('bannerUrl', '/page/channel/6', '首页横幅链接', 'index', 0, NOW(), NOW()),
('contactButton', '加入协会', '联系按钮', 'index', 0, NOW(), NOW()),

-- Features（优势展示）
('feature1title', '政策研究', '优势1标题', 'index', 0, NOW(), NOW()),
('feature1desc', '宣传贯彻政策方针，开展调查研究，为政府制定政策法规和标准规范提供参考意见', '优势1描述', 'index', 0, NOW(), NOW()),
('feature2title', '标准制定', '优势2标题', 'index', 0, NOW(), NOW()),
('feature2desc', '组织制定并推广政务信息化、数字政府相关团体标准，是广东省数字政务领域团体标准建设的重要力量', '优势2描述', 'index', 0, NOW(), NOW()),
('feature3title', '行业交流', '优势3标题', 'index', 0, NOW(), NOW()),
('feature3desc', '举办数字政务发展论坛、智慧城市发展大会等品牌活动，搭建行业交流平台', '优势3描述', 'index', 0, NOW(), NOW()),

-- Services（服务展示）
('serviceTag', '核心服务', '服务区块标签', 'index', 0, NOW(), NOW()),
('serviceTitle', '协会服务', '服务区块标题', 'index', 0, NOW(), NOW()),
('serviceDescription', '全方位服务数字政务发展', '服务区块副标题', 'index', 0, NOW(), NOW()),
('service1title', '政策研究', '服务1标题', 'index', 0, NOW(), NOW()),
('service1description', '开展调查研究，向主管部门反映意见和建议，为政府决策提供参考', '服务1描述', 'index', 0, NOW(), NOW()),
('service1url', '/page/channel/1', '服务1链接', 'index', 0, NOW(), NOW()),
('service2title', '标准规范', '服务2标题', 'index', 0, NOW(), NOW()),
('service2description', '制定并推广政务信息化、数字政府相关团体标准，已发布多项标准', '服务2描述', 'index', 0, NOW(), NOW()),
('service2url', '/page/channel/2', '服务2链接', 'index', 0, NOW(), NOW()),
('service3title', '行业活动', '服务3标题', 'index', 0, NOW(), NOW()),
('service3description', '举办数字政务发展论坛、智慧城市发展大会等品牌活动，促进交流合作', '服务3描述', 'index', 0, NOW(), NOW()),
('service3url', '/page/channel/3', '服务3链接', 'index', 0, NOW(), NOW()),
('service4title', '评估咨询', '服务4标题', 'index', 0, NOW(), NOW()),
('service4description', '开展项目效果评估、优秀案例征集评审、造价指导等服务', '服务4描述', 'index', 0, NOW(), NOW()),
('service4url', '/page/channel/4', '服务4链接', 'index', 0, NOW(), NOW()),
('service5title', '人才培养', '服务5标题', 'index', 0, NOW(), NOW()),
('service5description', '组织专业培训、技能认证、职称评审服务，培养数字政务专业人才', '服务5描述', 'index', 0, NOW(), NOW()),
('service5url', '/page/channel/5', '服务5链接', 'index', 0, NOW(), NOW()),
('service6title', '产业协同', '服务6标题', 'index', 0, NOW(), NOW()),
('service6description', '密切政府、企业、高校与科研机构的联系，推动合作项目落地', '服务6描述', 'index', 0, NOW(), NOW()),
('service6url', '/page/channel/6', '服务6链接', 'index', 0, NOW(), NOW()),

-- News（新闻资讯）
('newsTag', '协会动态', '资讯区块标签', 'index', 0, NOW(), NOW()),
('newsTitle', '最新动态', '资讯区块标题', 'index', 0, NOW(), NOW()),
('newsDescription', '了解协会最新活动和行业资讯', '资讯区块副标题', 'index', 0, NOW(), NOW()),

-- Partners（合作伙伴）
('partnerTag', '会员单位', '合作伙伴标签', 'index', 0, NOW(), NOW()),
('partnerTitle', '会员风采', '合作伙伴标题', 'index', 0, NOW(), NOW()),
('partnerDesc', '服务超过300家会员单位', '合作伙伴描述', 'index', 0, NOW(), NOW()),

-- Contact（联系我们）
('contactTitle', '加入广东省数字政务协会', '联系区块标题', 'index', 0, NOW(), NOW()),
('contactDesc', '共同推动数字政务发展，共建智慧美好未来', '联系区块描述', 'index', 0, NOW(), NOW()),
('workTime', '周一至周五 9:00-17:00', '工作时间', 'index', 0, NOW(), NOW()),

-- Base（基础信息）
('companyName', '广东省数字政务协会', '协会名称', 'base', 0, NOW(), NOW()),
('siteDescription', '广东省数字政务协会（原广东省电子政务协会）是广东省数字政务领域的专业性社会团体，致力于政策研究、标准制定、行业交流、评估咨询、人才培养及产业协同', '站点描述', 'base', 0, NOW(), NOW()),
('siteKeywords', '数字政务,电子政务,团体标准,政策研究,人才培养,智慧城市,数字政府', 'SEO关键词', 'base', 0, NOW(), NOW()),
('location', '中国 · 广东 · 广州', '协会地址', 'base', 0, NOW(), NOW()),
('phone', '020-12345678', '联系电话', 'base', 0, NOW(), NOW()),
('email', 'info@egag.org.cn', '联系邮箱', 'base', 0, NOW(), NOW()),
('siteCopyright', '广东省数字政务协会 版权所有', '版权信息', 'base', 0, NOW(), NOW()),
('readMore', '查看详情', '查看更多按钮', 'index', 0, NOW(), NOW());

-- ----------------------------
-- 标签数据（协会业务相关）
-- ----------------------------
INSERT INTO `tag` (`tag_name`, `tag_type`, `deleted`, `create_time`, `update_time`) VALUES
('数字政务', 'article', 0, NOW(), NOW()),
('团体标准', 'article', 0, NOW(), NOW()),
('政策研究', 'article', 0, NOW(), NOW()),
('行业活动', 'article', 0, NOW(), NOW()),
('人才培养', 'article', 0, NOW(), NOW()),
('智慧城市', 'article', 0, NOW(), NOW()),
('评估评审', 'article', 0, NOW(), NOW()),
('职称评审', 'article', 0, NOW(), NOW()),
('政务服务', 'article', 0, NOW(), NOW()),
('协会动态', 'article', 0, NOW(), NOW());

-- ----------------------------
-- 文章-标签关联
-- ----------------------------
INSERT INTO `article_tag` (`article_id`, `tag_id`, `create_time`) VALUES
(1, 10, NOW()), (1, 1, NOW()),
(2, 2, NOW()), (2, 9, NOW()),
(3, 4, NOW()), (3, 6, NOW()), (3, 1, NOW()),
(4, 7, NOW()), (4, 9, NOW()),
(5, 5, NOW()), (5, 8, NOW()),
(6, 2, NOW()), (6, 10, NOW()),
(7, 5, NOW()), (7, 8, NOW()),
(8, 10, NOW()), (8, 1, NOW());

-- ----------------------------
-- 示例活动数据（协会品牌活动）
-- ----------------------------
INSERT INTO `activity` (`activity_name`, `activity_desc`, `start_time`, `end_time`, `create_user_id`, `create_user_name`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('第十届广东数字政务发展论坛', '第十届广东数字政务发展论坛，探讨数字政府建设新格局与智慧城市发展新路径', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_ADD(NOW(), INTERVAL 30 DAY), 1, 'admin', 1, 0, NOW(), NOW()),
('第八届广东省数字政务与新型智慧城市发展大会', '第八届广东省数字政务与新型智慧城市发展大会，展示最新成果与技术应用', DATE_ADD(NOW(), INTERVAL 60 DAY), DATE_ADD(NOW(), INTERVAL 60 DAY), 1, 'admin', 1, 0, NOW(), NOW()),
('软件工程造价师培训班', '软件工程造价师专业培训，为政务信息化项目造价管理培养专业人才', DATE_ADD(NOW(), INTERVAL 15 DAY), DATE_ADD(NOW(), INTERVAL 45 DAY), 1, 'admin', 1, 0, NOW(), NOW()),
('政务信息化标准宣贯培训', '政务信息化团体标准宣贯培训，讲解标准内容与应用方法', DATE_ADD(NOW(), INTERVAL 20 DAY), DATE_ADD(NOW(), INTERVAL 20 DAY), 1, 'admin', 1, 0, NOW(), NOW());

-- ----------------------------
-- 示例表单数据（协会相关表单）
-- ----------------------------
INSERT INTO `form` (`form_name`, `form_type`, `create_user_id`, `create_user_name`, `form_desc`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('会员入会申请', 1, 1, 'admin', '申请加入广东省数字政务协会，成为会员单位', 1, 0, NOW(), NOW()),
('活动报名', 1, 1, 'admin', '报名参加协会举办的各项活动', 1, 0, NOW(), NOW()),
('培训报名', 1, 1, 'admin', '报名参加协会组织的专业培训', 1, 0, NOW(), NOW());

-- ----------------------------
-- 表单字段数据
-- ----------------------------
INSERT INTO `form_item` (`form_id`, `item_name`, `item_type`, `default_value`, `item_tip`, `placeholder`, `required`, `sort`, `deleted`, `create_time`, `update_time`) VALUES
-- 会员入会申请表单字段
(1, '单位名称', 'input', NULL, '请输入申请单位全称', '请输入单位名称', 1, 1, 0, NOW(), NOW()),
(1, '单位类型', 'select', '企业单位', NULL, '请选择单位类型', 1, 2, 0, NOW(), NOW()),
(1, '联系人', 'input', NULL, '请输入联系人姓名', '请输入联系人', 1, 3, 0, NOW(), NOW()),
(1, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 4, 0, NOW(), NOW()),
(1, '邮箱', 'input', NULL, '请输入单位邮箱', '请输入邮箱', 1, 5, 0, NOW(), NOW()),
(1, '申请说明', 'textarea', NULL, '请简述申请理由和业务领域', '请输入申请说明', 1, 6, 0, NOW(), NOW()),

-- 活动报名表单字段
(2, '姓名', 'input', NULL, '请输入报名人姓名', '请输入姓名', 1, 1, 0, NOW(), NOW()),
(2, '单位', 'input', NULL, '请输入所属单位', '请输入单位', 1, 2, 0, NOW(), NOW()),
(2, '职务', 'input', NULL, '请输入职务', '请输入职务', 1, 3, 0, NOW(), NOW()),
(2, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 4, 0, NOW(), NOW()),
(2, '邮箱', 'input', NULL, '请输入邮箱地址', '请输入邮箱', 1, 5, 0, NOW(), NOW()),
(2, '是否会员', 'select', '是', NULL, '是否协会会员', 0, 6, 0, NOW(), NOW()),

-- 培训报名表单字段
(3, '姓名', 'input', NULL, '请输入报名人姓名', '请输入姓名', 1, 1, 0, NOW(), NOW()),
(3, '单位', 'input', NULL, '请输入所属单位', '请输入单位', 1, 2, 0, NOW(), NOW()),
(3, '联系电话', 'input', NULL, '请输入联系电话', '请输入电话', 1, 3, 0, NOW(), NOW()),
(3, '邮箱', 'input', NULL, '请输入邮箱地址', '请输入邮箱', 1, 4, 0, NOW(), NOW()),
(3, '培训课程', 'select', '软件工程造价师', NULL, '请选择培训课程', 1, 5, 0, NOW(), NOW()),
(3, '备注说明', 'textarea', NULL, '如有特殊需求请备注说明', '请输入备注', 0, 6, 0, NOW(), NOW());

-- 更新表单字段的选项值
UPDATE `form_item` SET `default_value` = '企业单位;事业单位;政府部门;高校科研机构;其他' WHERE `form_id` = 1 AND `item_name` = '单位类型';
UPDATE `form_item` SET `default_value` = '是;否' WHERE `form_id` = 2 AND `item_name` = '是否会员';
UPDATE `form_item` SET `default_value` = '软件工程造价师;大数据工程师职称评审;政务信息化标准宣贯;其他培训' WHERE `form_id` = 3 AND `item_name` = '培训课程';