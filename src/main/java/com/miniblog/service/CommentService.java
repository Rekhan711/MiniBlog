package com.miniblog.service;

import com.miniblog.model.Comment;
import com.miniblog.model.Post;
import com.miniblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getCommentsForPost(Post post) {
        return commentRepository.findByPostAndIsDeletedFalseOrderByCreatedAtDesc(post);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public void softDelete(Comment comment) {
        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }

    public Optional<Comment> getById(UUID id) {
        return commentRepository.findById(id);
    }

}
