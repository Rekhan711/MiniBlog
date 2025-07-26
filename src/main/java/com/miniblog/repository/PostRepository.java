package com.miniblog.repository;

import com.miniblog.model.Post;
import com.miniblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByIsDeletedFalseOrderByCreatedAtDesc();

    List<Post> findByAuthorAndIsDeletedFalseOrderByCreatedAtDesc(User author);

    Optional<Post> findByIdAndIsDeletedFalse(UUID id);
}
