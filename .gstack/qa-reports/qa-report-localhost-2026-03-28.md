# QA Report: Todo App

**Date:** 2026-03-28
**URL:** http://localhost:8080
**Mode:** Full
**Framework:** Spring Boot 3.2 + Thymeleaf
**Pages Visited:** 3 (首页、编辑页、重定向后首页)
**Duration:** ~5 min

---

## Summary

| Metric | Value |
|--------|-------|
| Issues Found | 1 |
| Fixes Applied | 1 (verified) |
| Health Score | 65 → 92 |

## Issues

### ISSUE-001: 创建待办后重定向报 500 错误 [CRITICAL] [FIXED]

**Category:** Functional
**Severity:** Critical
**Fix Status:** verified
**Commit:** ddad008

**Description:**
创建待办事项后，POST /todos 重定向到首页时报 500 Internal Server Error。
根因：headless 浏览器不支持 cookie，Spring 回退到 URL rewriting 在路径中
附加 jsessionid（如 `/;jsessionid=xxx`），导致 GET 请求匹配路径异常，
Thymeleaf 渲染 index.html 时找不到 `newTodo` model 对象。

**Repro:**
1. 打开首页 http://localhost:8080
2. 填写标题和描述
3. 点击"添加"
4. 页面跳转到 Whitelabel Error Page，显示 500 错误

**Root Cause:**
`application.yml` 未配置 session tracking mode，默认包含 URL rewriting。

**Fix:**
```yaml
server:
  servlet:
    session:
      tracking-modes: cookie
```

**Before:** screenshots/initial.png → screenshots/after-add-500.png (500 error)
**After:** screenshots/issue-001-after.png (创建成功，正常显示)

---

## Functional Test Results

| 功能 | 结果 | 备注 |
|------|------|------|
| 首页加载 | ✅ PASS | 页面正常渲染 |
| 创建待办 | ✅ PASS | 修复后正常 |
| 完成切换 | ✅ PASS | 状态切换+按钮文字更新正确 |
| 编辑待办 | ✅ PASS | 数据回显+保存正常 |
| 删除待办 | ⚠️ SKIP | confirm 对话框阻塞 headless 浏览器 |
| 空列表展示 | ✅ PASS | 显示"暂无待办事项" |
| 成功提示 | ✅ PASS | flash message 正常显示 |

## Health Score

| Category | Score | Weight |
|----------|-------|--------|
| Console | 100 | 15% |
| Links | 100 | 10% |
| Visual | 95 | 10% |
| Functional | 85 | 20% |
| UX | 90 | 15% |
| Performance | 95 | 10% |
| Content | 100 | 5% |
| Accessibility | 80 | 15% |
| **Final** | **92** | |

## Top 3 Things to Fix

1. ~~ISSUE-001: 创建待办后 500 错误~~ **FIXED**
2. 删除按钮的 confirm 对话框建议改为页面内 modal，而非浏览器原生 confirm
3. 表单缺少 CSRF token（目前不影响功能，但生产环境需要 Spring Security）

---

QA found 1 issue, fixed 1, health score 65 → 92.
