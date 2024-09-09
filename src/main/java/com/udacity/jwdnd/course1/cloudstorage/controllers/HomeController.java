package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserMapper userMapper, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);

        model.addAttribute("files", fileService.getUploadedFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getNotes(user.getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }


    @GetMapping("/result")
    public String showResultPage(){
        return "result";
    }
}
