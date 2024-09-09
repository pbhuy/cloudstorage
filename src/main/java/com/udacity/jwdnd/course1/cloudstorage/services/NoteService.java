package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entities.Note;
import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    @Autowired
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(int userid){
        return noteMapper.getNotes(userid);
    }

    public void createNote(Note note, int userId){
        Note newNote = new Note();
        newNote.setUserid(userId);
        newNote.setNotedescription(note.getNotedescription());
        newNote.setNotetitle(note.getNotetitle());

        noteMapper.insertNote(newNote);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(int noteid){
        noteMapper.deleteNote(noteid);
    }
}
