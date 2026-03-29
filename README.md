# Todo App — 待办事项管理

一个简单的待办事项管理系统，支持任务分类和统计仪表盘。

## 功能

- 待办事项的增删改查
- 任务分类（工作、学习、生活、其他）
- 按分类筛选待办事项
- 统计仪表盘（总任务数、已完成数、完成率、分类分布）
- 完成/撤回状态切换

## 技术栈

| 组件 | 技术 |
|------|------|
| 后端 | Spring Boot 3.2 |
| 前端 | Thymeleaf（服务端渲染） |
| 数据库 | H2（内存数据库） |
| ORM | Spring Data JPA |
| 构建 | Maven |
| CI | GitHub Actions |
| 部署 | Railway |

## 快速开始

```bash
# 克隆仓库
git clone https://github.com/shouzhi2702/todo-app.git
cd todo-app

# 运行（需要 JDK 17+）
mvn spring-boot:run

# 访问
open http://localhost:8080
```

## 项目结构

```
src/main/java/com/shouzhi/todo/
├── TodoApplication.java              # 启动类
├── entity/
│   ├── Todo.java                     # 待办实体
│   └── Category.java                 # 分类枚举（工作/学习/生活/其他）
├── repository/
│   └── TodoRepository.java           # 数据访问层
├── service/
│   └── TodoService.java              # 业务逻辑（CRUD + 统计）
└── controller/
    ├── TodoController.java           # 首页控制器（列表/筛选/增删改）
    └── DashboardController.java      # 仪表盘控制器（统计数据）
```

## 页面

| 路径 | 功能 |
|------|------|
| `/` | 首页 — 待办列表 + 新增表单 + 分类筛选 |
| `/?category=WORK` | 按分类筛选 |
| `/dashboard` | 统计仪表盘 |
| `/todos/{id}/edit` | 编辑待办 |
| `/h2-console` | H2 数据库控制台 |

## 测试

```bash
# 运行全部 24 个测试
mvn test

# 运行单个测试类
mvn test -Dtest=TodoServiceTest
mvn test -Dtest=TodoControllerTest
mvn test -Dtest=DashboardControllerTest
```

测试覆盖：
- TodoServiceTest（12 个测试）— CRUD、分类筛选、统计计算
- TodoControllerTest（9 个测试）— 端点、校验、无效参数处理
- DashboardControllerTest（2 个测试）— 仪表盘渲染、零数据

## 部署

项目通过 Railway 自动部署。推送到 main 分支后 Railway 自动构建和上线。

生产地址：https://todo-app-production-1c2f.up.railway.app

## 文档

- [CHANGELOG.md](CHANGELOG.md) — 版本变更记录
- [方案文档](doc/plan/任务分类与统计仪表盘方案.md) — 功能设计方案
- [Gstack 实操记录](doc/plan/Gstack完整开发闭环实操记录.md) — 开发流程记录
