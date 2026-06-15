# SpringBootCMS 改版规划

## 一、项目现状分析

### 1.1 技术栈现状（已升级部分标注 ✅）

| 模块 | 当前技术 | 升级后技术 | 状态 |
|------|---------|-----------|------|
| Spring Boot | 2.2.7.RELEASE | 3.x (Jakarta EE) | ✅ 已升级 |
| Java | JDK 1.8 | JDK 17+ | ✅ 已升级 |
| 容器 | Undertow | Tomcat (默认) | ✅ 已还原 |
| ORM | MyBatis-Plus 3.3.1 | MyBatis (原生XML) | ✅ 已迁移 |
| 数据库连接池 | Druid 1.1.22 | HikariCP (默认) | ✅ 已替换 |
| 缓存 | EhCache + JCache | Caffeine | ✅ 已替换 |
| 安全框架 | Spring Security + JWT | Spring Security + Session | ✅ 已重构 |
| 密码存储 | 明文比对 | BCrypt Hash | ✅ 已实现 |
| 数据库引擎 | MyISAM | InnoDB | ✅ 已迁移 |
| 逻辑删除 | 物理删除 | deleted字段软删除 | ✅ 已实现 |
| 模板引擎 | FreeMarker | FreeMarker | 保持 |
| 前端UI | Layui + Bootstrap3 | Bootstrap5 + jQuery3 | 🔄 规划中 |
| JSON | FastJSON | Jackson | ✅ 已替换 |
| 工具库 | Hutool + commons-lang3 | Apache Commons | ✅ 已统一 |
| 验证码 | Hutool CaptchaUtil | Kaptcha | ✅ 已替换 |
| 文件上传 | 自定义StorageService | 自定义StorageService | 保持 |

### 1.2 项目结构现状（已更新）

```
com.softdev.cms
├── config/          # 配置类
│   ├── WebSecurityConfig          # Spring Security + Session配置 ✅
│   ├── CacheConfig                # Caffeine缓存配置 ✅
│   ├── KaptchaConfig              # 验证码配置 ✅
│   ├── WebMvcConfig               # MVC配置
│   ├── ServerConfig               # 服务器配置
│   └── GlobalDefaultExceptionHandler # 全局异常处理
├── controller/      # 控制器（18个）
│   ├── BackEndController          # 后台登录/验证码/登出 ✅
│   ├── FrontEndController         # 前台页面+接口
│   ├── ArticleController          # 文章CRUD ✅ (含view_count递增)
│   ├── ChannelController          # 频道CRUD ✅
│   ├── MenuController             # 菜单CRUD+初始化
│   ├── ActivityController         # 活动CRUD ✅
│   ├── ActivitySignController     # 活动签到 ✅
│   ├── FormController             # 表单CRUD ✅
│   ├── FormItemController         # 表单项CRUD ✅
│   ├── FormSubmitController       # 表单提交 ✅
│   ├── FormSubmitValueController  # 表单提交值 ✅
│   ├── FileController             # 文件上传/下载
│   ├── TemplateController         # 模板CRUD ✅
│   ├── UserController             # 用户CRUD ✅ (BCrypt密码)
│   ├── MediaController            # 媒体资源管理 ✅ 新增
│   ├── AuditLogController         # 操作日志查询 ✅ 新增
│   ├── SiteConfigController       # 站点配置管理 ✅ 新增
│   ├── TagController              # 标签管理 ✅ 新增
│   └── IndexController            # 首页
├── entity/          # 实体类（16个 + DTO/异常子包）
│   ├── Article ✅ (新增slug/coverImage/viewCount/publishTime/deleted)
│   ├── Channel ✅ (新增slug/coverImage/description/channelType/deleted)
│   ├── Menu ✅ (新增deleted)
│   ├── User ✅ (新增email/companyName/companyAddress/openId/deleted)
│   ├── Activity ✅ (新增deleted)
│   ├── ActivitySign ✅ (新增deleted)
│   ├── Form ✅ (新增deleted)
│   ├── FormItem ✅ (新增placeholder/deleted)
│   ├── FormSubmit ✅ (新增auditUserId/auditUserName/auditTime/deleted)
│   ├── FormSubmitValue ✅ (新增deleted)
│   ├── Template ✅ (新增deleted)
│   ├── Media ✅ 新增 (媒体资源管理)
│   ├── AuditLog ✅ 新增 (操作日志)
│   ├── SiteConfig ✅ 新增 (站点全局配置)
│   ├── Tag ✅ 新增 (标签)
│   ├── ArticleTag ✅ 新增 (文章-标签关联)
│   ├── dto/  (QueryParamDTO, FormSubmitValueDTO, FormSubmitValueExcelDTO)
│   └── exception/ (StorageException, StorageFileNotFoundException)
├── mapper/          # Mapper接口（16个，原生MyBatis XML）✅
│   ├── ArticleMapper ✅ (含selectBySlug/incrementViewCount)
│   ├── ChannelMapper ✅ (含selectBySlug/selectByParentChannelId)
│   ├── UserMapper ✅ (含逻辑删除)
│   ├── MenuMapper ✅
│   ├── ActivityMapper ✅
│   ├── ActivitySignMapper ✅
│   ├── FormMapper ✅
│   ├── FormItemMapper ✅
│   ├── FormSubmitMapper ✅
│   ├── FormSubmitValueMapper ✅
│   ├── TemplateMapper ✅
│   ├── MediaMapper ✅ 新增
│   ├── AuditLogMapper ✅ 新增
│   ├── SiteConfigMapper ✅ 新增
│   ├── TagMapper ✅ 新增
│   └── ArticleTagMapper ✅ 新增
├── service/         # 服务类
│   ├── CmsUserDetailsService      # Spring Security用户详情 ✅
│   ├── FrontEndService            # 前台服务（含缓存）
│   └── StorageService/Impl        # 文件存储服务
└── util/            # 工具类
    ├── Result                      # 统一返回体 ✅
    └── AuditLogHelper              # 审计日志辅助 ✅ 新增
```

