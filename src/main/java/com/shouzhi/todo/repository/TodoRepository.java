package com.shouzhi.todo.repository;

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
}
