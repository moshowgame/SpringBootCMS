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

# Author
- Moshow郑锴@zhengkai.blog.csdn.net

#功能描述
- 用户、角色(普通用户1/管理员9)、菜单管理(二级菜单)功能。
- 表单管理功能，提供动态表单定义与提交功能，可自定义项。
- 活动管理功能，提供活动发布与签到功能。
- 文章管理功能，提供文章列表、频道列表(二级目录)、附件管理功能。
- 权限模块SpringSecurity+JWT模块，解决session问题
- 首页根据模板值动态控制+赋值

#LOGIN INFO
- http://localhost:8888/cms/
- admin/123456

#UPDATE
|日期|内容|
|----|----|
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
