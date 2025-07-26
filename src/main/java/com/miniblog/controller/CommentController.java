package com.miniblog.controller;

import com.miniblog.dto.CommentDTO;
import com.miniblog.enums.Role;
import com.miniblog.model.Comment;
import com.miniblog.model.Post;
import com.miniblog.model.User;
import com.miniblog.service.CommentService;
import com.miniblog.service.PostService;
import com.miniblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/post/{postId}")
    public String addComment(@PathVariable UUID postId,
                             @Valid @ModelAttribute("commentDTO") CommentDTO dto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Post post = postService.getById(postId).orElse(null);
        if (post == null) return "error/404";

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/" + postId + "?error=comment";
        }

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        Comment comment = modelMapper.map(dto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);
        commentService.save(comment);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable UUID id,
                                @AuthenticationPrincipal UserDetails userDetails) {

        Comment comment = commentService.getById(id).orElse(null);
        if (comment == null) return "error/404";

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        boolean isAuthor = comment.getUser().getId().equals(user.getId());
        boolean isAdmin = Role.ADMIN.equals(user.getRole()); // ✅ enum сравниваем правильно

        if (!isAuthor && !isAdmin) return "error/403";

        commentService.softDelete(comment);
        return "redirect:/posts/" + comment.getPost().getId();
    }

}
