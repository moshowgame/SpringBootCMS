/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : springbootcms

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 16/07/2020 23:32:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `activity_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `activity_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `activity_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动简介',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '活动结束时间',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户姓名',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '活动状态:0未发布 1已发布',
  PRIMARY KEY (`activity_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (3, '共同抗击疫情，健康中华IT讲座', '病毒无情，人有情。防护病毒，防护IT系统，人人有责。', '2020-05-09 16:50:13', '2020-06-23 11:56:43', '2020-05-13 09:00:00', '2020-07-13 12:00:00', 1, '系统管理员', 0);
INSERT INTO `activity` VALUES (4, '新版系统上线庆典', 'new version arrived! update it please!', '2020-06-21 10:25:48', '2020-06-21 10:25:51', '2020-06-20 10:00:00', '2020-10-01 10:00:00', 1, '系统管理员', 1);

-- ----------------------------
-- Table structure for activity_sign
-- ----------------------------
DROP TABLE IF EXISTS `activity_sign`;
CREATE TABLE `activity_sign`  (
  `sign_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '签到id',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `sign_time` timestamp(0) NULL DEFAULT NULL COMMENT '签到时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sign_type` int(255) NULL DEFAULT 1 COMMENT '签到类型:1签到 2请假',
  `leave_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假理由',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`sign_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of activity_sign
-- ----------------------------
INSERT INTO `activity_sign` VALUES (9, 4, NULL, '2020-06-21 11:08:36', NULL, 'Moshow K ZHENG', 1, '', '18502072277', 'WOMS');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `article_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文章标题',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '频道ID',
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关键字，逗号分隔',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '发布状态:1发布0未发布',
  `is_top` tinyint(1) NULL DEFAULT NULL COMMENT '是否置顶:0否1是',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`article_id`) USING BTREE,
  FULLTEXT INDEX `idx_full_keyword`(`keyword`),
  FULLTEXT INDEX `idx_full_content`(`content`)
) ENGINE = MyISAM AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 'springboot给力', 1, '超级管理员', '<header><nav id=\"nav\"><pre class=\"prettyprint highlight prettyprinted\"><code class=\"language-text\" data-lang=\"text\"><span class=\"pln\">plugins </span><span class=\"pun\">{</span><span class=\"pln\">\n	id </span><span class=\"str\">\'org.springframework.boot\'</span><span class=\"pln\"> version </span><span class=\"str\">\'2.2.0.RELEASE\'</span><span class=\"pln\">\n	id </span><span class=\"str\">\'io.spring.dependency-management\'</span><span class=\"pln\"> version </span><span class=\"str\">\'1.0.8.RELEASE\'</span><span class=\"pln\">\n	id </span><span class=\"str\">\'java\'</span><span class=\"pln\">\n</span><span class=\"pun\">}</span><span class=\"pln\">\n\n</span><span class=\"kwd\">group</span><span class=\"pln\"> </span><span class=\"pun\">=</span><span class=\"pln\"> </span><span class=\"str\">\'com.example\'</span><span class=\"pln\">\nversion </span><span class=\"pun\">=</span><span class=\"pln\"> </span><span class=\"str\">\'0.0.1-SNAPSHOT\'</span><span class=\"pln\">\nsourceCompatibility </span><span class=\"pun\">=</span><span class=\"pln\"> </span><span class=\"str\">\'1.8\'</span><span class=\"pln\">\n\nrepositories </span><span class=\"pun\">{</span><span class=\"pln\">\n	mavenCentral</span><span class=\"pun\">()</span><span class=\"pln\">\n</span><span class=\"pun\">}</span><span class=\"pln\">\n\ndependencies </span><span class=\"pun\">{</span><span class=\"pln\">\n	implementation </span><span class=\"str\">\'org.springframework.boot:spring-boot-starter-web\'</span><span class=\"pln\">\n	testImplementation</span><span class=\"pun\">(</span><span class=\"str\">\'org.springframework.boot:spring-boot-starter-test\'</span><span class=\"pun\">)</span><span class=\"pln\"> </span><span class=\"pun\">{</span><span class=\"pln\">\n		exclude </span><span class=\"kwd\">group</span><span class=\"pun\">:</span><span class=\"pln\"> </span><span class=\"str\">\'org.junit.vintage\'</span><span class=\"pun\">,</span><span class=\"pln\"> </span><span class=\"kwd\">module</span><span class=\"pun\">:</span><span class=\"pln\"> </span><span class=\"str\">\'junit-vintage-engine\'</span><span class=\"pln\">\n	</span><span class=\"pun\">}</span><span class=\"pln\">\n</span><span class=\"pun\">}</span><span class=\"pln\">\n\ntest </span><span class=\"pun\">{</span><span class=\"pln\">\n	useJUnitPlatform</span><span class=\"pun\">()</span><span class=\"pln\">\n</span><span class=\"pun\">}</span></code></pre></nav></header>', 1, 'springboot,spring,java', '2020-05-26 22:01:18', '2020-06-06 23:25:11', 1, 0, 'plugins { 	id \'org.springframework.boot\' version \'2.2.0.RELEASE\' 	id \'io.spring.dependency-management\' version \'1.0.8.RELEASE\' 	id \'java\' }');
INSERT INTO `article` VALUES (2, 'Starting with Spring Initializr', 1, '超级管理员', '<h2 id=\"scratch\">Starting with Spring Initializr</h2><div class=\"sectionbody\"><div class=\"paragraph\"><p>For all Spring applications, you should start with the&nbsp;<a href=\"https://start.spring.io/\">Spring Initializr</a>. The Initializr offers a fast way to pull in all the dependencies you need for an application and does a lot of the setup for you. This example needs only the Spring Web dependency. The following image shows the Initializr set up for this sample project:</p></div><div class=\"imageblock\"><div class=\"content\"><img src=\"https://spring.io/guides/gs/rest-service/images/initializr.png\" alt=\"initializr\"></div></div><div class=\"admonitionblock note\"><table class=\"layui-table\"><tbody><tr><td class=\"icon\"><span class=\"fa icon-note\" title=\"Note\"></span></td><td class=\"content\"><p>The preceding image shows the Initializr with Maven chosen as the build tool. You can also use Gradle. It also shows values of&nbsp;<code>com.example</code>&nbsp;and&nbsp;<code>rest-service</code>&nbsp;as the Group and Artifact, respectively. You will use those values throughout the rest of this sample.</p><p><img src=\"@@@1590502608626微信图片_20191103220440.jpg\" alt=\"1590502608626微信图片_20191103220440.jpg\"><br></p></td></tr></tbody></table></div></div>', 1, 'Starting,Initializr', '2020-05-26 22:04:14', '2020-06-06 23:25:19', 0, 0, 'Starting with Spring Initializr');
INSERT INTO `article` VALUES (3, '电子政务简讯第238期：本周简报', 1, '超级管理员', '<p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">广州：番禺区开办企业</font>“<font face=\"宋体\">一网通办，一窗通取</font><font face=\"Calibri\">”</font><font face=\"宋体\">经验走前列</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">近日，广州市开办企业</font>“<font face=\"宋体\">一网通办、一窗通取</font><font face=\"Calibri\">”</font><font face=\"宋体\">推进会召开，番禺作为该项工作的全市示范区，在会上播放视频作经验介绍。</font></span><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">　　番禺区作为开办企业</font>“<font face=\"宋体\">一网通办、一窗通取</font><font face=\"Calibri\">”</font><font face=\"宋体\">试点做了大量的工作。一是印发系列配套改革文件，确保试点工作有序开展；二是多次与职能部门召开工作协调会，简化完善各流程环节；三是在大厅设置开办企业专区、增配设备、导办人员等，确保业务顺利开展。</font></span><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">自然资源标准化信息服务平台上线</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span>5<font face=\"宋体\">月</font><font face=\"Calibri\">28</font><font face=\"宋体\">日，自然资源标准化信息服务平台正式上线运行，可为公众提供</font><font face=\"Calibri\">1000</font><font face=\"宋体\">余项现行自然资源国家标准和行业标准的全文在线阅读。企事业单位和社会公众可通过访问网站</font><font face=\"Calibri\">www.nrsis.org.cn</font><font face=\"宋体\">或从自然资源部网站</font><font face=\"Calibri\">“</font><font face=\"宋体\">标准规范</font><font face=\"Calibri\">”</font><font face=\"宋体\">栏目入口进行免费查阅。</font></span><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">深圳：龙岗全力打造全国营商环境高地和产业发展强区</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span>2020<font face=\"宋体\">年</font><font face=\"Calibri\">5</font><font face=\"宋体\">月</font><font face=\"Calibri\">20</font><font face=\"宋体\">日，深圳市龙岗区印发《龙岗区优化营商环境改革三年行动方案（</font><font face=\"Calibri\">2020</font><font face=\"宋体\">－</font><font face=\"Calibri\">2022</font><font face=\"宋体\">）》，提出到</font><font face=\"Calibri\">2022</font><font face=\"宋体\">年成为</font><font face=\"Calibri\">“</font><font face=\"宋体\">全国营商环境高地和产业发展强区</font><font face=\"Calibri\">”</font><font face=\"宋体\">。</font></span><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">　　据了解，《三年行动方案》明确龙岗区以打造国际一流营商环境作为推进深圳建设中国特色社会主义先行示范区的重要举措，结合龙岗营商环境现状，梳理形成实施审批提速、加大模式创新、优化执法监管、强化要素支撑和提升城市品质五大方面任务共计</font>27<font face=\"宋体\">项重点举措。</font></span><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">多部门联合发布《网络安全审查办法》正式实施</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">由国家互联网信息办公室、国家发展改革委员会、工业和信息化部、公安部等</font>12<font face=\"宋体\">个部门联合发布的《网络安全审查办法》，从</font><font face=\"Calibri\">6</font><font face=\"宋体\">月</font><font face=\"Calibri\">1</font><font face=\"宋体\">日起正式实施。</font></span><span><o:p></o:p></span></p><p class=\"p\"><span>&nbsp; <font face=\"宋体\">根据《网络安全审查办法》，网络安全审查，重点评估了关键信息基础设施运营者采购网络产品和服务，可能带来的国家安全风险，主要考虑以下因素：产品和服务的关键数据是否有被泄露、控制、毁坏的可能；产品和服务的安全性、开放性、透明性、来源的多样性，供应渠道的可靠性以及因为政治、外交、贸易等因素导致供应中断的风险；产品和服务提供者遵守中国法律、行政法规、部门规章情况；其他可能危害关键信息基础设施安全和国家安全的因素。</font></span><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">《信息技术</font> <font face=\"宋体\">大数据</font> <font face=\"宋体\">大数据系统基本要求》等</font>12<font face=\"宋体\">项大数据国家标准正式发布</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">近期，由全国信标委大数据标准工作组组织研制的《信息技术</font>&nbsp;<font face=\"宋体\">大数据</font><font face=\"Calibri\">&nbsp;</font><font face=\"宋体\">大数据系统基本要求》等</font><font face=\"Calibri\">12</font><font face=\"宋体\">项大数据国家标准获批正式发布，标准清单如下。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\" align=\"justify\" style=\"text-align: justify;\"><span>&nbsp;</span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">政策文件</font></span></b><span><o:p></o:p></span></p><p class=\"MsoNormal\"><span>&nbsp;</span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">河北：用好</font>“<font face=\"宋体\">好差评</font><font face=\"Calibri\">”</font><font face=\"宋体\">，促进政务服务提质增效 </font><font face=\"Calibri\">———</font><font face=\"宋体\">论深入开展</font><font face=\"Calibri\">“</font><font face=\"宋体\">三创四建</font><font face=\"Calibri\">”</font><font face=\"宋体\">力促各项事业实现新突破</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">政务服务好不好，群众评价说了算。近日，省政府办公厅印发《河北省政务服务</font>“<font face=\"宋体\">好差评</font><font face=\"Calibri\">”</font><font face=\"宋体\">评价办法》，提出河北省将全面建立政务服务绩效由企业和群众评判的</font><font face=\"Calibri\">“</font><font face=\"宋体\">好差评</font><font face=\"Calibri\">”</font><font face=\"宋体\">制度体系，进一步促进各级政府工作作风转变和工作效能提升，为企业和群众提供全面规范、公开公平、便捷高效的政务服务。此举体现了以人民为中心的发展思想，是深化</font><font face=\"Calibri\">“</font><font face=\"宋体\">放管服</font><font face=\"Calibri\">”</font><font face=\"宋体\">改革、加快建设服务型政府、深入开展</font><font face=\"Calibri\">“</font><font face=\"宋体\">三创四建</font><font face=\"Calibri\">”</font><font face=\"宋体\">活动的有力载体，一经出台就赢得了社会关注。</font></span><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">征集｜《广州市工业产业区块管理办法》公开征求社会公众意见</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">为规范全市工业产业区块管理，保障先进制造业发展空间，提高用地节约集约利用水平，推动工业高质量发展，根据《广州市提高工业用地利用效率实施办法》（穗府办规〔</font>2019<font face=\"宋体\">〕</font><font face=\"Calibri\">4</font><font face=\"宋体\">号）等文件有关规定，结合广州市实际，我局联合市规划和自然资源局组织制定了《广州市工业产业区块管理办法》（征求意见稿，以下简称《管理办法》，详见附件）。根据《广州市行政规范性文件管理规定》（广州市人民政府令第</font><font face=\"Calibri\">52</font><font face=\"宋体\">号），现对《管理办法》公开征求社会公众意见。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">《河南省加快</font>5G<font face=\"宋体\">产业发展三年行动计划 （</font><font face=\"Calibri\">2020—2022</font><font face=\"宋体\">年）》印发</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">近日，河南省政府办公厅印发《河南省加快</font>5G<font face=\"宋体\">产业发展三年行动计划（</font><font face=\"Calibri\">2020—2022</font><font face=\"宋体\">年）》，明确了</font><font face=\"Calibri\">5G</font><font face=\"宋体\">产业今后三年的发展目标，围绕加快</font><font face=\"Calibri\">5G</font><font face=\"宋体\">网络建设、加快推进</font><font face=\"Calibri\">5G</font><font face=\"宋体\">技术创新、大力发展</font><font face=\"Calibri\">5G</font><font face=\"宋体\">产业、深入拓展</font><font face=\"Calibri\">5G</font><font face=\"宋体\">应用场景、强化</font><font face=\"Calibri\">5G</font><font face=\"宋体\">安全保障等重点任务，进一步细化</font><font face=\"Calibri\">5G</font><font face=\"宋体\">网络建设支持政策，培育经济高质量发展新动能，推动网络强省建设。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">工信部</font>19<font face=\"宋体\">项举措深化信息通信领域</font><font face=\"Calibri\">“</font><font face=\"宋体\">放管服</font><font face=\"Calibri\">”</font><font face=\"宋体\">改革</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">　近日，工业和信息化部发布《关于深化信息通信领域</font>“<font face=\"宋体\">放管服</font><font face=\"Calibri\">”</font><font face=\"宋体\">改革的通告》，从减少申请材料、深化</font><font face=\"Calibri\">“</font><font face=\"宋体\">不见面</font><font face=\"Calibri\">”</font><font face=\"宋体\">审批、推行并行审批和检测优化、推进行政审批服务便民化、加强和规范涉企检查、不断深化</font><font face=\"Calibri\">“</font><font face=\"宋体\">证照分离</font><font face=\"Calibri\">”</font><font face=\"宋体\">六个方面提出了</font><font face=\"Calibri\">19</font><font face=\"宋体\">项改革举措。</font></span><span><o:p></o:p></span></p><p class=\"p\" align=\"justify\" style=\"text-align: justify;\"><span>&nbsp;</span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">观点分享</font></span></b><span><o:p></o:p></span></p><p class=\"MsoNormal\"><span>&nbsp;</span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">大数据技术在经济运行监测分析中的应用</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">摘要：从省级政府部门角度</font>,<font face=\"宋体\">对大数据技术在经济运行监测分析工作中的应用场景进行了探讨</font><font face=\"Calibri\">,</font><font face=\"宋体\">分析了当前工作应用中存在的缺乏专业人才支撑、涉企政务数据共享有待进一步加强、成熟适用的分析模型及应用场景比较少等问题</font><font face=\"Calibri\">,</font><font face=\"宋体\">提出强化专业人才培养、加快推进政务数据信息共享、深挖经济运行监测分析业务与大数据技术契合点等建议。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">提高基层治理能力，先解答好这三个问题</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">导读：新冠疫情对基层治理能力和治理水平提出了新的挑战，基层治理体系的进一步完善需要解答好以下三个问题：实现怎样的治理目标、谁来治理基层以及怎样治理基层。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt; <font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\"><font face=\"宋体\">中国城市数字经济发展报告</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">数字经济发展研究小组、中国移动通信联合会区块链专委会、数字岛研究院共同编写的《</font>2019-2020<font face=\"宋体\">中国城市数字经济发展报告》正式发布，该报告总计十大篇章，从国际到国内，从政策到应用，覆盖数字经济的两大分类：数字产业化及产业数字化，以城市为切入点，以国内近</font><font face=\"Calibri\">300</font><font face=\"宋体\">个地级及以上城市为考查目标，通过五大维度，二十余个细分指标，综合考查国内城市数字经济发展现状。</font></span><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"p\"><b><span class=\"16\">Gartner<font face=\"宋体\">对于建设数据中台的</font><font face=\"Calibri\">4</font><font face=\"宋体\">个建议</font></span></b><span><o:p></o:p></span></p><p class=\"p\"><span><font face=\"宋体\">数据中台是中国本土诞生的一个名词，很多企业在</font>“<font face=\"宋体\">什么是数据中台</font><font face=\"Calibri\">”</font><font face=\"宋体\">和</font><font face=\"Calibri\">“</font><font face=\"宋体\">我要上</font><font face=\"Calibri\">XX</font><font face=\"宋体\">中台</font><font face=\"Calibri\">”</font><font face=\"宋体\">徘徊。其炒作程度跟当年的</font><font face=\"Calibri\">“</font><font face=\"宋体\">大数据</font><font face=\"Calibri\">” </font><font face=\"宋体\">一词有一拼，如果用</font><font face=\"Calibri\">Gartner</font><font face=\"宋体\">的炒作周期图来看，数据中台目前已经逼近炒作的顶峰。</font></span><span><o:p></o:p></span></p><p class=\"p\"><a href=\"#wechat_redirect\"><span class=\"15\">&lt;&lt;<font face=\"宋体\">进入全文</font></span></a><span><o:p></o:p></span></p><p class=\"MsoNormal\"><span>&nbsp;</span></p>', 3, '电子政务简讯', '2020-06-06 20:31:35', '2020-06-09 15:44:13', 1, 0, '近日，广州市开办企业“一网通办、一窗通取”推进会召开，番禺作为该项工作的全市示范区，在会上播放视频作经验介绍。  　　番禺区作为开办企业“一网通办、一窗通取”试点做了大量的工作。一是印发系列配套改革文件，确保试点工作有序开展；二是多次与职能部门召开工作协调会，简化完善各流程环节；三是在大厅设置开办企业专区、增配设备、导办人员等，确保业务顺利开展。');
INSERT INTO `article` VALUES (4, '深化数字浙江建设，2020年工作要点来啦', 1, '超级管理员', '<section style=\"text-align: justify;\"><section powered-by=\"xiumi.us\" style=\"text-align: center;\"><section><section powered-by=\"xiumi.us\"><section><section powered-by=\"xiumi.us\"><section style=\"text-align: justify;\"><p><span>　《深化数字浙江建设2020年工作要点》近日出台，明确了2020年深化数字浙江建设总体安排：聚焦政府、经济、社会三大数字化转型，推进数字科技突破、数字基础设施提升、数据协同应用，加快建设国家数字经济创新发展试验区，为省域治理现代化提效，为浙江省“两个高水平”建设增色，为数字中国建设贡献浙江力量、提供浙江样本。</span></p></section></section></section></section></section></section></section><section style=\"text-align: justify;\"><section powered-by=\"xiumi.us\" style=\"text-align: center;\"><section><section powered-by=\"xiumi.us\"><section><section powered-by=\"xiumi.us\"><section style=\"text-align: justify;\"><p><strong><span>大力推进数字技术创新突破</span></strong><span></span></p><p><span>　　1、实施数字技术强基工程</span></p><p><span>　　推进之江实验室、阿里达摩院、西湖大学、浙江清华长三角研究院、北京航空航天大学杭州创新研究院等建设。</span></p><p><span>　　支持之江实验室争创国家实验室，支持中电海康集团创建自旋电子器件与集成系统国家重点实验室，支持阿里巴巴集团和杭州市创建国家数据智能技术创新中心。</span></p><p><span>　　支持浙江大学、之江实验室谋划建设工业互联网安全、量子精密测量与观测等大科学装置。</span></p><p><span>　　2、实施数字技术攻关工程</span></p><p><span>　　实施“领雁计划”，在专用芯片、开源芯片、人工智能、区块链、大数据、云计算、网络通信、量子计算、先进制造与智能装备等领域重点突破核心关键技术。</span></p><p><span>　　实施“尖兵计划”，在数字安防、智能网联车、工业机器人、工业互联网、精密加工机床、柔性显示等领域实现卡脖子技术攻关的重大突破。</span></p><p><span>　　3、实施数字技术协同创新工程</span></p><p><span>　　联合实施长三角关键核心技术协同攻关项目，三省一市共同争取国家2030重大战略项目和国家科技重大专项。</span></p><p><span>　　推进杭州、德清国家新一代人工智能创新发展试验区建设。</span></p><p><span>　　加快省级数字经济领域特色小镇、“万亩千亿”新产业平台等建设。</span></p><p><span>　　全力打造数字经济升级版</span></p><p><span>　　1、加快建设国家数字经济创新发展试验区</span></p><p><span>　　探索构建数字经济新型生产关系，加快政府数字化转型，创新数字经济多元协同治理体系，助力长三角一体化发展。</span></p><p><span>　　2、做强数字化新兴产业</span></p><p><span>　　打造全国领先的大数据产业中心，抓好14家大数据省级重点企业研究院建设，培育一批数据服务型企业。</span></p><p><span>　　建设全球知名的云计算产业中心。打造1家具有国际领先水平的公共云计算平台，培育发展一批云计算重点骨干企业。深化企业上云，累计上云企业达40万家。</span></p><p><span>　　3、壮大数字化基础产业</span></p><p><span>　　着力发展集成电路产业，推进杭州、宁波等省级集成电路产业基地和杭州“芯火”双创基地（平台）建设。</span></p><p><span>　　推动长电科技（绍兴）先进芯片封装测试、中欣晶圆大硅片、金瑞泓12英寸晶圆片生产线等重大项目建设，推动中电海康MRAM项目量产线、矽力杰12英寸模拟芯片等项目启动建设。</span></p><p><span>　　加快杭州市国际级软件名城建设，推进宁波市中国特色建名城创建，大力发展平台即服务（PaaS）和软件即服务（SaaS）模式。</span></p><p><span>　　支持杭州、宁波等地与华为公司共建服务器生产基地，推动鲲鹏生态体系建设。</span></p><p><span>　　4、推动制造业数字化转型</span></p><p><span>　　深化“1+N”工业互联网平台体系建设，推进阿里云 supET 工业互联网创新中心建设，加快推进长三角工业互联网一体化发展示范区建设，建设100个行业级、区域级、企业级省级工业互联网平台。</span></p><p><span>　　组织开展5000项智能化技术改造项目，推广应用工业机器人，累计在役工业机器人突破10万台。</span></p><p><span>　　探索“未来工厂”建设标准和路径，加快企业数字化、网络化、智能化转型。</span></p><p><span>　　5、推进智慧农业发展</span></p><p><span>　　加快建设浙江乡村智慧网，完善全省农业农村大数据中心，打造农业农村统一数据资源目录，健全数字乡村应用支撑体系。</span></p><p><span>　　加快智慧园艺、智慧畜禽、智慧水产、智慧田管建设，创建100个数字农业工厂。</span></p><p><span>　　深入实施“互联网+”农产品出村进城工程，发展农产品加工物流、快递、冷链仓储等基础设施，建立区域性电子商务公共服务平台，发展农产品电商，深化农业休闲观光旅游网络推广。</span></p><p><span>　　6、推动新型贸易中心建设</span></p><p><span>　　加快建设中国（浙江）国际贸易“单一窗口”，推进浙江数字口岸一体化及“单一窗口”数据协同，做好船舶通关一体化试点，在舟山、宁波、张家港实现国际航行船舶转港数据复用。</span></p><p><span>　　深化杭州、宁波、义乌跨境电商综试区建设，制定实施温州、绍兴跨境电商综试区实施方案。</span></p><p><span>　　力争全省跨境网络零售出口1000亿元以上。支持省内eWTP试点建设。</span></p><p><span>　　7、建设新兴金融中心</span></p><p><span>　　推动世界银行全球数字金融中心建设。推动浙江省金融科技应用试点，支持嘉兴申报科技金融改革创新国家试点，全面推进全省33个试点项目建设。</span></p><p><span>　　推进“移动支付之省”建设，探索制定移动支付之省建设评价指标体系。</span></p><p><span>　　推动银行、保险、证券机构数字化转型，完成省、市、县三级银行机构信贷业务流程全覆盖，实现与公共信用信息、“亩均论英雄”、区块链电子票据等平台对接。</span></p><p><span>　　8、发展数字文旅产业</span></p><p><span>　　建设“浙里好玩”平台，完善主题“一卡通”，开展线上文旅消费季活动，探索“信用游”浙江。</span></p><p><span>　　制定《浙江省关于加快推动文化和科技深度融合发展的实施意见》。</span></p><p><span>　　指导提升横店影视文化产业集聚区、宁波象山影视城数字化水平，构建全国领先的高科技影视后期制作中心和数字网络中心。</span></p><p><span>　　开展新一批“文化+互联网”创新企业遴选活动。</span></p></section></section></section></section></section></section></section><section style=\"text-align: justify;\"><section powered-by=\"xiumi.us\" style=\"text-align: center;\"><section><section powered-by=\"xiumi.us\"><section><section powered-by=\"xiumi.us\"><section style=\"text-align: justify;\"><p><span>　&nbsp;<strong><span class=\"Apple-converted-space\">&nbsp;</span>全面提升政府数字化治理能力和水平</strong></span></p><p><span>　　1、建设省域空间治理数字化平台</span></p><p><span>　　建成省域空间治理大数据库，形成一套通用模块，统筹相关部门空间治理业务，推进重大战略、重大平台、重大项目谋划和实施全链条协同，形成多场景应用、多部门协同、高质量智慧化的省域空间治理数字化平台。</span></p><p><span>　　建设国土空间规划实施监督信息系统和自然生态资源综合信息监管系统，提升不动产登记一窗云平台服务水平。</span></p><p><span>　　2、推动社会治理数字化转型</span></p><p><span>　　深化“基层治理四平台”信息系统建设，建设基层治理主题数据库，建设社会矛盾纠纷调处化解、食品安全信息报送处置等平台。</span></p><p><span>　　推动雪亮工程建设，实现视频数据智能化创新应用。完成自然灾害、安全生产风险防控和应急救援平台建设，建成应急管理综合指挥平台和全省应急管理减灾救灾业务应用专题二期项目。</span></p><p><span>　　3、推动经济管理数字化应用</span></p><p><span>　　深化经济运行监测分析数字化平台运用。</span></p><p><span>　　继续推进政采云和统一公共支付平台建设，深化全省电子票据改革，升级改造财政业务一体化系统并在全省分步分批推广。</span></p><p><span>　　建成浙江省能源管理数字化平台。</span></p><p><span>　　4、加快数字化监管</span></p><p><span>　　强化全省统一行政执法监管（“互联网+监管”）平台建设应用，推进简易处罚的掌上办理端应用。</span></p><p><span>　　双随机事项覆盖率达到100%，跨部门联合双随机监管占比达到5%，现场执法的掌上执法应用率达到 90%，风险预警事件处置率达到 100%。</span></p><p><span>　　建设数字化市场监管体系，持续推进特种设备安全风险防范预警系统、食品安全综合治理数字化平台、全国网络交易监测平台、长三角市场主体数据库等重大项目建设与应用。深化金融风险“天罗地网”监测防控体系建设。</span></p><p><span>　　5、推动公共服务数字化应用</span></p><p><span>　　全面推进省市县三级服务事项接入政务服务2.0平台，加快推动多部门联办“一件事”延伸扩面。</span></p><p><span>　　持续迭代建设浙政钉2.0 平台，推动各级部门开发“微应用”，实现政务协同业务“上钉”。</span></p><p><span>　　推广机关内部“最多跑一次”系统，实现90%以上部门间非涉密办事事项接入平台。迭代建设投资项目在线审批监管平台3.0版。</span></p><p><span>　　6、推进政务数字化转型</span></p><p><span>　　编制出台《省本级电子政务项目管理办法》，加快“912”工程建设。迭代升级全省规划管理数字化平台。</span></p><p><span>　　推进建设覆盖浙江省纪检监察系统的检举举报平台，构建标准统一、业务协同、数据共享的信息化监督平台。</span></p><p><span>　　建设司法行政指挥中心数据大脑，开展全省公共法律服务实体平台、网络平台、热线平台融合建设，打通12348热线数据共享路径，构建全省统一标准的法律服务办件库。建设统一的数字档案系统，全面推进政务服务事项电子化归档工作，实现政务办理“归档零纸件”和数字档案馆（室）全覆盖。</span></p></section></section></section></section></section></section></section><section style=\"text-align: justify;\"><section powered-by=\"xiumi.us\" style=\"text-align: center;\"><section><section powered-by=\"xiumi.us\"><section><section powered-by=\"xiumi.us\"><section style=\"text-align: justify;\"><p><span>　<strong>&nbsp; 加快构建数字社会建设样板省</strong></span></p><p><span>　　1、加快建设智慧城市</span></p><p><span>　　加强杭州、宁波、嘉兴等地在“城市大脑”、新型智慧城市建设领域的理论研究与创新应用，指导湖州、衢州、德清“城市大脑”示范试点建设。</span></p><p><span>　　推动城市大脑在交通、平安、城管、市政公用、环保、文旅等领域的综合应用，形成一批新型智慧城市应用新标杆。</span></p><p><span>　　推动全省未来社区建设，构建未来社区智慧服务平台。</span></p><p><span>　　2、发展数字就业服务</span></p><p><span>　　建设省就业创业服务应用项目和省就业监测平台，开展全省就业形势监测分析评估。</span></p><p><span>　　推进人才服务平台（二期）建设，推进人才项目申报系统建设，打造“掌上服务平台”，加强服务事项和服务资源归集，建设全省统一的人才流量入口、人才服务超市和人才数据平台。</span></p><p><span>　　3、发展数字医疗</span></p><p><span>　　建成全省医疗保障系统核心业务骨干网络、医疗保障专属行业云，实现全省统一的医疗保障核心业务平台在试点地区上线运行。</span></p><p><span>　　推动实现浙、苏、皖全省域异地就医门诊费用直接结算。升级优化全省医保移动支付平台，融合医保电子凭证的应用与推广。</span></p><p><span>　　升级完善省、市、县三级全民健康信息平台，推进健康专有云建设。</span></p><p><span>　　推动互联网医院向基层延伸，完善互联网诊疗服务的医保政策，实现健康医保卡在线医保结算功能，建立全流程线上服务模式。</span></p><p><span>　　在省市级医院和部分有条件的医共体，率先开展医学人工智能创新应用。</span></p><p><span>　　4、全面打造数字交通</span></p><p><span>　　持续推进智慧高速云控平台建设，推出省智慧高速云控1.0版，深化“浙里畅行”出行一站式服务应用开发，实现公众出行城市“一张图”覆盖全省主要城市。</span></p><p><span>　　建设500条普通公路非现场执法车道，累计达到1800条。</span></p><p><span>　　深入推进宁波舟山港智慧港口建设。</span></p><p><span>　　推动沪杭甬高速智慧化改造，重点完成建设高清地图和高精度定位、布局5G通信网络、推广使用绿色能源等应用试点。</span></p><p><span>　　全面建设杭州绕城西复线智慧公路，力争主体工程同步建成通车。</span></p><p><span>　　5、全域推行数字文体</span></p><p><span>　　承办中国短视频大会和网络视频大赛。</span></p><p><span>　　探索省、市、县三级广电媒体融合协作新模式，推动全省广播电视媒体融合发展创新中心建设。</span></p><p><span>　　建设全民健身地图2.0版。整合公共体育设施及具备条件的商业健身资源，实现80%以上符合条件的体育场地设施接入预约支付功能。实施体育场馆智能化改造，提升体育场馆智慧化管理水平。</span></p><p><span>　　6、全面发展数字教育</span></p><p><span>　　启动之江汇教育广场2.0建设，建设新型教学空间1000个，开设网络同步课程500门，新增共享微课资源1000个。</span></p><p><span>　　开展“互联网+义务教育”实验区建设。实施全省中小学教师信息技术应用能力工程2.0，开展名师网络工作室、特色网络教学空间等项目建设，持续开展名师直播活动100场、针对性地帮扶乡村学校送教活动100次和伴随式评课指导100次。</span></p><p><span>　　推动省域在线开放课程共享应用，推进省内高校开展在线开放课程的跨校学分认定和转换。</span></p><p><span>　　7、建设养老数字化平台</span></p><p><span>　　发展互联网+养老，加快建设“浙里养”平台。</span></p></section></section></section></section></section></section></section><section style=\"text-align: justify;\"><section powered-by=\"xiumi.us\" style=\"text-align: center;\"><section><section powered-by=\"xiumi.us\"><section><section powered-by=\"xiumi.us\"><section style=\"text-align: justify;\"><p><span>&nbsp; &nbsp;<span class=\"Apple-converted-space\">&nbsp;</span><strong>&nbsp;进一步强化云上浙江基础支撑</strong></span></p><p><span>　　1、推进新型基础设施建设</span></p><p><span>　　制定新型信息基础设施建设规划，加快建设国家新型互联网交换中心、工业互联网标识解析节点等，加快建设义乌市国际互联网数据专用通道，争取中国（浙江）自由贸易试验区舟山国际互联网数据专用通道建设获得批复。</span></p><p><span>　　建成5G 基站超过5万个，实现城区、交通干线、重点景区等区域的 5G信号覆盖。</span></p><p><span>　　推动阿里巴巴、浙江联通、杭钢集团、之江实验室等一批大数据中心建设。</span></p><p><span>　　2、加快推进IPv6规模化部署</span></p><p><span>　　推动基础电信企业持续优化提升IPv6网络质量和服务能力，推动内容分发网络、云服务平台等IPv6改造升级。</span></p><p><span>　　3、推进数字长三角建设</span></p><p><span>　　编制数字长三角建设方案。建设长三角信用平台，打造长三角信用数据资源池。</span></p><p><span>　　推进长三角地区政务服务“一网通办”，实现长三角地区高频电子证照跨省亮证、一号通办。</span></p><p><span>　　开展长三角地区统一的环境信息系统建设，共享大气、水、土壤等环境数据，推进环保数字化联防联控。</span></p><p><span>　　4、推进数字大湾区建设</span></p><p><span>　　建设一批重大项目，打造全球数字经济创新高地。</span></p><p><span>　　5、推进卫星互联网建设和应用</span></p><p><span>　　着力打造统一高效、共建共享的数据强省</span></p><p><span>　　1、迭代建设公共数据平台2.0</span></p><p><span>　　完善省市两级公共数据平台，推进一批省域治理专题数据库建设，开通市场监管、公安、人社等 19条“数据高铁” 专线。</span></p><p><span>　　加快建设省级政务“一朵云”平台、同城容灾中心、异地备份中心。</span></p><p><span>　　建设浙江数字政府大数据研究院，推动各部门电子政务系统向政务云平台集中部署、迁移。</span></p><p><span>　　2、加快公共数据开放应用</span></p><p><span>　　完善省市数据开放目录，厘清数据开放需求清单，新增开放数据集 1500个、开放数据项5000项以上。</span></p><p><span>　　优先开放普惠金融、交通出行、医疗健康、市场监管、社会保障等领域数据，设区市推出5个以上数据开放创新应用。</span></p><p><span>　　3、培育市场化数据应用服务主体</span></p><p><span>　　依托云栖小镇等平台，建设并推广数据供应中枢系统，推动场景应用和治理应用，打造数据供应链和产业链。</span></p><p><span>　　4、推进政法大数据建设</span></p><p><span>　　依托省数字政法平台，加快各级政法单位数据资源整合利用，做强政法云数据中心，编制第三期《政法信息资源共享目录》，实现政法数据资源一体化。</span></p><p><span>　　5、推进城市管理大数据建设</span></p><p><span>　　开展城市管理数据资源普查，实现行业全要素数据资源目录管理。</span></p><p><span>　　建设城市管理数据管理平台，建立行业数据资源管理系统和数据标签体系。</span></p><p><span>　　建设城管数据交换共享平台，建立城市管理特征数据库，实现省市县三级行业监管部门间城市运行数据互联共享。</span></p></section></section></section></section></section></section></section>', 3, '数字建设', '2020-06-09 15:45:23', '2020-06-09 15:45:23', 1, 0, '《深化数字浙江建设2020年工作要点》近日出台，明确了2020年深化数字浙江建设总体安排：聚焦政府、经济、社会三大数字化转型，推进数字科技突破、数字基础设施提升、数据协同应用，加快建设国家数字经济创新发展试验区，为省域治理现代化提效，为浙江省“两个高水平”建设增色，为数字中国建设贡献浙江力量、提供浙江样本。');
INSERT INTO `article` VALUES (5, '【最新动态】2020年1－2月软件业经济运行情况', 1, '超级管理员', '<div id=\"page-content\" class=\"rich_media_area_primary\"><div class=\"rich_media_area_primary_inner\"><div id=\"img-content\" class=\"rich_media_wrp\"><h2 class=\"rich_media_title\" id=\"activity-name\">【最新动态】2020年1－2月软件业经济运行情况</h2><div id=\"meta_content\" class=\"rich_media_meta_list\"><span class=\"rich_media_meta rich_media_meta_text\">工信微报</span><span class=\"Apple-converted-space\">&nbsp;</span><span class=\"rich_media_meta rich_media_meta_nickname\" id=\"profileBt\"><a id=\"js_name\">广东省电子政务协会</a></span><span class=\"Apple-converted-space\">&nbsp;</span><span id=\"publish_time\" class=\"rich_media_meta rich_media_meta_text\">4月3日</span></div><div id=\"js_album_area\"></div><div class=\"rich_media_content \" id=\"js_content\" style=\"text-align: justify;\"><section><span>　1-2月，受新冠肺炎疫情影响，需现场支持的技术服务和软件招投标项目等进度受阻，我国软件和信息技术服务业（以下简称软件业）的软件业务收入、利润、出口、人员工资总额均出现大幅下降，细分领域表现各异。<br></span></section><section><br></section><section><section powered-by=\"xiumi.us\"><section><section><p><span><strong>一</strong></span></p></section><section><p><span><strong>总体运行情况</strong></span></p></section></section></section></section><section><span>　　<strong>软件业务收入急速下行。</strong>1-2月，我国软件业完成软件业务收入8008亿元，同比下降11.6%，增速同比回落25.2个百分点，较去年全年回落27个百分点。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages \" data-ratio=\"0.43573667711598746\" data-s=\"300,640\" data-type=\"png\" data-w=\"638\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPenvSke9tIYX4OxzgU5iaQkiconRvhBlkqJOz3ftnX480TNX7byrATyng/640?wx_fmt=png\" _width=\"576px\" src=\"@@@1591806073612_img_upload.png\" crossorigin=\"anonymous\" data-fail=\"0\" id=\"id_1591806073591\"></section><section style=\"text-align: center;\"><span>图1&nbsp; 2019年1-2月以来软件业务收入增长情况</span></section><section style=\"text-align: center;\"><br></section><section><span>　<strong>　利润明显回落。</strong>1-2月，全行业实现利润总额981亿元，同比下降11.8%，增速同比回落18.8个百分点。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages \" data-ratio=\"0.43837753510140404\" data-s=\"300,640\" data-type=\"png\" data-w=\"641\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwP9n9CjsT9Okl3LdNmR6PKTCN6CusPvFkNJa9ZAxvt4DwIE4ZwBtqmHA/640?wx_fmt=png\" _width=\"576px\" src=\"@@@1591806074149_img_upload.png\" crossorigin=\"anonymous\" data-fail=\"0\" id=\"id_1591806074140\"></section><section style=\"text-align: center;\"><span>图2&nbsp; 2019年1-2月以来软件业利润总额增长情况</span></section><section style=\"text-align: center;\"><br></section><section><span>　　<strong>软件出口大幅下降。</strong>1-2月，软件业实现出口56.6亿美元，同比下降18.9%。其中，外包服务出口下降33.8%，增速同比回落39.0个百分点；嵌入式系统软件出口下降22.4%。</span></section><section><span>&nbsp;</span></section><section style=\"text-align: center;\"><img class=\"rich_pages \" data-ratio=\"0.43661971830985913\" data-s=\"300,640\" data-type=\"png\" data-w=\"639\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPWUQJBZBLjQULiaElx4dHn9oNmaxYFK6otL7wYyu32U9JjkuNBKkPetA/640?wx_fmt=png\" _width=\"576px\" src=\"@@@1591806074522_img_upload.png\" crossorigin=\"anonymous\" data-fail=\"0\" id=\"id_1591806074513\"></section><section style=\"text-align: center;\"><span>图3&nbsp; 2019年1-2月以来软件业出口增长情况</span></section><section style=\"text-align: center;\"><br></section><section><span>　　<strong>从业人数小幅增长，工资总额小幅下降。</strong>1-2月，我国软件业从业平均人数584万人，同比增长0.9%。从业人员工资总额同比下降2.6%，增速同比回落16.4个百分点。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages \" data-ratio=\"0.4308176100628931\" data-s=\"300,640\" data-type=\"png\" data-w=\"636\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPicWQC1Tibk0qoQuNgDhU6TibELtPEOlo4ow9P3mZ7zic97JgVsTiaHbuHjg/640?wx_fmt=png\" _width=\"576px\" src=\"@@@1591806074880_img_upload.png\" crossorigin=\"anonymous\" data-fail=\"0\" id=\"id_1591806074870\"></section><section style=\"text-align: center;\"><span>图4&nbsp; 2019年1-2月以来软件业从业人员工资总额增长情况</span></section><section style=\"text-align: center;\"><br></section><section><section powered-by=\"xiumi.us\"><section><section><p><span><strong>二</strong></span></p></section><section><p><span><strong>分领域运行情况</strong></span></p></section></section></section></section><section><span>　　<strong>软件产品收入大幅下降。</strong>1-2月，软件产品实现收入2189亿元，同比下降13.7%，增速同比回落24.8个百分点，占全行业收入的比重为27.3%。其中，工业软件产品收入达到233亿元，同比下降10.9%，占软件产品收入比重为10.6%。</span></section><section><br></section><section><span>　　<strong>信息技术服务收入降幅较小。</strong>1-2月，信息技术服务实现收入4824亿元，在全行业收入中占比为60.2%，同比下降8.4%，下降幅度相对较小，但增速同比回落25.2个百分点。其中，电子商务平台技术服务收入853亿元，同比增长3.5%，但增幅较去年全年回落24.6个百分点；云服务收入209亿元，同比下降33.3%；大数据服务收入254亿元，同比下降4.0%；集成电路设计收入226亿元，同比下降6.4%。</span></section><section><br></section><section><span>　　<strong>信息安全产品和服务收入大幅下滑。</strong>1-2月信息安全产品和服务共实现收入133亿元，同比下降11.6%，增速同比回落24.7个百分点。</span></section><section><br></section><section><span>　　<strong>嵌入式系统软件收入降幅较大。</strong>1-2月，嵌入式系统软件实现收入862亿元，同比下降22.0%，增速同比回落26.9个百分点。由于和制造业紧密相关，嵌入式系统软件收入在四个软件分领域中下降幅度最大。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages img_loading\" data-ratio=\"0.43661971830985913\" data-s=\"300,640\" data-type=\"png\" data-w=\"639\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPaz4z8YPp97Q006dibAWbSIPVsOMicOHBJwKQlibcS8icC6WFhoRaHJANlQ/640?wx_fmt=png\" _width=\"576px\" src=\"data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==\" crossorigin=\"anonymous\"></section><section style=\"text-align: center;\"><span>图5&nbsp; 2020年1-2月软件业分类收入占比情况</span></section><section><br></section><section><section powered-by=\"xiumi.us\"><section><section><p><span><strong>三</strong></span></p></section><section><p><span><strong>分地区运行情况</strong></span></p></section></section></section></section><section><span>　　<strong>东部地区软件业收入下降幅度相对较小，西部和东北地区降幅较大。</strong>1-2月，东部地区完成软件业务收入6837亿元，同比下降9.4%。中部地区完成软件业务收入104亿元，同比下降12.9%。西部地区完成软件业务收入863亿元，同比下降21.7%。东北地区完成软件业务收入204亿元，同比下降29.6%。东中西和东北四个地区软件业务收入在全国总收入中的占比分别为：85.4%、1.3%、10.8%和2.5%。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages img_loading\" data-ratio=\"0.434375\" data-s=\"300,640\" data-type=\"png\" data-w=\"640\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwP3PicR6csD2WRYxRgnicqkOUz1kyjPZGvRpx17ykbicPjftOqkzGicDmLnA/640?wx_fmt=png\" _width=\"576px\" src=\"data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==\" crossorigin=\"anonymous\"></section><section style=\"text-align: center;\"><span>图6&nbsp; 2020年1-2月软件业分地区收入增长情况</span></section><section style=\"text-align: center;\"><br></section><section><span>　　<strong>主要软件大省业务收入不同程度下降。</strong>1-2月，软件业务收入居前5名的广东、北京、江苏、上海、浙江软件业务收入分别下降3.1%、5.7%、13.3%、14.7%、16.5%，五省市合计软件业务收入占全国比重为69.7%。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages img_loading\" data-ratio=\"0.43573667711598746\" data-s=\"300,640\" data-type=\"png\" data-w=\"638\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPT1R2rtWpEdsLV91ibOq7IM7XCtSO2RUMgFq1CdVSGWic6WjkcK2sE1iag/640?wx_fmt=png\" _width=\"576px\" src=\"data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==\" crossorigin=\"anonymous\"></section><section style=\"text-align: center;\"><span>图7&nbsp; 2020年1-2月软件业务收入前十位省市增长情况</span></section><section><span>&nbsp;</span></section><section><span>　　<strong>中心城市软件业务收入大幅下降。</strong>1-2月，全国14个副省级中心城市实现软件业务收入4378亿元，同比下降10.2%，占全国软件业务收入比重达54.7%；中心城市软件业利润总额同比下降9.9%。</span></section><section><br></section><section style=\"text-align: center;\"><img class=\"rich_pages img_loading\" data-ratio=\"0.43260188087774293\" data-s=\"300,640\" data-type=\"png\" data-w=\"638\" data-src=\"https://mmbiz.qpic.cn/mmbiz_png/G0YfOInuXpsddNicGd0zlNKk6AzS2lpwPlzLKv8jeBBlqsxDYAh4Qiaddbe0t5ksyztnnHAtDcibnh1dxbvA8rF9g/640?wx_fmt=png\" _width=\"576px\" src=\"data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==\" crossorigin=\"anonymous\"></section><section style=\"text-align: center;\"><span>图8&nbsp; 2019年1-2月以来副省级中心城市软件业务收入增长情况</span></section><section><br></section><hr><section><span>来源：工业和信息化部运行监测协调局</span></section></div></div></div></div>', 3, '软件业', '2020-06-11 00:21:33', '2020-07-16 23:16:03', 1, 0, '　1-2月，受新冠肺炎疫情影响，需现场支持的技术服务和软件招投标项目等进度受阻，我国软件和信息技术服务业（以下简称软件业）的软件业务收入、利润、出口、人员工资总额均出现大幅下降，细分领域表现各异。');

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `channel_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '目录ID',
  `channel_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录名称',
  `parent_channel_id` int(11) NULL DEFAULT 0 COMMENT '父类ID',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `status` tinyint(255) NULL DEFAULT 1 COMMENT '发布状态:1发布0未发布',
  `seq` int(6) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES (1, '首页', 0, 'fa fa-tachometer', 0, 0);
INSERT INTO `channel` VALUES (3, '新闻资讯', 0, 'layui-icon-rss', 1, 0);
INSERT INTO `channel` VALUES (4, '专业知识', 0, 'layui-icon-rss', 1, 0);

-- ----------------------------
-- Table structure for form
-- ----------------------------
DROP TABLE IF EXISTS `form`;
CREATE TABLE `form`  (
  `form_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报告模板id',
  `form_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `form_type` int(1) NULL DEFAULT NULL COMMENT '模板类型',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户名称',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `form_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板描述',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态:0未发布 1已发布',
  `attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  PRIMARY KEY (`form_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form
-- ----------------------------
INSERT INTO `form` VALUES (10, '咨询表', 1, 1, 'admin', '2020-05-24 17:04:56', '2020-06-27 20:24:33', '<p><span>请提交咨询表</span></p>', 1, '');

-- ----------------------------
-- Table structure for form_item
-- ----------------------------
DROP TABLE IF EXISTS `form_item`;
CREATE TABLE `form_item`  (
  `item_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报告项id',
  `form_id` int(11) NULL DEFAULT NULL COMMENT '报告模板id',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报告项名称',
  `item_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'input' COMMENT '报告项类型input,radio,checkbox,textarea',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `item_tip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提示',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_item
-- ----------------------------
INSERT INTO `form_item` VALUES (50, 10, '附件上传', 'fileupload', '', NULL);
INSERT INTO `form_item` VALUES (49, 10, '问题描述', 'textarea', '', NULL);
INSERT INTO `form_item` VALUES (48, 10, '公司地址', 'input', '', NULL);
INSERT INTO `form_item` VALUES (47, 10, '咨询方向', 'input', '', NULL);

-- ----------------------------
-- Table structure for form_submit
-- ----------------------------
DROP TABLE IF EXISTS `form_submit`;
CREATE TABLE `form_submit`  (
  `submit_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '提交id',
  `form_id` int(11) NULL DEFAULT NULL COMMENT '表单id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态: 0审核未通过 1提交 2审核通过',
  `show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `audit_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `audit_user_id` int(11) NULL DEFAULT NULL COMMENT '审核用户ID',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`submit_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_submit
-- ----------------------------
INSERT INTO `form_submit` VALUES (9, 10, 1, '2020-05-26 23:36:17', '2020-05-26 23:36:17', 'admin', 2, 'admin', 'admin', '2020-05-26 23:37:19', 1, NULL, NULL);
INSERT INTO `form_submit` VALUES (10, 10, 1, '2020-05-26 23:36:55', '2020-05-26 23:36:55', 'admin', 0, 'admin', 'admin', '2020-05-27 23:57:18', 1, NULL, NULL);
INSERT INTO `form_submit` VALUES (12, 10, NULL, '2020-06-27 20:28:26', '2020-06-27 20:28:26', 'Moshow K XXX', 2, 'Moshow K XXX', '', '2020-06-27 20:44:16', NULL, '18502072288', 'WOMS');

-- ----------------------------
-- Table structure for form_submit_value
-- ----------------------------
DROP TABLE IF EXISTS `form_submit_value`;
CREATE TABLE `form_submit_value`  (
  `value_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '值id',
  `item_id` int(11) NOT NULL COMMENT '报告项id',
  `form_id` int(11) NULL DEFAULT NULL COMMENT '表单id',
  `value_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '值',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `submit_id` int(11) NULL DEFAULT NULL COMMENT '提交id',
  PRIMARY KEY (`value_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_submit_value
-- ----------------------------
INSERT INTO `form_submit_value` VALUES (38, 47, 10, '超级管理员', 1, 9);
INSERT INTO `form_submit_value` VALUES (39, 48, 10, '18588888888', 1, 9);
INSERT INTO `form_submit_value` VALUES (40, 49, 10, '2414214', 1, 9);
INSERT INTO `form_submit_value` VALUES (41, 50, 10, '', 1, 9);
INSERT INTO `form_submit_value` VALUES (42, 47, 10, '超级管理员', 1, 10);
INSERT INTO `form_submit_value` VALUES (43, 48, 10, '18588888888', 1, 10);
INSERT INTO `form_submit_value` VALUES (44, 49, 10, '2414214444', 1, 10);
INSERT INTO `form_submit_value` VALUES (45, 50, 10, '', 1, 10);
INSERT INTO `form_submit_value` VALUES (46, 47, 10, '', NULL, 11);
INSERT INTO `form_submit_value` VALUES (47, 48, 10, '', NULL, 11);
INSERT INTO `form_submit_value` VALUES (48, 49, 10, '', NULL, 11);
INSERT INTO `form_submit_value` VALUES (49, 50, 10, '', NULL, 11);
INSERT INTO `form_submit_value` VALUES (50, 47, 10, '好得', NULL, 12);
INSERT INTO `form_submit_value` VALUES (51, 48, 10, 'yes', NULL, 12);
INSERT INTO `form_submit_value` VALUES (52, 49, 10, '是的', NULL, 12);
INSERT INTO `form_submit_value` VALUES (53, 50, 10, '', NULL, 12);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `href` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/cms/index' COMMENT '地址',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'fa fa-home' COMMENT '图标',
  `target` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '_self' COMMENT '目标',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名字',
  `parent_menu_id` int(11) NULL DEFAULT 0 COMMENT '父菜单id',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1,5,9' COMMENT '权限',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (10, '/cms/index', 'fa fa-home', '_self', '基础管理', 0, '1,9,');
INSERT INTO `menu` VALUES (11, '/cms/user/list', 'fa fa-tachometer', '_self', '用户管理', 10, '1,9,');
INSERT INTO `menu` VALUES (12, '/cms/menu/list', 'fa fa-home', '_self', '菜单管理', 10, '1,9,');
INSERT INTO `menu` VALUES (32, '/cms/xxx/list', 'fa fa-tachometer', '_self', '文章管理', 0, '9,');
INSERT INTO `menu` VALUES (33, '/cms/channel/list', 'fa fa-tachometer', '_self', '频道列表', 32, '9,');
INSERT INTO `menu` VALUES (34, '/cms/article/list', 'fa fa-tachometer', '_self', '文章列表', 32, '9,');
INSERT INTO `menu` VALUES (38, '/cms/template/list', 'fa fa-tachometer', '_self', '模板值管理', 39, '9,');
INSERT INTO `menu` VALUES (22, '/cms/activity', 'fa fa-tachometer', '_self', '活动管理', 0, '1,9,');
INSERT INTO `menu` VALUES (23, '/cms/activity/list', 'fa fa-tachometer', '_self', '活动列表', 22, '1,9,');
INSERT INTO `menu` VALUES (25, '/cms/xxx/list', 'fa fa-tachometer', '_self', '表单管理', 0, '1,9,');
INSERT INTO `menu` VALUES (26, '/cms/form/list', 'fa fa-tachometer', '_self', '表单模板', 25, '1,9,');
INSERT INTO `menu` VALUES (27, '/cms/formSubmit/list', 'fa fa-tachometer', '_self', '提交列表', 25, '9,');
INSERT INTO `menu` VALUES (39, '/cms/xxx/list', 'fa fa-tachometer', '_self', '页面管理', 0, '1,9,');
INSERT INTO `menu` VALUES (40, '/cms/page/index', 'fa fa-tachometer', '_self', '首页预览', 39, '9,');
INSERT INTO `menu` VALUES (41, '/cms/page/article/3', 'fa fa-tachometer', '_self', '文章预览', 39, '9,');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (9, '管理员', '管理员权限');
INSERT INTO `role` VALUES (1, '普通用户', '普通用户权限');

-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template`  (
  `template_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'div id',
  `template_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 221 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of template
-- ----------------------------
INSERT INTO `template` VALUES (1, 'companyName', '广州智纵慧联信息咨询有限公司', '公司名称', 'base');
INSERT INTO `template` VALUES (2, 'bannerTitle', '政务信息系统云化迁移探讨', '首页大展示框大字', 'index');
INSERT INTO `template` VALUES (3, 'bannerDescription', '进行中', '首页大展示框小字', 'index');
INSERT INTO `template` VALUES (4, 'bannerButton', '点击参与', '大展示框按钮', 'index');
INSERT INTO `template` VALUES (5, 'bannerUrl', '#1', '大展示框按钮跳转页面', 'index');
INSERT INTO `template` VALUES (6, 'bannerImage', '/img/intro/banner-1.png', '大展示框背景图', 'index');
INSERT INTO `template` VALUES (7, 'serviceTitle', '我们的服务', '服务标题', 'index');
INSERT INTO `template` VALUES (8, 'serviceDescription', '商务文印服务;商品信息咨询服务;企业管理咨询服务;市场调研服务;市场营销策划服务;群众参与的文艺类演出、比赛等公益性文化活动的策划;会议及展览服务;计算机网络系统工程服务;计算机技术开发、技术服务;软件开发;', '服务描述', 'index');
INSERT INTO `template` VALUES (9, 'location', '广州市越秀区盘福路医国后街1号大院自编1号1503房', '地址', 'base');
INSERT INTO `template` VALUES (10, 'phone', '020-87599883', '联系电话', 'base');
INSERT INTO `template` VALUES (11, 'email', '279554612@qq.com', '电子邮箱', 'base');
INSERT INTO `template` VALUES (12, 'service1icon', '/img/services/1.svg', '服务1图标', 'index');
INSERT INTO `template` VALUES (13, 'service2icon', '/img/services/2.svg', '服务2图标', 'index');
INSERT INTO `template` VALUES (14, 'service3icon', '/img/services/3.svg', '服务3图标', 'index');
INSERT INTO `template` VALUES (15, 'service1title', '咨询', '服务1标题', 'index');
INSERT INTO `template` VALUES (16, 'service2title', '金融', '服务2标题', 'index');
INSERT INTO `template` VALUES (17, 'service3title', '支持', '服务3标题', 'index');
INSERT INTO `template` VALUES (18, 'service1description', '咨询', '服务1链接', 'index');
INSERT INTO `template` VALUES (19, 'service2description', '咨询', '服务2描述', 'index');
INSERT INTO `template` VALUES (20, 'service3description', '咨询', '服务3描述', 'index');
INSERT INTO `template` VALUES (22, 'service2url', 'www.egag.org.cn', '服务2链接', 'index');
INSERT INTO `template` VALUES (23, 'service3url', 'www.egag.org.cn', '服务3链接', 'index');
INSERT INTO `template` VALUES (21, 'service1url', 'www.egag.org.cn', '服务1链接', 'index');
INSERT INTO `template` VALUES (24, 'solutionTitle', '终极解决方案', '解决方案标题', 'index');
INSERT INTO `template` VALUES (25, 'solutionDescription', '解决方案内容有：xxxxxxxxxxxxxxxxxxxxxxx', '解决方案内容', 'index');
INSERT INTO `template` VALUES (213, 'solutionUrl', 'www.egag.org.cn', '解决方案链接', 'index');
INSERT INTO `template` VALUES (214, 'readMore', '了解更多', 'ReadMore', 'index');
INSERT INTO `template` VALUES (215, 'quoteTitle', '报价信息', '标题', 'index');
INSERT INTO `template` VALUES (216, 'quoteDescription', '获取业界公正、公开、公平的报价，请马上联系我们', '描述', 'index');
INSERT INTO `template` VALUES (217, 'quoteUrl', 'http://www.egag.org.cn', '链接', 'index');
INSERT INTO `template` VALUES (218, 'consultingTitle', '我们是广东省一流的咨询公司', '标题', 'index');
INSERT INTO `template` VALUES (219, 'consultingDescription', '以前瞻的理念、专业一流的团队为企业提供系统咨询服务，专业、放心。', '描述', 'index');
INSERT INTO `template` VALUES (220, 'consultingUrl', 'http://www.egag.org.cn', '链接', 'index');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号名称',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `show_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名称',
  `company_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司地址',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用：1启用 0停用',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `open_id_unique`(`open_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2088 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '2020-02-15 22:14:32', '2020-05-30 13:24:43', '超级管理员', 'egag', '', '123456', 1, 9, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (2, 'admin100', '2020-02-15 22:14:32', '2020-02-16 11:52:41', '管理员100', 'egag', NULL, '123456', 1, 9, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (3, 'admin101', '2020-02-15 22:14:32', '2020-02-16 11:58:44', '管理员101', 'egag', NULL, '123456', 1, 9, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (4, 'user201', '2020-02-15 22:14:32', '2020-02-15 22:14:35', '学生201', 'egag', NULL, '123456', 1, 1, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (5, 'user202', '2020-02-15 22:14:32', '2020-02-15 22:14:35', '学生202', 'egag', NULL, '123456', 1, 1, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (6, 'user203', '2020-02-15 22:14:32', '2020-02-16 12:21:22', '学生203', 'egag', NULL, '123456', 1, 1, NULL, '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (7, 'user204', '2020-02-16 12:16:10', '2020-02-16 12:16:10', '学生204', 'egag', NULL, '123456', 1, 1, '', '18588888888', 'softdev@xxx.com');
INSERT INTO `user` VALUES (8, 'user205', '2020-02-16 12:20:42', '2020-02-16 12:20:42', '学生205', 'egag', NULL, '123456', 1, 1, NULL, '18588888888', 'softdev@xxx.com');

SET FOREIGN_KEY_CHECKS = 1;
