package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.entity.Note;

public record NoteDTO(String title, String text) {
    public NoteDTO(Note note) {
        this(note.getTitle(), note.getText());
    }
}
