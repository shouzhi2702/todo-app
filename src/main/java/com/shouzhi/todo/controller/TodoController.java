package com.shouzhi.todo.controller;

import com.shouzhi.todo.entity.Category;
import com.shouzhi.todo.entity.Todo;
import com.shouzhi.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 创建者：shouzhi | 创建时间: 2026-03-28 | 项目: todo-app
 * 描述：待办事项控制器
 */
@Controller
@RequestMapping("/")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 首页 - 展示所有待办事项
     * @Author shouzhi @Date 2026-03-28
     */
    @GetMapping
    public String index(@RequestParam(required = false) String category, Model model) {
        if (category != null && !category.isEmpty()) {
            try {
                Category cat = Category.valueOf(category);
                model.addAttribute("todos", todoService.findByCategory(cat));
                model.addAttribute("selectedCategory", category);
            } catch (IllegalArgumentException e) {
                model.addAttribute("todos", todoService.findAll());
            }
        } else {
            model.addAttribute("todos", todoService.findAll());
        }
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("categories", Category.values());
        return "index";
    }

    /**
     * 创建待办事项
     * @Author shouzhi @Date 2026-03-28
     */
    @PostMapping("/todos")
    public String create(@Valid @ModelAttribute("newTodo") Todo todo,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("todos", todoService.findAll());
            model.addAttribute("categories", Category.values());
            return "index";
        }
        todoService.create(todo);
        redirectAttributes.addFlashAttribute("message", "创建成功");
        return "redirect:/";
    }

    /**
     * 切换完成状态
     * @Author shouzhi @Date 2026-03-28
     */
    @PostMapping("/todos/{id}/toggle")
    public String toggle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.toggleComplete(id);
        redirectAttributes.addFlashAttribute("message", "状态已更新");
        return "redirect:/";
    }

    /**
     * 删除待办事项
     * @Author shouzhi @Date 2026-03-28
     */
    @PostMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.delete(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/";
    }

    /**
     * 编辑页面
     * @Author shouzhi @Date 2026-03-28
     */
    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.findById(id));
        return "edit";
    }

    /**
     * 更新待办事项
     * @Author shouzhi @Date 2026-03-28
     */
    @PostMapping("/todos/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("todo") Todo todo,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit";
        }
        todoService.update(id, todo);
        redirectAttributes.addFlashAttribute("message", "更新成功");
        return "redirect:/";
    }
}
