package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.File;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home/files")
public class FileController {
    private final FileService fileService;
    private final UserMapper userMapper;

    public FileController(FileService fileService, UserMapper userMapper) {
        this.fileService = fileService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String processFileUpload(
            Model model,
            @RequestParam("file") MultipartFile uploadedFile,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        String errorMessage = validateFileUpload(uploadedFile, authentication);

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/result?error";
        }

        saveUploadedFile(uploadedFile, authentication);
        return "redirect:/result?success";
    }

    private String validateFileUpload(MultipartFile file, Authentication authentication) {
        if (file.isEmpty()) {
            return "Upload failed: The selected file is empty. Please choose a valid file and try again.";
        }

        String username = (String) authentication.getPrincipal();
        User currentUser = userMapper.getUserByName(username);

        if (!fileService.isFileAvailable(file.getOriginalFilename(), currentUser.getUserId())) {
            return "Upload failed: A file with this name already exists in your account. Please rename the file or choose a different one.";
        }

        return null;
    }

    private void saveUploadedFile(MultipartFile file, Authentication authentication) throws IOException {
        String username = (String) authentication.getPrincipal();
        User currentUser = userMapper.getUserByName(username);
        fileService.createFile(file, currentUser.getUserId());
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileid, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (fileid > 0) {
            fileService.deleteFile(fileid);
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Can not delete the file");
        return "redirect:/result?error";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }
}
