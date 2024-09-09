package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserMapper userMapper;

    public NoteController(NoteService noteService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public String handleAddUpdateNote(Authentication authentication, Note note){
        String username = (String) authentication.getPrincipal();
        User user = userMapper.getUserByName(username);
        Integer userId = user.getUserId();

        if (note.getNoteid() != null) {
            noteService.updateNote(note);
        } else {
            noteService.createNote(note, userId);
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int noteid, RedirectAttributes redirectAttributes){
        if(noteid > 0){
            noteService.deleteNote(noteid);
            return "redirect:/result?success";
        }


        redirectAttributes.addAttribute("error", "Can not delete the note");
        return "redirect:/result?error";
    }
}
