package com.shouzhi.todo.service;

import com.shouzhi.todo.entity.Category;
import com.shouzhi.todo.entity.Todo;
import com.shouzhi.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：待办事项业务逻辑层
 */
@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * 查询所有待办事项
     * @Author shouzhi @Date 2026-03-28
     * @return 待办事项列表（按创建时间倒序）
     */
    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 按分类查询待办事项
     * @Author shouzhi @Date 2026-03-28
     * @param category 分类
     * @return 该分类下的待办事项列表
     */
    public List<Todo> findByCategory(Category category) {
        return todoRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    /**
     * 获取统计数据
     * @Author shouzhi @Date 2026-03-28
     * @return 统计信息 Map（totalCount, completedCount, completionRate, categoryStats）
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        long total = todoRepository.count();
        long completed = todoRepository.countByCompleted(true);
        double rate = total > 0 ? (double) completed / total * 100 : 0;

        stats.put("totalCount", total);
        stats.put("completedCount", completed);
        stats.put("completionRate", Math.round(rate * 10) / 10.0);

        Map<String, Long> categoryStats = new LinkedHashMap<>();
        for (Category cat : Category.values()) {
            categoryStats.put(cat.getLabel(), todoRepository.countByCategory(cat));
        }
        stats.put("categoryStats", categoryStats);

        return stats;
    }

    /**
     * 根据ID查询待办事项
     * @Author shouzhi @Date 2026-03-28
     * @param id 待办事项ID
     * @return 待办事项
     */
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("待办事项不存在: " + id));
    }

    /**
     * 创建待办事项
     * @Author shouzhi @Date 2026-03-28
     * @param todo 待办事项
     * @return 创建后的待办事项
     */
    @Transactional
    public Todo create(Todo todo) {
        todo.setCompleted(false);
        return todoRepository.save(todo);
    }

    /**
     * 更新待办事项
     * @Author shouzhi @Date 2026-03-28
     * @param id 待办事项ID
     * @param todo 更新内容
     * @return 更新后的待办事项
     */
    @Transactional
    public Todo update(Long id, Todo todo) {
        Todo existing = findById(id);
        existing.setTitle(todo.getTitle());
        existing.setDescription(todo.getDescription());
        existing.setCategory(todo.getCategory());
        return todoRepository.save(existing);
    }

    /**
     * 切换完成状态
     * @Author shouzhi @Date 2026-03-28
     * @param id 待办事项ID
     * @return 更新后的待办事项
     */
    @Transactional
    public Todo toggleComplete(Long id) {
        Todo todo = findById(id);
        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }

    /**
     * 删除待办事项
     * @Author shouzhi @Date 2026-03-28
     * @param id 待办事项ID
     */
    @Transactional
    public void delete(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }
}
