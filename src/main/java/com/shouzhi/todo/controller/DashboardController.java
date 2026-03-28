package com.shouzhi.todo.controller;

import com.shouzhi.todo.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：统计仪表盘控制器
 */
@Controller
public class DashboardController {

    private final TodoService todoService;

    public DashboardController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 统计仪表盘页面
     * @Author shouzhi @Date 2026-03-28
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = todoService.getStatistics();
        model.addAttribute("totalCount", stats.get("totalCount"));
        model.addAttribute("completedCount", stats.get("completedCount"));
        model.addAttribute("completionRate", stats.get("completionRate"));
        model.addAttribute("categoryStats", stats.get("categoryStats"));
        return "dashboard";
    }
}
