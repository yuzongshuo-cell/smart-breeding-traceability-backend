# 智慧养殖溯源管理系统（Backend）

基于 **Spring Boot + MyBatis + PageHelper + JWT + Redis（黑名单登出）+ Knife4j（Swagger）** 的后台接口项目，实现登录鉴权、统一返回、全局异常、分页查询、多表联查等能力，服务于“栏舍/栏圈/动物/检疫病症/数据大屏”等业务模块。

---

## 技术栈

- Java 8
- Spring Boot 2.7.x
- MyBatis + MyBatis XML
- MySQL 8.x（建议）
- PageHelper（分页）
- JWT（无状态鉴权）
- Redis（JWT 黑名单：退出登录立即失效）
- Knife4j / Swagger（接口文档）
- Druid / HikariCP（数据源，视依赖而定）

---

## 功能亮点

- **JWT 登录鉴权**：拦截器统一校验 token，有效期控制
- **Redis 黑名单登出**：`/logout` 写入黑名单，**登出后 token 立刻失效**
- **统一响应结构**：Result + ResultCode
- **全局异常处理**：业务异常与系统异常统一返回
- **分页查询**：PageHelper 实现列表分页
- **多表联查**：ExtMapper + resultMap/association/collection 映射扩展字段
- **接口文档**：Knife4j 在线调试（支持 token 全局参数）

---

## 项目结构（示例）

src/main/java/com/briup/product_source
├─ controller # 控制层：对外接口
├─ service # 业务接口
├─ service/impl # 业务实现
├─ dao # Mapper 接口
├─ pojo # 实体类
├─ config # 配置类（WebConfig、Knife4jConfig 等）
├─ Interceptor # JWT 拦截器
├─ exception # 业务异常
└─ util # 工具类（JwtUtil 等）
src/main/resources
├─ mappers # MyBatis XML
└─ application.yml # 本地配置


## 环境准备

- JDK 8
- Maven 3.6+
- MySQL 8.x
- Redis 6.x+（用于黑名单登出）

---

## 快速启动

### 1. 导入数据库
1) 创建数据库（默认名：`agri_trace`）
2) 执行 `sql/` 目录下的建表与初始化脚本

> 建议提供一个默认账号：`admin / admin`

### 2. 配置文件
复制配置模板并修改为你本地环境：

```bash
cp src/main/resources/application.yml.example src/main/resources/application.yml
修改 MySQL、Redis、JWT 密钥等配置。

3. 启动项目
mvn spring-boot:run
启动成功后默认端口：9999

接口文档（Knife4j）
访问：
http://localhost:9999/doc.html

调试说明：

先调用 /login 获取 token

在 Knife4j 的全局参数/请求头里设置 token: <你的token>

调试其他需要鉴权的接口

登录鉴权说明
登录接口：POST /login

请求：application/x-www-form-urlencoded

username=admin

password=admin

后续请求在 Header 中携带：

token: <jwt_token>

登出接口：

GET /logout

Header 携带 token 后写入 Redis 黑名单，之后该 token 立即失效

注意事项（安全）
不要在接口返回中暴露密码字段

建议对外返回 VO/DTO，或对 password 使用 @JsonIgnore

不要提交敏感配置

application.yml 不入库，只提交 application.yml.example

数据库密码、Redis 密码、JWT secret 采用环境变量或本地配置

TODO（可选优化）
密码加密（BCrypt）+ 登录失败次数限制

角色权限（RBAC）/ 管理员踢人下线

操作日志（AOP）

单元测试 / Postman Collection 导出

License
MIT

yaml
Copy code

---

## 2）.gitignore（直接复制到项目根目录）

```gitignore
# ====== Java / Maven ======
target/
*.class
*.log
*.ctxt

# ====== IntelliJ IDEA ======
.idea/
*.iml
*.iws
*.ipr
out/

# ====== OS ======
.DS_Store
Thumbs.db

# ====== Spring Boot ======
logs/
log/
*.pid
*.pid.lock

# ====== Local config (DO NOT COMMIT) ======
src/main/resources/application.yml
src/main/resources/application-dev.yml
src/main/resources/application-prod.yml

# ====== Uploads / temp ======
/profile/
/upload/
/tmp/

# ====== Others ======
*.swp
*.bak
