package org.example.springblogappapi.repository;

import org.example.springblogappapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    public List<Post> findByUserId(Long userId);
}