### 1.3 核心问题分析

1. ~~**JWT认证混乱**~~ ✅ 已解决：移除JWT，改为Session + Spring Security
2. ~~**安全漏洞**~~ ✅ 已解决：BCrypt密码、SQL参数化、Session认证
3. **UI过时** 🔄：后台使用Layui（已停止维护），前台Bootstrap3；编辑器使用LayEdit（功能简陋）
4. ~~**依赖老旧**~~ ✅ 已解决：Spring Boot 3.x、MyBatis原生、Jackson、Caffeine
5. ~~**缓存配置不一致**~~ ✅ 已解决：统一使用Caffeine + Spring Cache注解
6. ~~**数据库引擎MyISAM**~~ ✅ 已解决：全部迁移InnoDB
7. ~~**物理删除**~~ ✅ 已解决：全部改为deleted逻辑删除
8. ~~**缺少审计/配置/标签/媒体管理**~~ ✅ 已解决：新增4张表+对应代码

---

## 二、改版目标

### 2.1 总体目标

将SpringBootCMS升级为一个基于现代技术栈的、安全可靠的CMS系统，具备现代企业级UI体验、完善的权限管理、高效的缓存策略。

### 2.2 改版原则

- **安全优先**：消除所有已知安全漏洞，建立完善的权限体系
- **渐进式改造**：按模块分阶段推进，每个阶段可独立验证
- **最小化依赖**：移除冗余依赖，统一技术选型
- **前后端分离友好**：后端API设计RESTful化，为未来前后端分离预留空间

---

## 三、改版详细规划

### 阶段一：后端框架升级与依赖重构 ✅ 已完成

#### 3.1 Spring Boot 3 升级 ✅

- [x] `pom.xml` parent 升级为 `spring-boot-starter-parent` 3.x
- [x] Java 版本从 1.8 升级到 17+
- [x] `javax.servlet.*` → `jakarta.servlet.*`（Jakarta EE 迁移）
- [x] `javax.validation.*` → `jakarta.validation.*`
- [x] 移除 `springloaded` 依赖
- [x] 更新 `application.yml` 配置项

#### 3.2 容器与连接池还原 ✅

- [x] `pom.xml` 移除 `spring-boot-starter-undertow` 及其exclusion
- [x] `pom.xml` 移除 `druid-spring-boot-starter`
- [x] `application-dev.yml` 移除 Druid 专属配置
- [x] 保留HikariCP默认配置

#### 3.3 移除Hutool，统一使用Apache Commons ✅

- [x] `pom.xml` 移除 `cn.hutool:hutool-all`
- [x] `StringUtils.java` 中 Hutool引用 → Apache Commons / JDK原生
- [x] 验证码生成 → Kaptcha
- [x] `sun.misc.BASE64Decoder` → `java.util.Base64`

#### 3.4 FastJSON 替换 ✅

- [x] `pom.xml` 移除 `com.alibaba:fastjson`
- [x] `WebMvcConfig.java` 移除 `FastJsonHttpMessageConverter` 配置
- [x] 所有 `JSON.toJSONString()` → `ObjectMapper.writeValueAsString()`
- [x] 所有 `JSON.parseObject()` → `ObjectMapper.readValue()`

