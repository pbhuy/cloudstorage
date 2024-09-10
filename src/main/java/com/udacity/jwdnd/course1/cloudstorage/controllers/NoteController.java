package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.entities.User;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public String processNoteAddOrUpdate(Authentication authentication, @ModelAttribute Note note) {
        User currentUser = getCurrentUser(authentication);

        if (isExistingNote(note)) {
            updateExistingNote(note);
        } else {
            createNewNote(note, currentUser.getUserId());
        }

        return "redirect:/result?success";
    }

    private User getCurrentUser(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        return userMapper.getUserByName(username);
    }

    private boolean isExistingNote(Note note) {
        return note.getNoteid() != null;
    }

    private void updateExistingNote(Note note) {
        noteService.updateNote(note);
    }

    private void createNewNote(Note note, Integer userId) {
        noteService.createNote(note, userId);
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
