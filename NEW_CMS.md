# SpringBootCMS 改版规划

## 一、项目现状分析

### 1.1 技术栈现状

| 模块 | 当前技术 | 版本 |
|------|---------|------|
| Spring Boot | spring-boot-starter-parent | 2.2.7.RELEASE |
| Java | JDK | 1.8 |
| 容器 | Undertow（已排除Tomcat） | - |
| ORM | MyBatis-Plus | 3.3.1 |
| 数据库连接池 | Druid | 1.1.22 |
| 缓存 | EhCache + JCache | - |
| 安全框架 | Spring Security + JWT (jjwt 0.9.1) | - |
| 模板引擎 | FreeMarker | - |
| 前端UI | Layui + layuimini（后台）+ Bootstrap3（前台） | - |
| JSON | FastJSON | 1.2.83 |
| 工具库 | Hutool 5.3.5 + Apache commons-lang3 3.9 | - |
| 文件上传 | 自定义StorageService（本地文件系统） | - |
| 验证码 | Hutool CaptchaUtil | - |

### 1.2 项目结构现状

```
com.softdev.cms
├── config/          # 配置类（7个）
│   ├── WebSecurityConfig          # Spring Security + JWT配置
│   ├── JwtRequestFilter           # JWT请求过滤器
│   ├── JwtAuthenticationEntryPoint # JWT认证入口
│   ├── WebMvcConfig               # MVC配置（FastJSON、拦截器已注释）
│   ├── UserLoginInterceptor       # 用户登录拦截器（已注释，未启用）
│   ├── MybatisPlusConfig          # MyBatis-Plus分页配置
│   ├── ServerConfig               # 服务器配置
│   └── GlobalDefaultExceptionHandler # 全局异常处理
├── controller/      # 控制器（14个）
│   ├── BackEndController          # 后台登录/验证码/登出
│   ├── JwtAuthenticationController # JWT认证接口
│   ├── FrontEndController         # 前台页面+接口
│   ├── ArticleController          # 文章CRUD
│   ├── ChannelController          # 频道CRUD
│   ├── MenuController             # 菜单CRUD+初始化
│   ├── ActivityController         # 活动CRUD
│   ├── ActivitySignController     # 活动签到
│   ├── FormController             # 表单CRUD
│   ├── FormItemController         # 表单项CRUD
│   ├── FormSubmitController       # 表单提交
│   ├── FormSubmitValueController  # 表单提交值
│   ├── FileController             # 文件上传/下载
│   ├── TemplateController         # 模板CRUD
│   ├── UserController             # 用户CRUD
│   └── IndexController            # 首页
├── entity/          # 实体类（11个 + DTO/异常/JWT子包）
│   ├── Article, Channel, Menu, User, Activity, ActivitySign
│   ├── Form, FormItem, FormSubmit, FormSubmitValue, Template
│   ├── dto/  (QueryParamDTO, FormSubmitValueDTO, FormSubmitValueExcelDTO)
│   ├── exception/ (StorageException, StorageFileNotFoundException)
│   └── jwt/  (JwtRequest, JwtResponse, JwtUser)
├── mapper/          # Mapper接口（11个，MyBatis-Plus BaseMapper）
├── service/         # 服务类（4个）
│   ├── FrontEndService            # 前台服务（含缓存）
│   ├── JwtUserDetailsService      # JWT用户详情服务
│   ├── StorageService/Impl        # 文件存储服务
└── util/            # 工具类（5个）
    ├── JwtTokenUtil               # JWT工具
    ├── EhCacheUtil                # 缓存常量
    ├── ReturnT                    # 统一返回
    ├── StringUtils                # 字符串工具（含Base64图片、URL编码）
    └── MD5Util                    # MD5工具
```

### 1.3 核心问题分析

1. **JWT认证混乱**：JWT和Session混用，`BackEndController.login()` 生成JWT Token但Session相关代码被注释；`UserLoginInterceptor` 被注释未启用；前端通过URL参数`?token=xxx`传递JWT，既不安全也不规范
2. **安全漏洞**：SQL注入风险（Mapper中`${page}`直接拼接）、密码明文存储和传输、JWT secret硬编码在配置文件、验证码校验被注释掉
3. **UI过时**：后台使用Layui（已停止维护），前台Bootstrap3；编辑器使用LayEdit（功能简陋）
4. **依赖老旧**：Spring Boot 2.2.7、MyBatis-Plus 3.3.1、FastJSON有安全漏洞、Hutool工具与Apache Commons重复
5. **缓存配置不一致**：EhCache XML配置与Spring Cache注解混用，缓存策略不统一

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

