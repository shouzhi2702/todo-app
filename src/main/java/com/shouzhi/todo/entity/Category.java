package com.shouzhi.todo.entity;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：待办事项分类枚举
 */
public enum Category {

    WORK("工作"),
    STUDY("学习"),
    LIFE("生活"),
    OTHER("其他");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
