package com.shouzhi.todo.controller;

import com.shouzhi.todo.entity.Category;
import com.shouzhi.todo.entity.Todo;
import com.shouzhi.todo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：TodoController Web 层测试
 */
@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Nested
    @DisplayName("GET / 首页")
    class Index {

        @Test
        @DisplayName("无筛选参数时返回所有待办")
        void returnsAllTodos() throws Exception {
            Todo todo = createTodo(1L, "测试任务", Category.WORK);
            when(todoService.findAll()).thenReturn(List.of(todo));

            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attributeExists("todos", "newTodo", "categories"))
                    .andExpect(model().attribute("todos", hasSize(1)));
        }

        @Test
        @DisplayName("按分类筛选返回对应待办")
        void filtersByCategory() throws Exception {
            Todo workTodo = createTodo(1L, "工作任务", Category.WORK);
            when(todoService.findByCategory(Category.WORK)).thenReturn(List.of(workTodo));

            mockMvc.perform(get("/").param("category", "WORK"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("selectedCategory", "WORK"))
                    .andExpect(model().attribute("todos", hasSize(1)));
        }

        @Test
        @DisplayName("无效分类参数不报错，回退到全部")
        void invalidCategoryFallsBack() throws Exception {
            when(todoService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/").param("category", "INVALID"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("todos"));
        }
    }

    @Nested
    @DisplayName("POST /todos 创建")
    class Create {

        @Test
        @DisplayName("有效数据创建成功并重定向")
        void createsAndRedirects() throws Exception {
            when(todoService.create(any())).thenReturn(createTodo(1L, "新任务", Category.OTHER));

            mockMvc.perform(post("/todos")
                            .param("title", "新任务")
                            .param("category", "OTHER"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));

            verify(todoService).create(any());
        }

        @Test
        @DisplayName("标题为空时返回首页并显示错误")
        void emptyTitleShowsError() throws Exception {
            when(todoService.findAll()).thenReturn(List.of());

            mockMvc.perform(post("/todos").param("title", ""))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().hasErrors());
        }
    }

    @Nested
    @DisplayName("POST /todos/{id}/toggle 切换状态")
    class Toggle {

        @Test
        @DisplayName("切换成功后重定向")
        void togglesAndRedirects() throws Exception {
            Todo todo = createTodo(1L, "任务", Category.WORK);
            when(todoService.toggleComplete(1L)).thenReturn(todo);

            mockMvc.perform(post("/todos/1/toggle"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));

            verify(todoService).toggleComplete(1L);
        }
    }

    @Nested
    @DisplayName("POST /todos/{id}/delete 删除")
    class Delete {

        @Test
        @DisplayName("删除成功后重定向")
        void deletesAndRedirects() throws Exception {
            mockMvc.perform(post("/todos/1/delete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));

            verify(todoService).delete(1L);
        }
    }

    @Nested
    @DisplayName("GET /todos/{id}/edit 编辑页")
    class Edit {

        @Test
        @DisplayName("返回编辑页并回显数据")
        void returnsEditPage() throws Exception {
            Todo todo = createTodo(1L, "待编辑", Category.STUDY);
            when(todoService.findById(1L)).thenReturn(todo);

            mockMvc.perform(get("/todos/1/edit"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("edit"))
                    .andExpect(model().attribute("todo", hasProperty("title", is("待编辑"))));
        }
    }

    @Nested
    @DisplayName("POST /todos/{id}/update 更新")
    class UpdateTodo {

        @Test
        @DisplayName("有效数据更新成功并重定向")
        void updatesAndRedirects() throws Exception {
            when(todoService.update(eq(1L), any())).thenReturn(createTodo(1L, "新标题", Category.WORK));

            mockMvc.perform(post("/todos/1/update")
                            .param("title", "新标题")
                            .param("description", "新描述")
                            .param("category", "WORK"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));

            verify(todoService).update(eq(1L), any());
        }
    }

    // ===== 辅助方法 =====

    private Todo createTodo(Long id, String title, Category category) {
        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle(title);
        todo.setCategory(category);
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());
        return todo;
    }
}