### 阶段一：后端框架升级与依赖重构

#### 3.1 Spring Boot 4 升级

**目标**：从 Spring Boot 2.2.7 升级到 Spring Boot 4.x

**变更清单**：
- [ ] `pom.xml` parent 升级为 `spring-boot-starter-parent` 4.x
- [ ] Java 版本从 1.8 升级到 17+（Spring Boot 4 最低要求）
- [ ] `javax.servlet.*` → `jakarta.servlet.*`（Jakarta EE 迁移）
- [ ] `javax.validation.*` → `jakarta.validation.*`
- [ ] 移除 `springloaded` 依赖（已废弃，Spring Boot DevTools 已包含热部署功能）
- [ ] 更新 `application.yml` 配置项（部分配置key已变更）
- [ ] 移除 `spring.http.encoding` 配置（Spring Boot 4 已内置UTF-8）

#### 3.2 容器与连接池还原

**目标**：移除Undertow，恢复Tomcat；移除Druid，使用Spring Boot默认HikariCP

**变更清单**：
- [ ] `pom.xml` 移除 `spring-boot-starter-undertow` 及其exclusion
- [ ] `pom.xml` 移除 `druid-spring-boot-starter`
- [ ] `application-dev.yml` 移除 `spring.datasource.type: com.alibaba.druid.pool.DruidDataSource`
- [ ] `application-dev.yml` 移除 Druid 专属配置（`initialSize/minIdle/maxActive/maxWait`等）
- [ ] `application-dev.yml` 移除 Druid 监控配置（`web-stat-filter/stat-view-servlet`）
- [ ] 保留HikariCP默认配置，仅按需调整 `spring.datasource.hikari.*` 参数

#### 3.3 移除Hutool，统一使用Apache Commons

**目标**：移除 `hutool-all` 依赖，所有工具类改用 Apache Commons

**变更清单**：
- [ ] `pom.xml` 移除 `cn.hutool:hutool-all`
- [ ] 新增 `org.apache.commons:commons-text`（字符串处理增强）
- [ ] 新增 `org.apache.commons:commons-imaging`（如需图片处理）
- [ ] `StringUtils.java` 中 `cn.hutool.core.net.URLEncoder` → `java.net.URLEncoder`
- [ ] `StringUtils.java` 中 `cn.hutool.http.HttpUtil` → `org.apache.http.client` 或 `java.net.HttpClient`
- [ ] `BackEndController.java` 验证码生成 `cn.hutool.captcha.CaptchaUtil` → 自定义或 `com.github.cage:cage` / `kaptcha`
- [ ] `FrontEndController.java` 日期判断 `cn.hutool.core.date.DateUtil.isIn()` → 自行实现或Apache Commons Lang
- [ ] `sun.misc.BASE64Decoder` → `java.util.Base64`

#### 3.4 FastJSON 替换

**目标**：移除 FastJSON，使用 Jackson（Spring Boot 默认）

**变更清单**：
- [ ] `pom.xml` 移除 `com.alibaba:fastjson`
- [ ] `WebMvcConfig.java` 移除 `FastJsonHttpMessageConverter` 配置
- [ ] 所有 `JSON.toJSONString()` → `ObjectMapper.writeValueAsString()`
- [ ] 所有 `JSON.parseObject()` → `ObjectMapper.readValue()`
- [ ] 所有 `JSON.parseArray()` → `ObjectMapper.readValue(..., new TypeReference<>())`
- [ ] 所有 `JSONObject/JSONArray` 操作 → 使用 `Map/List` 或自定义DTO

---

### 阶段二：认证体系重构（JWT → Session）

#### 3.5 移除JWT，改为Session认证

**目标**：移除JWT认证体系，改为传统Session + Spring Security方式

**变更清单**：
- [ ] `pom.xml` 移除 `io.jsonwebtoken:jjwt`
- [ ] 删除 `JwtTokenUtil.java`
- [ ] 删除 `JwtRequestFilter.java`
- [ ] 删除 `JwtAuthenticationEntryPoint.java`
- [ ] 删除 `JwtAuthenticationController.java`
- [ ] 删除 `entity/jwt/JwtRequest.java`、`JwtResponse.java`、`JwtUser.java`
- [ ] 重写 `WebSecurityConfig.java`：
  - 启用Session管理（`SessionCreationPolicy.IF_REQUIRED`）
  - 配置CSRF保护（针对Session认证必须）
  - 配置登录/登出URL
  - 配置路径权限规则
