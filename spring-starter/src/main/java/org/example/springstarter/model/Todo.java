package org.example.springstarter.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean completed = false;
}
