package com.shouzhi.todo.repository;

import com.shouzhi.todo.entity.Category;
import com.shouzhi.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：待办事项数据访问层
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 按完成状态查询，按创建时间倒序
     * @Author shouzhi @Date 2026-03-28
     */
    List<Todo> findByCompletedOrderByCreatedAtDesc(Boolean completed);

    /**
     * 查询所有待办，按创建时间倒序
     * @Author shouzhi @Date 2026-03-28
     */
    List<Todo> findAllByOrderByCreatedAtDesc();

    /**
     * 按分类查询，按创建时间倒序
     * @Author shouzhi @Date 2026-03-28
     */
    List<Todo> findByCategoryOrderByCreatedAtDesc(Category category);

    /**
     * 按分类统计数量
     * @Author shouzhi @Date 2026-03-28
     */
    long countByCategory(Category category);

    /**
     * 统计已完成数量
     * @Author shouzhi @Date 2026-03-28
     */
    long countByCompleted(Boolean completed);
}