- [ ] 重写 `BackEndController.java`：
  - `login()` 方法改为Session认证，通过 `AuthenticationManager` 验证
  - 登录成功后将用户信息存入Session
  - `logout()` 使用Spring Security的logout机制
  - `captcha()` 改用非Hutool的验证码库
- [ ] 重写 `UserLoginInterceptor.java`：
  - 启用拦截器，校验Session中是否存在登录用户
  - 未登录请求一律重定向到登录页或返回401
- [ ] `WebMvcConfig.java` 启用 `UserLoginInterceptor` 拦截器注册
- [ ] `MenuController.initMenu()` 移除token参数传递，改从Session获取用户角色

#### 3.6 游客权限与安全加固

**目标**：完善权限管理，游客一律Access Denied，防止注入攻击

**变更清单**：
- [ ] 定义权限模型：
  - **游客（未登录）**：仅可访问前台公开页面（`/page/**`、`/`、`/static/**`）
  - **普通用户（roleId=1）**：基础后台功能
  - **管理员（roleId=9）**：全部后台功能
- [ ] `WebSecurityConfig.java` 配置路径权限：
  - `/admin/login`、`/admin/captcha` → permitAll
  - `/page/**`、`/static/**`、`/` → permitAll（前台公开）
  - `/admin/**`、`/article/**`、`/channel/**` 等后台API → authenticated
  - 敏感操作（删除、权限修改）→ 需要ADMIN角色
- [ ] SQL注入防护：
  - Mapper中 `${page}` → `#{page}`，使用MyBatis分页插件替代手工分页
  - 所有 `@Select` 中的动态SQL改用XML Mapper或 `@SelectProvider`
  - 移除所有 `${}` 拼接，统一使用 `#{}`
- [ ] XSS防护：
  - 配置Spring Security Headers
  - 前端富文本内容做服务端sanitize
- [ ] CSRF防护：启用Spring Security CSRF Token
- [ ] 密码安全：使用BCrypt加密存储（当前`UserMapper.login()`明文比对需改为加密比对）
- [ ] 文件上传安全：校验文件类型、大小、文件名净化

---

### 阶段三：ORM重构（MyBatis-Plus → MyBatis）

#### 3.7 MyBatis升级与Mapper重写

**目标**：移除MyBatis-Plus，升级为MyBatis最新版，所有Mapper重写为标准MyBatis Mapper

**变更清单**：
- [ ] `pom.xml` 移除 `com.baomidou:mybatis-plus-boot-starter`
- [ ] `pom.xml` 新增 `org.mybatis.spring.boot:mybatis-spring-boot-starter` 3.x
- [ ] 删除 `MybatisPlusConfig.java`（MyBatis-Plus分页插件配置）
- [ ] 新增 `MyBatisConfig.java`：
  - 配置 `MapperScan`
  - 配置MyBatis分页插件（`PageInterceptor`）
  - 配置驼峰映射等
- [ ] 所有Entity类移除MyBatis-Plus注解：
  - `@TableId(type = IdType.AUTO)` → 删除
  - `@TableField(exist = false)` → 使用 `@Transient` 或MyBatis的 `resultMap` 处理
- [ ] 创建XML Mapper文件（`resources/mapper/*.xml`），每个Mapper对应一个XML：
  - `ArticleMapper.xml`：包含 pageAll、countAll、selectByCondition 等SQL
  - `UserMapper.xml`：包含 login、selectByUserName 等
  - `ChannelMapper.xml`
  - `MenuMapper.xml`
  - `ActivityMapper.xml`
  - `ActivitySignMapper.xml`
  - `FormMapper.xml`
  - `FormItemMapper.xml`
  - `FormSubmitMapper.xml`
  - `FormSubmitValueMapper.xml`
  - `TemplateMapper.xml`
- [ ] 所有Mapper接口重写：
  - 移除 `extends BaseMapper<T>`
  - 移除 `@Select` 注解SQL（迁移到XML）
  - 定义标准CRUD方法：`insert`、`updateById`、`deleteById`、`selectById`、`selectPage`等
- [ ] 所有Controller中的 `QueryWrapper` 用法 → 改为Mapper方法参数传递
- [ ] 所有Controller中的 `selectPage(Page, Wrapper)` → 改为MyBatis分页 `PageHelper` 或 `RowBounds`
- [ ] `application-dev.yml` 新增MyBatis配置：
  ```yaml
  mybatis:
    mapper-locations: classpath:mapper/*.xml
    type-aliases-package: com.softdev.cms.entity
    configuration:
      map-underscore-to-camel-case: true
  ```

---

### 阶段四：缓存重构

#### 3.8 Spring Boot默认缓存优化

