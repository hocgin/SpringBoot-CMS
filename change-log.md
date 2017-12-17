# 2017-11-6
+ 设置权限接口
- JWT 和 Session 同时开启..
+ 增加登陆(/admin/login.html) 和 退出(/admin/logout)

## 问题1:
- 如果单独使用 JWT 如何解决直接请求页面的问题。
    1. 使用携带参数的方式。
    2. 将 Token 放入 Session 中处理。
  > 因此, 我暂时取消了去除 Session 的设定, 但是这样没法防止 csrf(头疼, 求解)...
  > 考虑使用 Spring Security 自带的防止 csrf 但 APP 怎么获取CSRF的值呢？

--------------
# 2017-11-8
- 完善很多页面问题, 配置以下解决方案。
- 优化单位的一些校验 (数据校验未完全完成, 前端+后端) __设想: 前端+后端都需判断数据的完整性(正则/是否必填)__
- 删除单位会影响到
    1. // 删除此单位 及 子类单位
    2. // 删除用户 和 此单位及子单位之间的关联(这些单位的用户所属单位字段设置为null)
    3. // 删除角色 和 此单位及子单位之间的关联(直接删除掉这些角色)
- 配置了 rememberMe 功能。

## 解决方案[#问题1]()
- /api/** 使用 JWT **(未配置)**
- Web 站点, 使用 Spring Security 来防止 csrf。
因此, 具体有以下方案
1. 单纯的<a>标签使用
```html
<!--1. 使用表单方式进行跳转-->
<form th:action="@{/admin/logout}" method="POST">
    <input type="submit" class="btn btn-default btn-flat" value="退出"/>
</form>
<!--2. 或使用 AJAX 进行跳转-->

```
2. 全局的 AJAX Header 设置
```html
<meta name="_csrf" th:content="${_csrf.token}" />
    <!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" th:content="${_csrf.headerName}" />
<!--...-->
    <script>
        $(function () {
            // 添加 CSRF 头
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            $.ajaxSetup({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                }
            });
        });
    </script>
```

# 2017-11-9
- 更新`Department`/`Permission`/`Variable`
- 优化权限
    - 删除权限时, 会移除角色 与 此权限 及 子权限的关联
- 去除 修改单位/修改权限 的冗余HTML代码
- 更新所有的校验情况, 进行逻辑整理和代码优化

# 2017-11-12
- 添加 /api/** 使用 JWT

# 2017-11-14
- UI 更新
- 完善用户信息
- 内置初始化数据库(`BuiltInSeeder`)
- 完善权限系统
    - 默认: admin/admin **所有后台权限**
    - 默认: adm1n/adm1n **只有后台浏览权限**
    
# 2017-11-17
- 系统日志
  - ILog 支持SpEL表达式
    > 暂缺查询..
- 菜单排序

# 2017-11-19
- 修正目录结构
- 标准化返回码
- 文件管理
- 公共API
    - 下载 `/public/downalod/{id|kepp-name}?rename=xx&src=ID`
    - 图片浏览 `/public/image/{id|kepp-name}?width=xx&height=xx&src=ID&force=true`

# 2017-11-22
- 城市管理

# 2017-11-23
- 天气服务
- 系统日志
    + 查询功能
    
# 2017-11-24
- 天气主页
- 颜色调节
   - 样式调节
fixed 修正城市文件导入 经纬度 设置不符合
fixed 修正序列化时数字开头字段丢失

# 2017-11-25
- 按位置搜索
- 优化背景
- 拦截打印 NoSQL 语句
- 搜索框优化

# 2017-11-26
- 会员管理

# 2017-11-27
- 邮件模版管理

# 2017-11-28
- 前台用户登陆
- Spring Security 优化
- 更改 Member Token 生成策略
- Swagger 配置

# 2017-11-29
- 配置用户剔除
- 认证异常信息补充
- 会员 Token 管理

# 2017-11-30
- 配置 Quartz 定时任务
- 任务配置
- 每月重置会员Token次数Job
- 其他微调

# 2017-12-1
- 百度 IP 定位 
- 邮件发送(群发 + 指定发送)

# 2017-12-2
- 邮件发送调整
- 邮件模块更新..

# 2017-12-3
- 更改邮件发送为异步操作
- 更换为主框架模版
- 日志精简(抽为查看详情)
- 修正API
- API 添加自动定位
- 短链管理`/u/{code}`使用
- 栏目管理 ..睡觉

# 2017-12-4
- 配置栏目管理权限
- 配置排序返回键
- 修改为统一 defaults.html
- 优化权限分配
- 文章管理
- - 栏目关联删除加入了级联删除


# 2017-12-9
- 通用评论框
- 评论管理

# 2017-12-16
- 重新规划 User
- 优化文章预览+栏目预览
- 控制面板

# 2017-12-17
- 天气请求地点分析
- SWAGGER_2 更新
- 图片读取 api 更新
- 重置密码
- 邮件验证表