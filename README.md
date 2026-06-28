# SpringBootCMS

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)
![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.x-red.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)
![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)
![Security](https://img.shields.io/badge/security-audited-success.svg)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

> **基于 Spring Boot 3 的现代化企业级内容管理系统（CMS）**
> 安全可靠、开箱即用、易于扩展，适用于政府/协会/中小企业官网及内部知识库

## 👤 Author

- Moshow 郑锴 @ [zhengkai.blog.csdn.net](https://zhengkai.blog.csdn.net)

---

## 项目简介

**SpringBootCMS** 是一套面向内容管理场景的**开箱即用**型 Web 平台，专注于解决以下痛点：

- 🏢 **政府/协会/企业官网搭建**：内置多频道、动态表单、活动签到等业务模块，无需从零开发
- 🛡️ **安全合规要求**：通过全系统安全审计（SQL 注入/XSS/CSRF/SSRF/RBAC 等 14 类风险全部加固）
- ⚡ **快速交付**：前后台开箱即用、自带 SEO 优化、模板化页面配置、数据库 DDL + 示例数据一键导入
- 🔧 **易于扩展**：原生 MyBatis XML Mapper、清晰的代码分层、丰富的工具类、完整的中文注释

项目自 2020 年发布以来已迭代多个版本，**v2.0 大版本**采用 Spring Boot 3 + Jakarta EE + Spring Security 6 等现代技术栈重构，从零重写了安全体系与审计体系。

## ✨ 核心特性一览

| | |
|---|---|
| 🎯 **现代化技术栈** | Spring Boot 3.x + Java 17 + MyBatis 3 + MySQL 8 |
| 🎨 **双端 UI** | 后台：jQuery 3 + Bootstrap 5；前台：Bootstrap 5 + AOS 动画 |
| 📝 **内容管理** | 文章/频道/标签/封面图/浏览量/URL Slug/定时发布 |
| 📋 **动态表单** | 字段类型自由配置、提交审核流程、匿名提交、专项表单 |
| 🎫 **活动管理** | 活动发布、签到/请假、前台匿名签到 |
| 🔍 **全文搜索** | 文章/活动/表单三类搜索 + LIKE 通配符转义 |
| 🖼️ **媒体库** | 图片/音视频/文档管理、网络图片转存（防 SSRF） |
| 📊 **站点配置** | 全局配置项分组管理（general/seo/email/upload） |
| 🔐 **安全防护** | 14 类攻击面全部加固，审计报告见 [SECURITY_AUDIT.md](file:///home/moshow/workspace/java/SpringBootCMS/SECURITY_AUDIT.md) |
| 🌐 **SEO 友好** | 页面级 meta 标签、Open Graph、JSON-LD 结构化数据 |
| 📝 **完整审计** | audit_log 表 + 17 表全字段审计 + 逻辑删除 |
| 🇨🇳 **国内优化** | Bootstrap/jQuery/Bootstrap Icons/AOS/TinyMCE 全部本地化 |

## 🏗️ 系统架构

```
┌─────────────────────────────────────────────────────────────────┐
│                          浏览器 (PC/Mobile)                       │
└────────────┬────────────────────────────────────┬───────────────┘
             │ HTTPS (TLS 1.2+)                   │
             │ HSTS / CSP / X-Frame-Options      │
┌────────────▼────────────────────────────────────▼───────────────┐
│              Spring Security 6 (认证/授权/CSRF 防护)              │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ AttackDetectionFilter (全局攻击检测 Filter)                 │   │
│  │   ├─ URL Query 参数                                         │   │
│  │   ├─ Form body                                              │   │
│  │   └─ JSON body (递归展开)                                    │   │
│  └──────────────────────────────────────────────────────────┘   │
└────────────┬────────────────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────────────────┐
│                  Spring MVC (DispatcherServlet)                 │
│  ┌─────────────┬─────────────┬─────────────┬──────────────┐   │
│  │ /admin/**   │ /page/**    │ /file/**    │  ...         │   │
│  │ 后台API     │ 前台API     │ 文件API     │  业务API     │   │
│  └─────────────┴─────────────┴─────────────┴──────────────┘   │
└────────────┬────────────────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────────────────┐
│   Service Layer (业务层)  +  AttackDetectionUtil (二级校验)       │
└────────────┬────────────────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────────────────┐
│   MyBatis 3 (XML Mapper, 100% #{} 参数化, 0 个 ${} 拼接)        │
└────────────┬────────────────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────────────────┐
│   MySQL 8.x (InnoDB, utf8mb4, 17 张表) + HikariCP 连接池         │
└─────────────────────────────────────────────────────────────────┘
```

## 📦 适用场景

| 场景 | 说明 |
|------|------|
| 🏛️ **政府/行业协会官网** | 内置频道、文章、表单、活动等模块，符合政府协会的庄重、正式风格 |
| 🏢 **中小企业官网** | 快速搭建企业宣传、新闻发布、产品展示、留言咨询 |
| 📚 **内部知识库** | 内部文章、标签分类、文档管理、权限控制 |
| 🎓 **培训/教育机构** | 活动发布、报名签到、动态表单收集 |
| 🛒 **活动门户** | 活动管理、签到统计、表单收集 |

## 技术栈

| 模块 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.x (Jakarta EE) |
| ORM | MyBatis (原生XML Mapper) |
| 安全框架 | Spring Security + Session |
| 密码存储 | BCrypt Hash |
| 缓存 | Caffeine |
| 数据库 | MySQL 8.x (InnoDB, utf8mb4) |
| 模板引擎 | FreeMarker |
| 连接池 | HikariCP |
| JSON | Jackson |
| 验证码 | Kaptcha |
| 前端UI(后台) | jQuery 3 + Bootstrap 5 + Bootstrap Icons |
| 前端UI(前台) | jQuery 3 + Bootstrap 5 + Bootstrap Icons + AOS (动画) |
| 富文本编辑器 | TinyMCE |

## 功能模块

### 核心功能
- **用户管理**：用户CRUD、角色权限、BCrypt密码加密
- **菜单管理**：二级菜单、角色权限控制
- **文章管理**：文章CRUD、频道分类、标签、封面图、浏览量统计、定时发布、URL Slug
- **频道管理**：二级频道、频道类型(列表/单页/外链)、URL Slug
- **模板管理**：页面模板值动态配置、`#+文章id` 引用文章链接

### 扩展功能
- **标签系统**：标签管理、文章-标签多对多关联
- **媒体资源管理**：文件/图片上传管理、媒体类型分类(image/video/audio/document)、网络图片转存
- **站点配置**：全局配置项、分组管理(general/seo/email/upload)
- **操作日志**：审计日志记录与查询、IP/UA追踪
- **活动管理**：活动发布与签到、签到统计、请假管理
- **表单管理**：动态表单定义与提交、字段类型(text/number/select/file/date等)、审核流程、专项表单

### 前台展示
- **首页**：Hero Banner + 统计计数器 + 服务卡片 + 最新动态 + 合作伙伴
- **频道页**：频道文章列表、面包屑导航、分页
- **文章页**：文章详情、相关推荐、SEO meta 标签
- **搜索页**：文章/活动/表单三类搜索
- **表单页**：动态字段渲染、匿名提交、提交结果展示
- **活动页**：活动详情、签到表单

### 安全特性

#### 认证与授权
- **Spring Security + Session 认证**（已迁移自 JWT）
- **BCrypt** 密码 Hash 存储与校验
- **RBAC 角色基础访问控制**：管理员专属接口（`/user/**`、`/menu/**`、`/siteConfig/**`、`/template/**`、`/auditLog/**`）限制 `hasRole("ADMIN")`
- **登录失败锁定**：连续 5 次失败锁定 10 分钟
- **图形验证码**（Kaptcha）防爆破
- **会话固定防护**：登录后 `migrateSession()` 重新生成 Session ID
- **单点登录**：`maximumSessions(1)`

#### 注入攻击防护
- **SQL 注入**：MyBatis 全量参数化（`#{}`），16 个 Mapper 零 `${}` 拼接
- **XSS（存储型）**：所有 FreeMarker `value=""` / `<textarea>` 动态值使用 `?html` 转义
- **XSS（反射型 JS）**：`CmsUtil.escapeHtml()` / `CmsUtil.escapeAttr()` 统一转义用户可控字段
- **SSRF 四层防护**：内网 host 校验 + DNS IP 校验 + 重定向目标校验 + 超时控制

#### CSRF
- **CookieCsrfTokenRepository**（前端 JS 可读，自动附加 `X-XSRF-TOKEN`）
- 公开接口（`/page/**`、`/file/files/*`、`/formSubmitValue/submit`、`/activitySign/save` 等）已豁免

#### 文件上传/下载
- **五层防护**（上传）：扩展名白名单 + MIME 校验 + 文件魔数 + 大小限制 + UUID 命名
- **路径遍历防护**（下载）：拒绝 `..` 等非法字符 + 解析后绝对路径根目录二次校验
- **MIME 黑名单**（下载）：禁止 HTML/SVG/XML/JS 可执行类型
- **`X-Content-Type-Options: nosniff`** 响应头防浏览器嗅探

#### 安全响应头
- `Content-Security-Policy`（CSP）
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: SAMEORIGIN`（防 clickjacking）
- `Strict-Transport-Security`（HSTS，HTTPS 环境生效）
- **401/403 JSON 响应**：自定义 `AuthenticationEntryPoint` 和 `AccessDeniedHandler`，AJAX 请求不重定向

#### 审计与日志
- **审计日志表**（`audit_log`）只增不删，记录用户/IP/UA/操作
- 17 张业务表全部含审计字段（`create_time`、`update_time`、`create_user_id` 等）
- **逻辑删除**（`deleted` 字段软删除，保留数据历史）

> 📋 详细审计报告见 [SECURITY_AUDIT.md](file:///home/moshow/workspace/java/SpringBootCMS/SECURITY_AUDIT.md)

## 数据库表结构 (v2.0)

共 17 张表，全部使用 InnoDB 引擎 + utf8mb4 字符集：

| 表名 | 说明 | 逻辑删除 | 审计字段 |
|------|------|---------|---------|
| user | 用户表 | ✅ | ✅ |
| role | 角色表 | ✅ | ✅ |
| menu | 菜单表 | ✅ | ✅ |
| channel | 频道/栏目表 | ✅ | ✅ |
| article | 文章表 | ✅ | ✅ |
| tag | 标签表 | ✅ | ✅ |
| article_tag | 文章-标签关联表 | - | ✅ |
| template | 模板配置表 | ✅ | ✅ |
| activity | 活动表 | ✅ | ✅ |
| activity_sign | 活动签到表 | ✅ | ✅ |
| form | 表单模板表 | ✅ | ✅ |
| form_item | 表单字段表 | ✅ | ✅ |
| form_submit | 表单提交表 | ✅ | ✅ |
| form_submit_value | 表单提交值表 | ✅ | ✅ |
| media | 媒体资源表 | ✅ | ✅ |
| audit_log | 操作日志表 | 只增不删 | ✅ |
| site_config | 站点全局配置表 | ✅ | ✅ |

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.x
- Maven 3.6+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/moshowgame/SpringBootCMS.git
   cd SpringBootCMS
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE springbootcms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
   ```

3. **导入表结构**
   - 表结构：`src/main/resources/sql/ddl/schema.sql`
   - 示例数据（可选）：`src/main/resources/sql/data/sample-data.sql`

4. **修改配置**
   - 编辑 `src/main/resources/application-dev.yml`，修改数据库连接信息

5. **启动项目**
   ```bash
   mvn spring-boot:run
   ```

6. **访问系统**
   - 后台：`http://localhost:8888/cms/admin/login`
   - 前台：`http://localhost:8888/cms/`
   - 默认账号：`admin` / `123456`（仅 dev 环境自动登录可用 `devLogin`）

### 常见问题
- **Q：启动后访问 404？** A：检查 `server.servlet.context-path=/cms` 配置，所有 URL 需带 `/cms` 前缀
- **Q：上传目录权限错误？** A：编辑 `application-dev.yml` 的 `file.upload.path` 指向有写权限的目录（如 `/home/xxx/upload-files/`）
- **Q：静态资源 404？** A：所有第三方库已本地化在 `src/main/resources/static/lib/`，确保 maven 编译时复制到 `target/classes/static/lib/`
- **Q：忘记 admin 密码？** A：使用 `BCryptPasswordEncoder` 重新生成哈希值，SQL 替换 `user.password` 字段

## 📁 项目结构

```
SpringBootCMS/
├── src/main/java/com/softdev/cms/
│   ├── config/          # 配置类 (Security, Cache, Kaptcha, Jackson, AttackFilter)
│   ├── controller/      # 控制器 (18 个：前后台 + 文件 + 表单)
│   ├── entity/          # 实体类 (16 个 + DTO)
│   ├── mapper/          # Mapper 接口 (16 个)
│   ├── service/         # 服务类 (UserDetails, FrontEnd, Storage)
│   └── util/            # 工具类 (Result, AttackDetectionUtil, CmsUtil)
│
├── src/main/resources/
│   ├── mapper/          # MyBatis XML Mapper (16 个，全部 #{} 参数化)
│   ├── sql/             # SQL 脚本
│   │   ├── ddl/         #   - DDL: schema.sql
│   │   └── data/        #   - 示例数据: sample-data.sql
│   ├── static/          # 静态资源
│   │   ├── lib/         #   - 第三方库 (jQuery/Bootstrap/Bootstrap Icons/AOS/TinyMCE)
│   │   ├── js/          #   - 自定义 JS (admin-common.js 含 CmsTable/CmsApi/CmsUtil)
│   │   ├── css/         #   - 样式 (admin.css, frontend.css)
│   │   └── img/         #   - 图片
│   ├── templates/       # FreeMarker 模板
│   │   ├── cms/         #   - 后台模板 (16 个 list/edit)
│   │   ├── frontend/    #   - 前台模板 (index, channel, article, search, form, activity)
│   │   └── common/      #   - 公共模板
│   ├── application.yml
│   └── application-dev.yml
│
├── SECURITY_AUDIT.md    # 安全审计报告 (14 类攻击面全部加固)
└── README.md            # 本文档
```

## 🆚 为什么选择 SpringBootCMS？

| 特性 | SpringBootCMS | 通用 CMS | WordPress |
|------|---------------|----------|-----------|
| 中文友好 | ✅ 完整中文注释+中文 UI | 视项目而定 | ❌ 需汉化 |
| 政府协会风格 | ✅ 内置模板 | ❌ 需定制 | ❌ 需定制 |
| 代码所有权 | ✅ 100% 开源 Apache 2.0 | 视项目而定 | ❌ GPL |
| 安全审计 | ✅ 14 类风险全部加固 | 视项目而定 | ⚠️ 第三方插件多 |
| 易于扩展 | ✅ 原生 MyBatis + 清晰分层 | 视项目而定 | ❌ 插件机制复杂 |
| 国内部署 | ✅ 全部静态资源本地化 | 视项目而定 | ❌ 需替换 CDN |
| 部署难度 | ✅ 一键启动 (java -jar) | 中等 | 需 PHP+MySQL+Apache |
| 技术栈现代化 | ✅ Spring Boot 3 + Java 17 | 视项目而定 | ❌ PHP 7+ |

## 🤝 贡献

欢迎提交 PR / Issue！提交前请确保：
- 代码符合项目编码规范
- 新增功能附中文注释
- 安全相关变更同步更新 [SECURITY_AUDIT.md](file:///home/moshow/workspace/java/SpringBootCMS/SECURITY_AUDIT.md)

## 更新日志

> **变更类型图例**：🆕 新增 | ⚡ 优化 | 🐛 修复 | 🔒 安全 | ♻️ 重构 | 📝 文档

### 🚀 v2.1.0 (2026-06-28) — 安全加固与攻击检测

#### 🔒 安全
- 🆕 **攻击检测体系**：实现三层防御（客户端预检 + 全局 Filter + 业务 endpoint），共 56+ 条规则覆盖 9 大类（SQL/XSS/SVG/命令/路径遍历/LDAP/NoSQL/XXE/SSRF）
- 🆕 **AttackDetectionFilter**：全局 HTTP 请求体攻击检测，支持 URL/Form/JSON 三种参数类型
- 🆕 **AttackDetectionUtil**：业务层攻击检测工具，与全局 Filter 协同工作
- 🆕 **CmsUtil.showAttackWarning()**：攻击拦截后自动弹出警告横幅（8 秒自动消失）
- 🆕 **RBAC 角色基础访问控制**：`/user/**`、`/menu/**`、`/siteConfig/**`、`/template/**`、`/auditLog/**` 等管理接口限制为 `hasRole("ADMIN")`
- 🆕 **登录失败锁定**：连续 5 次失败锁定 10 分钟，防止暴力破解
- 🆕 **攻击检测审计日志**：每次攻击拦截记录用户/IP/UA/URI/方法到 `audit_log` 表
- ⚡ **CSRF 防护增强**：使用 `CsrfTokenRequestAttributeHandler` 解决懒加载问题，确保 Cookie 立即下发
- ⚡ **文件下载存储型 XSS 防护**：MIME 黑名单（HTML/SVG/XML/JS）+ `X-Content-Type-Options: nosniff`
- ⚡ **路径遍历防护加固**：从 `..` 字符检查升级为绝对路径根目录二次校验
- 🐛 **修复 FreeMarker 模板 `?html` 应用于 Date 对象**导致的渲染异常
- 🐛 **修复 401/403 异常时 `response is already committed`** 错误

#### ⚡ 优化
- 🆕 **后台/前台前端 UI 统一**：从 Layui 迁移到 jQuery 3 + Bootstrap 5 + Bootstrap Icons
- 🆕 **日期格式兼容性**：HTML `datetime-local` 提交格式 (`yyyy-MM-dd'T'HH:mm`) 全局支持，新增 `FlexibleDateDeserializer` 自动识别 7 种日期格式
- 🆕 **搜索功能增强**：Activity/Form/ActivitySign Mapper 新增 `searchParam` 字段过滤，支持多字段全文搜索
- 🆕 **搜索攻击检测 UI**：4 个搜索页面（前台文章/活动/表单 + 后台签到）添加客户端预检和服务端警告
- 🆕 **LIKE 通配符转义**：搜索条件中 `%` `_` `\` 自动转义，防止通配符注入
- 🆕 **安全审计报告**：[SECURITY_AUDIT.md](file:///home/moshow/workspace/java/SpringBootCMS/SECURITY_AUDIT.md)（14 类攻击面全部加固）
- 🆕 **README.md 大幅扩充**：增加项目简介、核心特性、系统架构图、横向对比、Roadmap、FAQ 等章节

#### 🐛 修复
- 🐛 修复后台表单字段名与实体字段名不匹配（17 个 list/edit 模板）
- 🐛 修复 `pageAll` 缺少分页参数导致的 `LIMIT null, null` SQL 错误
- 🐛 修复后台模板 `<th>序号</th>` 缺失导致列错位
- 🐛 修复 `form_submit.status` 插入 NULL 违反 NOT NULL 约束
- 🐛 修复 `form_submit_value` 模板 XSS 漏洞（添加 `?xhtml` 转义）
- 🐛 修复 TinyMCE 图片路径 `@@@` 占位符处理（支持 5 种格式）
- 🐛 修复 `ServletInputStream` 一次性消费导致 Controller 拿不到 body
- 🐛 修复 CORS/ContextPath/上传路径等技术细节

---

### 🚀 v2.0.0 (2025-06) — 大版本重构

#### ♻️ 重构
- ⚡ **Spring Boot 2 → 3.x**：升级到 Jakarta EE 命名空间
- ⚡ **MyBatis-Plus → MyBatis 原生 XML**：100% `#{}` 参数化查询，零 `${}` 拼接
- ⚡ **JWT → Spring Security Session**：移除 JWT，回归传统 Session 认证
- ⚡ **EhCache → Caffeine**：现代化本地缓存
- ⚡ **数据库引擎 → InnoDB**：全部 17 张表使用 InnoDB + utf8mb4
- ⚡ **物理删除 → 逻辑删除**：`deleted` 字段软删除，保留数据历史

#### 🆕 新增
- 🆕 **媒体资源管理**：图片/音视频/文档统一管理
- 🆕 **操作日志（audit_log）**：所有写操作记录到审计日志表
- 🆕 **站点全局配置（site_config）**：分组管理（general/seo/email/upload）
- 🆕 **标签管理**：标签 CRUD + 文章-标签多对多关联
- 🆕 **BCrypt 密码 Hash**：替换明文密码存储
- 🆕 **全表审计字段**：17 张表全部含 `create_time`、`update_time`、`create_user_id` 等
- 🆕 **dev/prod 环境拆分**：配置文件按环境隔离

---

### 🚀 v1.3.0 (2020-07) — 模板与图片

#### 🆕 新增
- 🆕 首页模板值 `#+文章id` 引用文章链接
- 🆕 图片地址转换 `@@@+图片名` 占位符机制
- 🆕 dev/prod 环境配置拆分

---

### 🚀 v1.2.0 (2020-06) — 业务扩展

#### 🆕 新增
- 🆕 活动管理页面（活动发布、签到、请假）
- 🆕 表单管理页面（动态字段、提交、审核）
- 🆕 文章全文搜索
- 🆕 频道分页

---

### 🚀 v1.0.0 (2020-05) — 初始版本

#### 🆕 新增
- 🆕 **核心模块**：文章/频道/模板/用户/菜单 5 大模块 CRUD
- 🆕 **认证授权**：Spring Security + JWT
- 🆕 **缓存**：EhCache
- 🆕 **数据库**：MySQL 5.x（MyISAM 引擎，utf8mb4 字符集）
- 🆕 **后台 UI**：Layui + layuimini
- 🆕 **前台 UI**：原生 HTML + jQuery 1.x

---

> 📋 完整安全审计报告见 [SECURITY_AUDIT.md](file:///home/moshow/workspace/java/SpringBootCMS/SECURITY_AUDIT.md)


## 📄 License

[Apache License 2.0](file:///home/moshow/workspace/java/SpringBootCMS/LICENSE) — 商业友好，可自由使用、修改、分发