**目标**：移除EhCache，使用Spring Boot默认缓存（ConcurrentMapCache或Caffeine），针对CMS场景优化

**变更清单**：
- [ ] `pom.xml` 移除 `javax.cache:cache-api` 和 `org.ehcache:ehcache`
- [ ] 新增 `com.github.ben-manes.caffeine:caffeine`（Spring Boot默认推荐的高性能缓存）
- [ ] 删除 `ehcache.xml`
- [ ] 删除 `EhCacheUtil.java`（缓存常量类）
- [ ] 新增 `CacheConfig.java`：
  - 配置Caffeine Cache Manager
  - 定义缓存策略：TTL、最大容量、淘汰策略
- [ ] 缓存策略设计：
  | 缓存名 | TTL | 最大容量 | 说明 |
  |--------|-----|---------|------|
  | cache-page | 30min | 100 | 页面模板缓存 |
  | cache-article | 15min | 500 | 文章内容缓存 |
  | cache-channel | 60min | 50 | 频道列表缓存 |
  | cache-activity | 10min | 100 | 活动列表缓存 |
  | cache-form | 10min | 100 | 表单缓存 |
- [ ] `FrontEndService.java` 缓存注解更新：
  - `@Cacheable` 的 value/key 保持语义一致
  - 新增文章列表缓存（高频访问）
  - 新增频道树缓存
- [ ] 页面级缓存：
  - 对前台页面（`/page/index`、`/page/channel/*`、`/page/article/*`）增加HTTP缓存头
  - 使用 `@Cacheable` 缓存页面ModelAndView数据
  - 后台编辑/删除操作时 `@CacheEvict` 清除对应缓存

---

### 阶段五：UI改版

#### 3.9 后台管理UI改版（Layui → Bootstrap5 + jQuery3）

**目标**：后台管理界面从Layui/layuimini迁移到Bootstrap5 + jQuery3，符合现代企业科技感

**变更清单**：
- [ ] 引入前端依赖：
  - Bootstrap 5.3.x CSS/JS
  - jQuery 3.7.x
  - Bootstrap Icons / Font Awesome 6
  - Bootstrap Table（数据表格）
  - Summernote 或 TinyMCE（富文本编辑器）
  - Dropzone.js 或 Bootstrap FileInput（文件上传）
  - Select2（下拉选择增强）
- [ ] 设计后台布局模板：
  - 顶部导航栏（深色/渐变，品牌Logo + 用户信息 + 退出）
  - 左侧菜单栏（可折叠，支持多级菜单，图标+文字）
  - 主内容区（面包屑 + 标签页 + 内容）
  - 响应式设计（移动端适配）
- [ ] 重写所有后台模板（`templates/cms/*.html`）：
  - `login.html`：现代登录页（渐变背景、卡片式表单、验证码）
  - `index.html`：后台主框架（侧边栏+顶栏+内容区）
  - `welcome.html`：仪表盘首页（统计卡片+快捷入口）
  - `article-list.html`：文章列表（Bootstrap Table + 搜索栏 + 操作按钮）
  - `article-edit.html`：文章编辑（富文本编辑器 + 图片上传 + 频道选择）
  - `channel-list.html` / `channel-edit.html`
  - `menu-list.html` / `menu-edit.html`
  - `activity-list.html` / `activity-edit.html`
  - `activitySign-list.html` / `activitySign-edit.html`
  - `form-list.html` / `form-edit.html`
  - `formItem-list.html` / `formItem-edit.html`
  - `formSubmit-list.html`
  - `formSubmitValue-display.html` / `formSubmitValue-export.html`
  - `template-list.html` / `template-edit.html`
  - `user-list.html` / `user-edit.html` / `user-info.html`
  - `error.html` / `404.html` / `success.html`

#### 3.10 编辑友好性增强

**目标**：后台编辑界面支持富文本编辑、文件/图片上传、编辑体验优化

**变更清单**：
- [ ] 富文本编辑器集成（Summernote推荐，Bootstrap原生风格）：
  - 工具栏：标题、加粗、斜体、列表、链接、图片、代码块、表格
  - 图片上传：拖拽上传 + 粘贴上传 + 点击上传
  - 图片自动转存本地（替代当前"网络图片转存"按钮）
  - Word/Excel内容粘贴支持
- [ ] 文件/图片上传组件：
  - 拖拽上传 + 点击上传
  - 多文件上传支持
  - 上传进度条
  - 文件类型/大小校验
  - 图片预览
- [ ] 编辑体验优化：
  - 自动保存草稿（localStorage）
  - 表单离开提示（未保存提醒）
  - 快捷键支持（Ctrl+S保存）
  - 全屏编辑模式