---

### 阶段二：认证体系重构 ✅ 已完成

#### 3.5 移除JWT，改为Session认证 ✅

- [x] `pom.xml` 移除 `io.jsonwebtoken:jjwt`
- [x] 删除 `JwtTokenUtil.java`、`JwtRequestFilter.java`、`JwtAuthenticationEntryPoint.java`、`JwtAuthenticationController.java`
- [x] 删除 `entity/jwt/` 子包（JwtRequest、JwtResponse、JwtUser）
- [x] 重写 `WebSecurityConfig.java`：Session管理 + 路径权限规则
- [x] 重写 `BackEndController.java`：Session认证 + Kaptcha验证码
- [x] 新增 `CmsUserDetailsService.java` 替代 JwtUserDetailsService

#### 3.6 游客权限与安全加固 ✅

- [x] 定义权限模型：游客(公开页面)、普通用户(roleId=1)、管理员(roleId=9)
- [x] `WebSecurityConfig.java` 配置路径权限
- [x] 密码安全：BCrypt加密存储和校验
- [x] SQL注入防护：`${page}` → `#{page}`，统一使用 `#{}`

---

### 阶段三：ORM重构 ✅ 已完成

#### 3.7 MyBatis升级与Mapper重写 ✅

- [x] `pom.xml` 移除 `mybatis-plus-boot-starter`，新增 `mybatis-spring-boot-starter` 3.x
- [x] 删除 `MybatisPlusConfig.java`
- [x] 所有Entity类移除MyBatis-Plus注解
- [x] 创建16个XML Mapper文件（`resources/mapper/*.xml`）
- [x] 所有Mapper接口重写：移除 `extends BaseMapper<T>`，定义标准CRUD方法
- [x] 所有Controller中的 `QueryWrapper` → Mapper方法参数传递
- [x] `application-dev.yml` 新增MyBatis配置

---

### 阶段四：数据库结构优化 ✅ 已完成

#### 3.8 存储引擎与字符集 ✅

- [x] 全部表 MyISAM → InnoDB（支持事务、行锁、外键）
- [x] 字符集统一 `utf8mb4` + `utf8mb4_general_ci`

#### 3.9 密码Hash存储 ✅

- [x] `WebSecurityConfig` 配置 `BCryptPasswordEncoder` Bean
- [x] `BackEndController.login()` 使用 `passwordEncoder.matches()` 校验
- [x] `UserController.save()` 使用 `passwordEncoder.encode()` 加密
- [x] DDL初始数据admin密码改为BCrypt hash

#### 3.10 新增表结构 ✅

| 表名 | 说明 | 状态 |
|------|------|------|
| media | 文件/图片资源管理表 | ✅ 已创建 |
| audit_log | 操作日志表 | ✅ 已创建 |
| site_config | 站点全局配置表 | ✅ 已创建 |
| tag | 标签表 | ✅ 已创建 |
| article_tag | 文章-标签关联表 | ✅ 已创建 |
| role | 角色表 | ✅ 已创建 |

对应代码：
- Entity: Media, AuditLog, SiteConfig, Tag, ArticleTag, Role ✅
- Mapper XML: MediaMapper.xml, AuditLogMapper.xml, SiteConfigMapper.xml, TagMapper.xml, ArticleTagMapper.xml ✅
- Mapper Java: MediaMapper, AuditLogMapper, SiteConfigMapper, TagMapper, ArticleTagMapper ✅
- Controller: MediaController, AuditLogController, SiteConfigController, TagController ✅

#### 3.11 Article表增强 ✅

- [x] `slug` - URL友好别名
- [x] `cover_image` - 封面图
- [x] `view_count` - 浏览量（含 `incrementViewCount` 方法）
- [x] `publish_time` - 发布时间（支持定时发布）

#### 3.12 全表审计字段 ✅

- [x] 所有表增加 `create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP`
- [x] 所有表增加 `update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`
- [x] 所有Entity增加 `createTime` / `updateTime` 字段（`@JsonFormat` 格式化）

#### 3.13 逻辑删除 ✅

- [x] 所有表增加 `deleted tinyint NOT NULL DEFAULT 0`
- [x] 所有Entity增加 `deleted` 字段
- [x] 所有Mapper XML SELECT 加 `AND deleted = 0` 条件
- [x] 所有Mapper XML JOIN 加 `AND t.deleted = 0` 条件
- [x] 所有 `deleteById` 改为 `UPDATE SET deleted = 1`
- [x] audit_log 表不需要 deleted 字段（日志只增不删）

