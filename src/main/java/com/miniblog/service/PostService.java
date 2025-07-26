package com.miniblog.service;

import com.miniblog.model.Post;
import com.miniblog.model.User;
import com.miniblog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllActivePosts() {
        return postRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
    }

    public Optional<Post> getById(UUID id) {
        return postRepository.findByIdAndIsDeletedFalse(id);
    }

    public List<Post> getUserPosts(User user) {
        return postRepository.findByAuthorAndIsDeletedFalseOrderByCreatedAtDesc(user);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public void softDelete(Post post) {
        post.setIsDeleted(true);
        postRepository.save(post);
    }

    public boolean isAuthor(Post post, User user) {
        return post.getAuthor().getId().equals(user.getId());
    }
}
