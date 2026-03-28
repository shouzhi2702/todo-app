package com.shouzhi.todo.service;

import com.shouzhi.todo.entity.Category;
import com.shouzhi.todo.entity.Todo;
import com.shouzhi.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：TodoService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("返回所有待办事项（按创建时间倒序）")
        void returnsAllTodos() {
            Todo todo1 = createTodo(1L, "任务1", Category.WORK);
            Todo todo2 = createTodo(2L, "任务2", Category.STUDY);
            when(todoRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(todo2, todo1));

            List<Todo> result = todoService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getTitle()).isEqualTo("任务2");
            verify(todoRepository).findAllByOrderByCreatedAtDesc();
        }
    }

    @Nested
    @DisplayName("findByCategory")
    class FindByCategory {

        @Test
        @DisplayName("按分类筛选返回对应待办")
        void returnsTodosByCategory() {
            Todo workTodo = createTodo(1L, "写代码", Category.WORK);
            when(todoRepository.findByCategoryOrderByCreatedAtDesc(Category.WORK))
                    .thenReturn(List.of(workTodo));

            List<Todo> result = todoService.findByCategory(Category.WORK);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCategory()).isEqualTo(Category.WORK);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("ID 存在时返回待办")
        void returnsTodoWhenExists() {
            Todo todo = createTodo(1L, "测试", Category.OTHER);
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

            Todo result = todoService.findById(1L);

            assertThat(result.getTitle()).isEqualTo("测试");
        }

        @Test
        @DisplayName("ID 不存在时抛出异常")
        void throwsWhenNotFound() {
            when(todoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.findById(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("创建待办时 completed 强制为 false")
        void setsCompletedToFalse() {
            Todo todo = createTodo(null, "新任务", Category.LIFE);
            todo.setCompleted(true); // 故意设为 true
            when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Todo result = todoService.create(todo);

            assertThat(result.getCompleted()).isFalse();
            verify(todoRepository).save(todo);
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("更新标题、描述、分类")
        void updatesAllFields() {
            Todo existing = createTodo(1L, "旧标题", Category.OTHER);
            when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Todo updateData = new Todo();
            updateData.setTitle("新标题");
            updateData.setDescription("新描述");
            updateData.setCategory(Category.WORK);

            Todo result = todoService.update(1L, updateData);

            assertThat(result.getTitle()).isEqualTo("新标题");
            assertThat(result.getDescription()).isEqualTo("新描述");
            assertThat(result.getCategory()).isEqualTo(Category.WORK);
        }
    }

    @Nested
    @DisplayName("toggleComplete")
    class ToggleComplete {

        @Test
        @DisplayName("未完成 → 已完成")
        void togglesFromFalseToTrue() {
            Todo todo = createTodo(1L, "任务", Category.WORK);
            todo.setCompleted(false);
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
            when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Todo result = todoService.toggleComplete(1L);

            assertThat(result.getCompleted()).isTrue();
        }

        @Test
        @DisplayName("已完成 → 未完成")
        void togglesFromTrueToFalse() {
            Todo todo = createTodo(1L, "任务", Category.WORK);
            todo.setCompleted(true);
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
            when(todoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Todo result = todoService.toggleComplete(1L);

            assertThat(result.getCompleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("删除存在的待办")
        void deletesExistingTodo() {
            Todo todo = createTodo(1L, "待删除", Category.OTHER);
            when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

            todoService.delete(1L);

            verify(todoRepository).delete(todo);
        }

        @Test
        @DisplayName("删除不存在的待办时抛异常")
        void throwsWhenDeletingNonExistent() {
            when(todoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> todoService.delete(99L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("getStatistics")
    class GetStatistics {

        @Test
        @DisplayName("正确计算统计数据")
        void calculatesStats() {
            when(todoRepository.count()).thenReturn(10L);
            when(todoRepository.countByCompleted(true)).thenReturn(3L);
            when(todoRepository.countByCategory(Category.WORK)).thenReturn(4L);
            when(todoRepository.countByCategory(Category.STUDY)).thenReturn(3L);
            when(todoRepository.countByCategory(Category.LIFE)).thenReturn(2L);
            when(todoRepository.countByCategory(Category.OTHER)).thenReturn(1L);

            Map<String, Object> stats = todoService.getStatistics();

            assertThat(stats.get("totalCount")).isEqualTo(10L);
            assertThat(stats.get("completedCount")).isEqualTo(3L);
            assertThat(stats.get("completionRate")).isEqualTo(30.0);

            @SuppressWarnings("unchecked")
            Map<String, Long> categoryStats = (Map<String, Long>) stats.get("categoryStats");
            assertThat(categoryStats.get("工作")).isEqualTo(4L);
            assertThat(categoryStats.get("学习")).isEqualTo(3L);
        }

        @Test
        @DisplayName("零条数据时完成率为 0")
        void zeroTodosReturnsZeroRate() {
            when(todoRepository.count()).thenReturn(0L);
            when(todoRepository.countByCompleted(true)).thenReturn(0L);
            when(todoRepository.countByCategory(any())).thenReturn(0L);

            Map<String, Object> stats = todoService.getStatistics();

            assertThat(stats.get("completionRate")).isEqualTo(0.0);
        }
    }

    // ===== 辅助方法 =====

    private Todo createTodo(Long id, String title, Category category) {
        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle(title);
        todo.setCategory(category);
        todo.setCompleted(false);
        return todo;
    }
}
