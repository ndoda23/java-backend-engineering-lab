package org.example.springstarter.service;

import org.example.springstarter.model.Todo;
import org.example.springstarter.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public List<Todo> getall(){
        return todoRepository.findAll();
    }

    public Todo getById(Long id){
        return todoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Todo not found : "+id));
    }

    public Todo create(Todo todo){
        return todoRepository.save(todo);
    }

    public void delete(Long id){
        todoRepository.deleteById(id);
    }

}