#### 3.14 其他字段优化 ✅

- [x] User: 新增 email, company_name, company_address, open_id
- [x] Channel: 新增 slug, cover_image, description, channel_type
- [x] FormSubmit: 新增 audit_user_id, audit_user_name, audit_time
- [x] FormItem: 新增 placeholder

---

### 阶段五：缓存重构 ✅ 已完成

#### 3.15 Spring Boot默认缓存优化 ✅

- [x] `pom.xml` 移除 `javax.cache:cache-api` 和 `org.ehcache:ehcache`
- [x] 新增 `com.github.ben-manes.caffeine:caffeine`
- [x] 删除 `ehcache.xml`、`EhCacheUtil.java`
- [x] 新增 `CacheConfig.java`：Caffeine Cache Manager + 缓存策略

缓存策略：

| 缓存名 | TTL | 最大容量 | 说明 |
|--------|-----|---------|------|
| cache-page | 30min | 100 | 页面模板缓存 |
| cache-article | 15min | 500 | 文章内容缓存 |
| cache-channel | 60min | 50 | 频道列表缓存 |
| cache-activity | 10min | 100 | 活动列表缓存 |
| cache-form | 10min | 100 | 表单缓存 |

---

### 阶段六：UI改版 🔄 规划中

#### 3.16 后台管理UI改版（Layui → Bootstrap5 + jQuery3）

- [ ] 引入前端依赖：Bootstrap 5.3.x、jQuery 3.7.x、Bootstrap Icons、Bootstrap Table、Summernote/TinyMCE、Dropzone.js、Select2
- [ ] 设计后台布局模板：顶部导航栏 + 左侧菜单栏 + 主内容区
- [ ] 重写所有后台模板（`templates/cms/*.html`）
- [ ] 新增模板：media-list/edit、audit-log-list、site-config-list/edit、tag-list

#### 3.17 编辑友好性增强

- [ ] 富文本编辑器集成（Summernote推荐）
- [ ] 文件/图片上传组件优化
- [ ] 编辑体验优化（自动保存草稿、快捷键、全屏编辑）

#### 3.18 前台UI改版（Bootstrap3 → Bootstrap5）

- [ ] 前台模板重写
- [ ] 前台静态资源更新

---

### 阶段七：通用重构与优化 🔄 部分完成

#### 3.19 统一返回体与API规范 ✅

- [x] `ReturnT.java` 重构为泛型类 `Result<T>`
- [ ] API路径规范化（后台 `/admin/api/`，前台 `/api/`）
- [ ] 分页参数统一

#### 3.20 文件存储服务优化

- [ ] 文件名安全处理：UUID命名 + 保留原始扩展名
- [ ] 文件类型白名单校验
- [ ] 文件大小限制配置化（从site_config读取）
- [ ] 上传文件自动记录到media表

#### 3.21 验证码方案 ✅

- [x] 引入 `com.github.penggle:kaptcha`
- [x] 验证码存入Session，登录时校验

#### 3.22 审计日志集成 🔄

- [x] AuditLogHelper 工具类已创建
- [x] AuditLogController 查询接口已创建
- [ ] 各Controller操作方法中调用 AuditLogHelper.log() 记录日志
- [ ] 登录/登出自动记录审计日志

---

## 四、数据库表结构总览（v2.0）

### 4.1 表清单

| 表名 | 说明 | 引擎 | 逻辑删除 | 审计字段 |
|------|------|------|---------|---------|
| user | 用户表 | InnoDB | ✅ deleted | ✅ |
| role | 角色表 | InnoDB | ✅ deleted | ✅ |
| menu | 菜单表 | InnoDB | ✅ deleted | ✅ |
| channel | 频道/栏目表 | InnoDB | ✅ deleted | ✅ |
| article | 文章表 | InnoDB | ✅ deleted | ✅ |
| tag | 标签表 | InnoDB | ✅ deleted | ✅ |
| article_tag | 文章-标签关联表 | InnoDB | - | ✅ create_time |
| template | 模板配置表 | InnoDB | ✅ deleted | ✅ |
| activity | 活动表 | InnoDB | ✅ deleted | ✅ |
| activity_sign | 活动签到表 | InnoDB | ✅ deleted | ✅ |
| form | 表单模板表 | InnoDB | ✅ deleted | ✅ |
| form_item | 表单字段表 | InnoDB | ✅ deleted | ✅ |
| form_submit | 表单提交表 | InnoDB | ✅ deleted | ✅ |
| form_submit_value | 表单提交值表 | InnoDB | ✅ deleted | ✅ |
| media | 媒体资源表 | InnoDB | ✅ deleted | ✅ |
| audit_log | 操作日志表 | InnoDB | - (只增不删) | ✅ create_time |
| site_config | 站点全局配置表 | InnoDB | ✅ deleted | ✅ |

