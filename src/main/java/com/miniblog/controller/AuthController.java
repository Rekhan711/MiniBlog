package com.miniblog.controller;

import com.miniblog.dto.UserRegistrationDTO;
import com.miniblog.model.User;
import com.miniblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/login")
    public String loginPage(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "auth/login";
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDTO", new UserRegistrationDTO());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String registerSubmit(
            @Valid @ModelAttribute("userDTO") UserRegistrationDTO userDTO,
            BindingResult bindingResult,
            Model model

    ) {
        if (userService.isUsernameTaken(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.userDTO", "Имя уже занято");
        }
        if (userService.isEmailTaken(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.userDTO", "Email уже зарегистрирован");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        User user = modelMapper.map(userDTO, User.class);
        userService.register(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}
