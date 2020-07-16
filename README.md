# SpringBootCMS
![image](https://img.shields.io/badge/SpringBoot-%E2%98%85%E2%98%85%E2%98%85%E2%98%85%E2%98%85-blue.svg)
![image](https://img.shields.io/badge/MybatisPlus-%E2%98%85%E2%98%85%E2%98%85%E2%98%85%E2%98%85-blue.svg)
![image](https://img.shields.io/badge/Freemarker-%E2%98%85%E2%98%85%E2%98%85%E2%98%85%E2%98%85-blue.svg)
[![Build Status](https://travis-ci.org/moshowgame/SpringBootCMS.svg?branch=master)](https://travis-ci.org/moshowgame/SpringBootCMS)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

- 做个简单易用的SpringBootCMS
- BackEnd后台:SpringBoot2 + MybatisPlus3 + Freemarker
- AdminUI管理页面:LayMini2(based on LayUI)
- FrontEndUI前端页面:Bootstrap4

# 界面预览
[查看有道笔记](https://note.youdao.com/ynoteshare1/index.html?id=75b5e251f4c83215dec6428a03142995&type=note)

# Author
- Moshow郑锴@zhengkai.blog.csdn.net

#功能描述
- `用户`、`菜单`管理(二级菜单)功能。
- `表单`管理功能，提供动态表单定义与提交功能，可自定义项。
- `活动`管理功能，提供活动发布与签到功能。
- `文章`管理功能，提供文章列表、频道列表(二级目录)、附件管理功能。
- 权限模块SpringSecurity+`JWT`，解决session问题
- 首页根据`模板值`动态控制+赋值
- 通过`ehcache`进行缓存,页面模板值/文章内容
- 首页模板值输入`#+文章id`例如`#1`可直接引用文章链接（通过renderArticleLink()方法）
- 图片上传后自动设置为`@@@+图片名`,在编辑页面和文章节目会渲染对应文件路径（通过renderFileLink()方法）
- 从网络copy的文章，点击`网络图片转存本地`可以自动下载图片到本地并替换对应链接

#Dev INFO
- http://localhost:8888/cms/
- admin/123456
- sql文件位于`\src\main\resources\sql`
- 开发环境使用`application-dev.yml`配置，正式环境请用`prod`

#UPDATE
|日期|内容|
|----|----|
|2020-07-16|首页中的模板值设置可为特殊的`#+文章id`，自动通过renderArticleLink()方法渲染为系统内文章id。<br>文章中上传后的图片自动进行图片地址转换`@@@+图片名称`,并在编辑和文章浏览页面中自动通过renderFileLink()进行路径渲染。<br>拆分application.yml支持dev/prod环境。|
|2020-06-27|修复查看表单功能，以及表单提交优化。|
|2020-06-21|活动页面以及提交功能。表单页面以及提交功能。|
|2020-06-17|文章搜索功能。|
|2020-06-13|修复编辑模板不能刷新缓存的问题。|
|2020-06-12|频道手工分页功能。默认页面切换到前端，自动切到/page路由，后端/admin路由。|
|2020-06-11|1.layedit之base64图片自动转换上传(上传会变空白，编码正确，待检测)。<br>2.layedit之网络图片一键下载(完成，需要优化对本地图片的判断)。|
|2020-06-06|文章页面，频道页面。|
|2020-05-31|新增模板值列表，以及前端首页页面映射模板值。|
|2020-05-30|SpringSecurity+JWT，以及Session->JWT改造。EhCache实现以及优化。|
|2020-05-27|发布、停止优化。增加EhCache配置。|
|2020-05-26|更新文章和频道部分，以及layedit编辑器上传文件相关，各个功能均增加状态及其控制|
|2020-05-25|文章、频道部分基础代码|
|2020-05-24|init，初始化项目|
