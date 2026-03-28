# Changelog

## [1.1.0.0] - 2026-03-28 — Task Categories & Dashboard

### Added
- Task categories (Work, Study, Life, Other) selectable when creating or editing todos
- Category filter bar on homepage for filtering todos by category
- Statistics dashboard page (`/dashboard`) with total count, completed count, completion rate, and category distribution bar chart
- Navigation bar for switching between homepage and dashboard
- Category badge displayed on each todo item
- Unit tests for TodoService (12 tests) and TodoController (9 tests)
- DashboardController with web layer tests (2 tests)

### Fixed
- Session tracking mode set to cookie-only, preventing 500 error on redirect in headless browsers
- Invalid category URL parameter no longer causes 500 error (graceful fallback to all todos)
- Edit page now preserves category selection when updating a todo
- Form validation errors now properly repopulate category dropdown

## [1.0.0.0] - 2026-03-28

Initial release. Basic CRUD Todo App with Spring Boot 3.2 + Thymeleaf + H2.
