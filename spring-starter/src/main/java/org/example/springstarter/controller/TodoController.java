package org.example.springstarter.controller;

import jakarta.validation.Valid;
import org.example.springstarter.model.Todo;
import org.example.springstarter.service.TodoService;
import org.springframework.http.ResponseEntity;
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
    public Todo create(@Valid @RequestBody Todo todo){
        return todoService.create(todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        todoService.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> createTodo(@PathVariable Long id,@Valid @RequestBody Todo todoDetails){
        Todo updatedTodo = todoService.updateTodo(id,todoDetails);
        return  ResponseEntity.ok(updatedTodo);
    }

}
