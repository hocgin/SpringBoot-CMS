
## @当前进程
- 站内信系统
- Service 整体整改
- 底层抽离(模块划分) 分布式规范
- 微信公众号接入

## 问题
- IUser 直接存储 User 对象 容易出问题
- 每日去除未验证用户定时任务

# 控制面板
- 关闭应用
 
 
 
-----------
## 监控
- /env
> 环境变量

- /configprops
> 配置对象

- /autoconfig

- /mapping
> url 映射信息

- /info
> 应用程序的基本描述

- /health
> 健康状况

- /metrics
> 展示当前应用的’指标’信息

- /trace
> 显示trace信息（默认为最新的一些HTTP请求）

- /shutdown
> 允许应用以优雅的方式关闭（默认情况下不启用）

- /dump
> 执行一个线程转储

- /configprops
> 显示一个所有@ConfigurationProperties的整理列表

- /beans
> 显示一个应用中所有Spring Beans的完整列表


