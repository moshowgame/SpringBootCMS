# SpringBootCMS

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)
![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![MyBatis](https://img.shields.io/badge/MyBatis-3.x-red.svg)
![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)

基于 Spring Boot 3 的内容管理系统，使用现代技术栈重构，安全可靠、易于扩展。

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
| 前端UI(后台) | Layui / layuimini (规划迁移至 Bootstrap5) |
| 前端UI(前台) | Bootstrap (规划迁移至 Bootstrap5) |

## 功能模块

### 核心功能
- **用户管理**：用户CRUD、角色权限、BCrypt密码加密
- **菜单管理**：二级菜单、角色权限控制
- **文章管理**：文章CRUD、频道分类、标签、封面图、浏览量统计、定时发布、URL Slug
- **频道管理**：二级频道、频道类型(列表/单页/外链)、URL Slug
- **模板管理**：页面模板值动态配置、`#+文章id` 引用文章链接

### 扩展功能
- **标签系统**：标签管理、文章-标签多对多关联
- **媒体资源管理**：文件/图片上传管理、媒体类型分类(image/video/audio/document)
- **站点配置**：全局配置项、分组管理(general/seo/email/upload)
- **操作日志**：审计日志记录与查询、IP/UA追踪
- **活动管理**：活动发布与签到
- **表单管理**：动态表单定义与提交、审核流程

### 安全特性
- Spring Security + Session 认证
- BCrypt 密码 Hash 存储与校验
- 逻辑删除（deleted字段软删除，保留数据历史）
- SQL参数化（`#{}` 防注入）
- 验证码（Kaptcha）

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
   - 后台：http://localhost:8888/admin/login
   - 前台：http://localhost:8888/
   - 默认账号：admin / 123456

## 项目结构

```
src/main/java/com/softdev/cms/
├── config/          # 配置类 (Security, Cache, Kaptcha, MVC)
├── controller/      # 控制器 (18个)
├── entity/          # 实体类 (16个 + DTO)
├── mapper/          # Mapper接口 (16个)
├── service/         # 服务类
└── util/            # 工具类 (Result, AuditLogHelper)

src/main/resources/
├── mapper/          # MyBatis XML Mapper (16个)
├── sql/             # DDL脚本
├── static/          # 静态资源 (CSS/JS/图片)
├── templates/       # FreeMarker模板
├── application.yml
└── application-dev.yml
```

## 更新日志

| 日期 | 内容 |
|------|------|
| 2025-06 | **v2.0 大版本重构**：Spring Boot 3.x升级、MyBatis-Plus→MyBatis原生、JWT→Session认证、BCrypt密码、InnoDB引擎、逻辑删除、Caffeine缓存、新增媒体/日志/配置/标签管理、全表审计字段 |
| 2020-07 | 首页模板值`#+文章id`引用、图片地址转换`@@@+图片名`、dev/prod环境拆分 |
| 2020-06 | 活动页面、表单页面、文章搜索、频道分页 |
| 2020-05 | 初始版本：SpringSecurity+JWT、EhCache、文章/频道/模板/用户/菜单管理 |

## Author

- Moshow郑锴 @ [zhengkai.blog.csdn.net](https://zhengkai.blog.csdn.net)

## License

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)
