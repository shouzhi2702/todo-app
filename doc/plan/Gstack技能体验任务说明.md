# Gstack 技能体验任务说明

> 作者：shouzhi | 创建时间：2026-03-28

## 一、基础项目

**项目名称**：待办事项管理系统（Todo App）
**技术栈**：Spring Boot 3.2 + Thymeleaf + H2 + JPA
**访问地址**：http://localhost:8080
**已有功能**：Todo 增删改查 + 完成状态切换

## 二、体验任务：新增「任务分类 + 统计仪表盘」功能

### 需求描述

在现有 Todo App 基础上，新增以下功能：

1. **任务分类**
   - 新增 Category 实体（工作、学习、生活、其他）
   - Todo 关联分类，创建时选择分类
   - 按分类筛选待办事项

2. **统计仪表盘**
   - 新增 `/dashboard` 页面
   - 展示：总任务数、已完成数、完成率
   - 按分类展示任务分布
   - 最近 7 天的完成趋势

### 涉及改动

| 层 | 改动内容 |
|---|---|
| Entity | 新增 Category 实体，Todo 增加分类关联 |
| Repository | 新增 CategoryRepository，TodoRepository 增加按分类查询 |
| Service | 新增 CategoryService，TodoService 增加统计方法 |
| Controller | 新增 DashboardController，TodoController 增加分类参数 |
| Frontend | index.html 增加分类选择和筛选，新增 dashboard.html |

## 三、Gstack 技能体验流程

### Step 1：方案评审 — `/gstack-autoplan`

```
告诉 Claude：
"我要在 Todo App 上新增任务分类和统计仪表盘功能，请帮我评审一下方案"
然后执行 /gstack-autoplan
```

**体验点**：观察 CEO/设计/工程三个视角分别关注什么

### Step 2：安全护栏 — `/gstack-guard`

```
/gstack-guard src/
```

**体验点**：观察 Agent 编码时，危险操作是否被拦截

### Step 3：正常编码

```
告诉 Claude：
"请按照评审后的方案，实现任务分类和统计仪表盘功能"
```

### Step 4：代码审查 — `/gstack-review`

```
/gstack-review
```

**体验点**：观察审查报告，关注 SQL 安全、逻辑缺陷等

### Step 5：QA 测试 — `/gstack-qa`

```
先启动应用：mvn spring-boot:run
然后执行 /gstack-qa
```

**体验点**：观察无头浏览器如何自动测试页面，发现问题后是否自动修复

### Step 6：设计审查 — `/gstack-design-review`

```
/gstack-design-review
```

**体验点**：观察设计师视角如何评价 UI

### Step 7：安全审计 — `/gstack-cso`

```
/gstack-cso
```

**体验点**：观察安全扫描报告

### Step 8：发布 — `/gstack-ship`

```
/gstack-ship
```

**体验点**：观察自动化发布流程

## 四、注意事项

- 启动项目前确保 8080 端口未被占用
- H2 是内存数据库，重启后数据会丢失
- 每个 gstack 技能可以单独体验，不必严格按顺序
