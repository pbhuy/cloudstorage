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
    public String handleFileUpload(Model model, @RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {
        String uploadError = null;

        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);

        if (fileUpload.isEmpty()) {
            uploadError = "Please select a non-empty file";
        }

        if (!fileService.isFileAvailable(fileUpload.getOriginalFilename(), user.getUserId())) {
            uploadError = "File already exists";

        }

        if(uploadError!=null) {
            model.addAttribute("error", uploadError);
            redirectAttributes.addFlashAttribute("error", uploadError);
            return "redirect:/result?error";
        }

        fileService.createFile(fileUpload, user.getUserId());
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileid, Authentication authentication, RedirectAttributes redirectAttributes){
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);
        String deleteError = null;

        if(fileid > 0){
            fileService.deleteFile(fileid);
            return "redirect:/result?success";
        }


        redirectAttributes.addAttribute("error", "Can not delete the file");
        return "redirect:/result?error";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ file.getFilename()+"\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }
}
