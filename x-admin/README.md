# 🚀 毕设脚手架 x-admin

#### 🛫 介绍
毕设脚手架工程，拿来即用，简单便捷，专为新手小白/毕设困难户打造，货真价实，童叟无欺

#### 👑 核心功能
自动生成代码，可以根据数据库表自动生成后台增删改查接口和前台的html代码，不用写代码就能做出一个管理系统！

目前系统可以根据后台数据库的特定字段自动生成 `文本框`、 `下拉框（带数据关联）`、`日期控件（日期和日期时间控件）`、`单选按钮`、`文件上传（表格可直接显示图片）`等，方便舒适。


#### 🚂 软件架构
Java后台：SpringBoot 2.1.0 + Mybatis-plus+ hutool工具包 + Apache poi

前台页面：Vue2.0 + ElementUI + Jquery + tinymce（富文本插件）

数据库：Mysql5.7+

**注意：JDK选择`1.8`版本，Mysql选择`5.7`版本**

前后端分离，页面可单独部署，默认放在项目的static文件夹，随后端工程一起访问。


#### 🚁 安装教程

1. 使用git下载本项目

    `https://gitee.com/xqnode/x-admin.git`

    或者直接下载zip解压

    ![下载zip](https://www.hualigs.cn/image/60a46392bf2a1.jpg)

2. 使用idea打开x-admin文件夹导入工程

3. 配置maven，下载项目依赖
   
   ![](https://www.hualigs.cn/image/60a463ef03442.jpg)
   
4. 新建数据库x-admin，执行x-admin.sql
   
   ![](https://www.hualigs.cn/image/60a4dec4a0c66.jpg)
   ![](https://www.hualigs.cn/image/60a4645016b23.jpg)
   
5. 修改application.yml中的数据库密码
   
   ![](https://www.hualigs.cn/image/60a4648a823b5.jpg)
   
6. 项目依赖下载完成后，直接运行 Application 启动SpringBoot即可
   
   ![](https://www.hualigs.cn/image/60a4650b7d904.jpg)
   
7. 运行效果   
   
   ![](https://www.hualigs.cn/image/60a464bbed518.jpg)

#### 🛸 使用说明

1. 登录页面请访问：http://localhost:9999/page/end/login.html
2. 账号：admin，密码：admin

#### 🎨 界面截图

登录

![](https://www.hualigs.cn/image/60a465c7e7d79.jpg)

注册

![](https://www.hualigs.cn/image/60a46643d0549.jpg)

项目主页

![](https://www.hualigs.cn/image/60a46662b622e.jpg)

用户管理

![](https://www.hualigs.cn/image/60a4675546135.jpg)

角色管理

![](https://www.hualigs.cn/image/60a46755483e2.jpg)

菜单管理

![](https://www.hualigs.cn/image/60a4675548b33.jpg)

公告管理

![](https://www.hualigs.cn/image/60a467ab49e80.jpg)

日志管理

![](https://www.hualigs.cn/image/60a467cfe30e7.jpg)

在线留言（默认隐藏了）

![](https://www.hualigs.cn/image/60a46805c6ea1.jpg)

个人信息页

![](https://www.hualigs.cn/image/60a4714bcc446.jpg)


#### 🏎 问题交流
付费QQ群：929481338  <br>
提供x-admin项目问题解答、技术指导，分享实战项目，带大家做项目。<br>
一次性收费99元，长期享受VIP待遇。

我的微信：

![](https://img-blog.csdnimg.cn/20201030174103759.jpg#pic_center)

#### 🛹 请作者喝杯咖啡

![](https://img-blog.csdnimg.cn/2021032107143511.jpg)
