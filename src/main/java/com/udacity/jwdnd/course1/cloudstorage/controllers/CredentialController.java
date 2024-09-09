package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credentials")
public class CredentialController {
    private final CredentialService credentialsService;
    private final UserMapper userMapper;

    @Autowired
    public CredentialController(CredentialService credentialsService, UserMapper userMapper) {
        this.credentialsService = credentialsService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String createCredential(Authentication authentication, Credential credentials){
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);
        Integer userId = user.getUserId();

        if (credentials.getCredentialid() != null) {
            credentialsService.editCredentials(credentials);
        } else {
            credentialsService.addCredentials(credentials, userId);
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteCredential(@RequestParam("id") int credentialid, Authentication authentication, RedirectAttributes redirectAttributes){
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);

        if(credentialid > 0){
            credentialsService.deleteCredentials(credentialid);
            return "redirect:/result?success";
        }
        redirectAttributes.addAttribute("error", "Can not delete the credential");
        return "redirect:/result?error";
    }
}