#### 3.11 前台UI改版（Bootstrap3 → Bootstrap5）

**目标**：前台展示页面从Bootstrap3升级到Bootstrap5，符合现代科技感

**变更清单**：
- [ ] 前台模板重写（`templates/frontend/*.html`）：
  - `index.html`：首页（Hero Banner + 频道导航 + 最新文章 + 特色内容）
  - `channel.html`：频道页（文章列表 + 侧边栏）
  - `article.html`：文章详情（正文 + 目录 + 相关文章）
  - `search.html`：搜索页
  - `activity-search.html` / `activity-sign.html`
  - `form-search.html` / `form-sign.html`
- [ ] 前台静态资源更新：
  - Bootstrap 3 → Bootstrap 5
  - Font Awesome 5 → Font Awesome 6
  - 移除不再需要的第三方库（ace-responsive-menu等，Bootstrap5自带navbar）
  - AOS动画库保留或替换为Bootstrap5 ScrollSpy

---

### 阶段六：通用重构与优化

#### 3.12 统一返回体与API规范

**变更清单**：
- [ ] `ReturnT.java` 重构为泛型类 `Result<T>`：
  ```java
  public class Result<T> {
      private int code;
      private String msg;
      private T data;
      private long count; // 分页用
  }
  ```
- [ ] API路径规范化：
  - 后台API统一前缀 `/admin/api/`
  - 前台API统一前缀 `/api/`
  - RESTful风格：GET查询、POST新增、PUT修改、DELETE删除
- [ ] 分页参数统一：`page`/`size` 替代 `page`/`limit`

#### 3.13 文件存储服务优化

**变更清单**：
- [ ] `StorageService` 接口保持不变，实现类优化
- [ ] 文件名安全处理：UUID命名 + 保留原始扩展名
- [ ] 文件类型白名单校验
- [ ] 文件大小限制配置化
- [ ] `FileController` 接口简化，移除冗余的base64和网络图片下载接口（由富文本编辑器统一处理）

#### 3.14 验证码方案

**变更清单**：
- [ ] 引入 `com.github.penggle:kaptcha` 或自定义SVG验证码
- [ ] 验证码存入Session，登录时校验
- [ ] 支持数学运算验证码（更安全）

#### 3.15 数据库脚本更新

**变更清单**：
- [ ] 存储引擎从 MyISAM → InnoDB（支持事务）
- [ ] 字符集统一 `utf8mb4`
- [ ] 添加外键约束和索引优化
- [ ] 密码字段改为BCrypt加密存储
- [ ] 添加 `created_at`/`updated_at` 默认值

---

## 四、依赖变更汇总

### 移除的依赖

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

### 新增的依赖

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
| `mysql-connector-j` | MySQL驱动（新artifactId） |
| `poi-ooxml` | Excel导入导出 |
| `commons-lang3` | Apache工具 |

---

## 五、实施顺序与优先级

```
阶段一（基础框架）→ 阶段二（安全认证）→ 阶段三（ORM重构）→ 阶段四（缓存）→ 阶段五（UI）→ 阶段六（优化）
```

| 阶段 | 内容 | 优先级 | 依赖 |
|------|------|--------|------|
| 阶段一 | Spring Boot 4 + 依赖清理 | P0 | 无 |
| 阶段二 | JWT→Session + 安全加固 | P0 | 阶段一 |
| 阶段三 | MyBatis-Plus→MyBatis | P0 | 阶段一 |
| 阶段四 | EhCache→Caffeine缓存 | P1 | 阶段一 |
| 阶段五 | UI改版 Bootstrap5 | P1 | 阶段二 |
| 阶段六 | 通用优化 | P2 | 阶段二+三 |

---

## 六、风险与注意事项

1. **Spring Boot 4 升级**：javax→jakarta迁移是最大的破坏性变更，需要全局替换import
2. **MyBatis-Plus→MyBatis**：所有 `QueryWrapper`/`BaseMapper` 用法需要重写，工作量较大
3. **密码加密迁移**：现有明文密码需要一次性迁移为BCrypt，需提供迁移脚本
4. **Session认证**：需注意分布式部署场景，如需集群部署需引入Spring Session + Redis
5. **CSRF保护**：启用CSRF后，所有POST请求需携带CSRF Token，前端需统一处理
6. **前端模板重写**：Layui组件（table/form/layer）需逐一替换为Bootstrap5对应组件
7. **富文本编辑器**：LayEdit内容格式可能与新编辑器不兼容，需提供内容迁移方案
