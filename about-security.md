
## `/admin/**`
- CSRF 
- 需要认证

### 允许匿名访问
- `/admin/login.html` 登陆
- 静态资源
  - `/favicon.ico`
  - `/**/*.css`
  - `/**/*.js`
  - `/**/*.woff`
  - `/**/*.ttf`
  - `/**/*.jp`
  - `/**/*.woff2`

-----
## `/api/**`
- JWT
### 允许匿名访问
- `/api/auth/**` 获得授权

-----
## `/public/**`
- 公共入口