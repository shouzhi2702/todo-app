package com.shouzhi.todo.controller;

import com.shouzhi.todo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：DashboardController Web 层测试
 */
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    @DisplayName("仪表盘页面正常渲染")
    void dashboardReturnsStats() throws Exception {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalCount", 10L);
        stats.put("completedCount", 3L);
        stats.put("completionRate", 30.0);
        Map<String, Long> categoryStats = new LinkedHashMap<>();
        categoryStats.put("工作", 4L);
        categoryStats.put("学习", 3L);
        categoryStats.put("生活", 2L);
        categoryStats.put("其他", 1L);
        stats.put("categoryStats", categoryStats);

        when(todoService.getStatistics()).thenReturn(stats);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("totalCount", 10L))
                .andExpect(model().attribute("completedCount", 3L))
                .andExpect(model().attribute("completionRate", 30.0));
    }

    @Test
    @DisplayName("零数据时仪表盘不报错")
    void dashboardWithZeroData() throws Exception {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalCount", 0L);
        stats.put("completedCount", 0L);
        stats.put("completionRate", 0.0);
        stats.put("categoryStats", new LinkedHashMap<String, Long>());

        when(todoService.getStatistics()).thenReturn(stats);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("totalCount", 0L));
    }
}
