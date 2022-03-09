package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.entity.Note;

import java.text.SimpleDateFormat;

public record ResponseNoteDTO(long id, String title, String text, String timeStr) {
    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("dd MMMM yyyy HH:mm:s");

    public ResponseNoteDTO(Note note) {
        this(note.getId(), note.getTitle(),
                note.getText(), DATE_FORMAT.format(note.getCreationTime()));
    }
}
