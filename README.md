## 更新日志
[更新日志](/change-log.md)

## 细节
### 后端
- Spring Security(权限控制)
- quartz(任务调度)
- swagger(API 文档)
- mongodb(数据库)
- thymeleaf(模版引擎)
- WebSocket(站内消息)
- ..

### 前端
- AdminLTE-2.4.2
- LayIM(聊天UI, 未提交)
- Layer(弹窗组件)
- EChart(图表)
- PJAX
- datatables(数据表格)
- webuploader(上传组件)
- ..

## 模块
- 消息系统
- 评论系统
- 文件系统
- 用户系统
- 内容管理
- 日志系统


## 初始化运行

1.编辑`application-dev.yml`做如下修改:
```yaml
initDatabase: true
```
2.启动后, 修改为false