### 4.2 ER关系

```
user ──1:N── article (create_user_id)
user ──1:N── activity (create_user_id)
user ──1:N── activity_sign (user_id)
user ──1:N── form_submit (user_id)
user ──1:N── media (upload_user_id)
user ──1:N── audit_log (user_id)

channel ──1:N── article (channel_id)
channel ──1:N── channel (parent_channel_id, 自关联)

article ──M:N── tag (通过 article_tag 关联表)

form ──1:N── form_item (form_id)
form ──1:N── form_submit (form_id)
form_item ──1:N── form_submit_value (item_id)
form_submit ──1:N── form_submit_value (submit_id)

role ──1:N── user (role_id)
role ──1:N── menu (role_id, 逗号分隔)
```

---

## 五、依赖变更汇总

### 移除的依赖 ✅

| 依赖 | 原因 |
|------|------|
| `spring-boot-starter-undertow` | 恢复默认Tomcat |
| `druid-spring-boot-starter` | 恢复默认HikariCP |
| `hutool-all` | 统一使用Apache Commons |
| `mybatis-plus-boot-starter` | 改用原生MyBatis |
| `fastjson` | 改用Jackson |
| `jjwt` | JWT改Session |
| `javax.cache:cache-api` | 改用Caffeine |
| `org.ehcache:ehcache` | 改用Caffeine |
| `springloaded` | 已废弃 |

### 新增的依赖 ✅

| 依赖 | 用途 |
|------|------|
| `mybatis-spring-boot-starter` 3.x | 原生MyBatis |
| `caffeine` | 高性能缓存 |
| `commons-text` | Apache字符串工具 |
| `kaptcha` | 验证码生成 |
| `spring-boot-starter-validation` | 参数校验 |

### 保留的依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | 含默认Tomcat |
| `spring-boot-starter-security` | 安全框架 |
| `spring-boot-starter-freemarker` | 模板引擎 |
| `spring-boot-starter-cache` | 缓存抽象 |
| `spring-boot-devtools` | 开发热部署 |
| `lombok` | 代码简化 |
| `mysql-connector-j` | MySQL驱动 |
| `poi-ooxml` | Excel导入导出 |
| `commons-lang3` | Apache工具 |

---

## 六、实施进度

```
阶段一（基础框架）✅ → 阶段二（安全认证）✅ → 阶段三（ORM重构）✅ → 阶段四（数据库优化）✅ → 阶段五（缓存）✅ → 阶段六（UI）🔄 → 阶段七（优化）🔄
```

| 阶段 | 内容 | 优先级 | 状态 |
|------|------|--------|------|
| 阶段一 | Spring Boot 3 + 依赖清理 | P0 | ✅ 已完成 |
| 阶段二 | JWT→Session + 安全加固 | P0 | ✅ 已完成 |
| 阶段三 | MyBatis-Plus→MyBatis | P0 | ✅ 已完成 |
| 阶段四 | 数据库结构优化 | P0 | ✅ 已完成 |
| 阶段五 | EhCache→Caffeine缓存 | P1 | ✅ 已完成 |
| 阶段六 | UI改版 Bootstrap5 | P1 | 🔄 规划中 |
| 阶段七 | 通用优化 | P2 | 🔄 部分完成 |

---

## 七、风险与注意事项

1. ~~**Spring Boot 3 升级**~~ ✅ 已完成：javax→jakarta迁移已完成
2. ~~**MyBatis-Plus→MyBatis**~~ ✅ 已完成：所有Mapper已重写
3. **富文本编辑器** 🔄：LayEdit内容格式可能与新编辑器不兼容，需提供内容迁移方案
4. **数据迁移**：旧数据库需要执行 `springbootcms_v2.sql` 重建，旧数据需编写迁移脚本
5. **前端模板** 🔄：所有 `templates/cms/*.html` 需要从Layui迁移到Bootstrap5

---

## 八、下一步计划

1. **UI改版**：后台管理界面 Bootstrap5 化
2. **审计日志集成**：在Controller操作中调用AuditLogHelper
3. **文件上传优化**：上传自动记录media表、从site_config读取限制
4. **API规范化**：统一路径前缀、RESTful风格
5. **前端模板**：前台页面 Bootstrap5 化
