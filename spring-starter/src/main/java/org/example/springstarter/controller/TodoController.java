package org.example.springstarter.controller;

import org.example.springstarter.model.Todo;
import org.example.springstarter.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAll(){
        return todoService.getall();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id){
        return todoService.getById(id);
    }

    @PostMapping
    public Todo create(@RequestBody Todo todo){
        return todoService.create(todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        todoService.delete(id);
    }
}
