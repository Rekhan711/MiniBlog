package com.miniblog.controller;

import com.miniblog.dto.PostDTO;
import com.miniblog.enums.Role;
import com.miniblog.model.Post;
import com.miniblog.model.User;
import com.miniblog.service.PostService;
import com.miniblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllActivePosts());
        return "post/list";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable UUID id, Model model) {
        return postService.getById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "post/view";
                })
                .orElse("error/404");
    }

    @GetMapping("/post/create")
    public String createPostForm(Model model) {
        model.addAttribute("postDTO", new PostDTO());
        return "create";
    }

    @PostMapping
    public String savePost(@Valid @ModelAttribute("postDTO") PostDTO dto,
                           BindingResult bindingResult,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "create";
        }

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Post post = modelMapper.map(dto, Post.class);
        post.setAuthor(user);

        postService.save(post);
        return "redirect:/posts/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        Post post = postService.getById(id).orElse(null);
        if (post == null) return "error/404";

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        if (!postService.isAuthor(post, user)) return "error/403";

        PostDTO dto = modelMapper.map(post, PostDTO.class);
        model.addAttribute("postDTO", dto);
        model.addAttribute("postId", post.getId());

        return "post/edit";
    }

    @PostMapping("/update")
    public String updatePost(@RequestParam UUID id,
                             @Valid @ModelAttribute("postDTO") PostDTO dto,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Post post = postService.getById(id).orElse(null);
        if (post == null) return "error/404";

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        if (!postService.isAuthor(post, user)) return "error/403";

        if (bindingResult.hasErrors()) {
            return "post/edit";
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam UUID id,
                             @AuthenticationPrincipal UserDetails userDetails) {

        Post post = postService.getById(id).orElse(null);
        if (post == null) return "error/404";

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        boolean isAuthor = postService.isAuthor(post, user);
        boolean isAdmin = Role.ADMIN.equals(user.getRole());

        if (!isAuthor && !isAdmin) return "error/403";

        postService.softDelete(post);
        return "redirect:/posts/";
    }

}
