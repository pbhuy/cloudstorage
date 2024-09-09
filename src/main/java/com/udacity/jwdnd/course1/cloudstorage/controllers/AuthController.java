package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
        String error = null;

        if (userService.isExistedUser(user.getUsername())) {
            error = "Username already exists.";
        }

        if (error == null) {
            int userId = userService.createUser(user);
            if (userId < 0) {
                error = "Signup error. Please try again.";
            }
        }

        if (error == null) {
            model.addAttribute("success", "Signup successful.");
            return "redirect:/login";
        } else {
            model.addAttribute("error", error);
        }

        return "signup";
    }
}
