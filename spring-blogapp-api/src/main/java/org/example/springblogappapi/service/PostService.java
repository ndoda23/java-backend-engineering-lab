package org.example.springblogappapi.service;

import org.example.springblogappapi.model.Post;
import org.example.springblogappapi.repository.PostRepository;
import org.example.springblogappapi.repository.UserRepository;
import org.example.springblogappapi.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;


    public PostService(UserService userService, PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public Post createPost(Long userId,Post post){
        User user = userService.getUserById(userId);

        post.setUser(user);
        post.setCreatedAt(java.time.LocalDateTime.now());

        return postRepository.save(post);
    }

    public List<Post> getPostsByUserId(Long userId){
        return postRepository.findByUserId(userId);
    }

    public Post updatePost(Long postId,Post updatedPost){
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("post cant be found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }

    public void deletePost(Long postId){
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("Post does not exist"));

        postRepository.delete(existingPost);
    }
}
