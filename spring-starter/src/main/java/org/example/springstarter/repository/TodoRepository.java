package org.example.springstarter.repository;

import org.example.springstarter.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoRepository extends JpaRepository<Todo,Long>{

}
