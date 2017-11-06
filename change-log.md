# 2017-11-6
+ 设置权限接口
- JWT 和 Session 同时开启..
+ 增加登陆(/admin/login.html) 和 退出(/admin/logout)

## 问题:
- 如果单独使用 JWT 如何解决直接请求页面的问题。
    1. 使用携带参数的方式。
    2. 将 Token 放入 Session 中处理。
  > 因此, 我暂时取消了去除 Session 的设定, 但是这样没法防止 csrf(头疼, 求解)...
  > 考虑使用 Spring Security 自带的防止 Csrf 但 APP 怎么获取CSRF的值呢？