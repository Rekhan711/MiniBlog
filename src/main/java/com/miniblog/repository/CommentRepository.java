package com.miniblog.repository;

import com.miniblog.model.Comment;
import com.miniblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByPostAndIsDeletedFalseOrderByCreatedAtDesc(Post post);
}
