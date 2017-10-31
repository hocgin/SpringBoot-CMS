## 关于 html 命名约定
1. `*-view.html` 即`Controller`中对应一个 
```
public final String BASE_TEMPLATES_PATH = "path/%s";

@GetMapping('*-view.html')
public String v*(){
    return String.format(BASE_TEMPLATES_PATH, "*-view")
}
```
2. `*-modal.html` 为一个弹窗。
3. `index.html` 为主界面
4. `content.html` 为`#pjax-container`的内容

## 关于服务器返回数据 `in.hocg.web.lang.body.response.Results`
- `200` 表示成功
- `500` 表示服务器错误
- `1000` 数据校验失败
- `1100` 数据库操作失败

## JS 相关 class 或 id 使用 驼峰式命名# Spring-Boot-ing
# JWTSpringSecurity
