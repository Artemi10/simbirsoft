package com.example.simbirsoft.transfer.note;

import com.example.simbirsoft.entity.Note;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ResponseNoteDTOTest {
    private static SimpleDateFormat DATE_FORMAT;
    private Note note;

    @BeforeAll
    public static void init() {
        DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy HH:mm:s");
    }

    @BeforeEach
    public void initNote() throws ParseException {
        var date = DATE_FORMAT.parse("03 Февраля 2022 12:34:16");
        note = Note.builder()
                .id(1)
                .title("Лабораторная работа")
                .text("Сделать лаборатоную работу по схемотехнике")
                .creationTime(new Timestamp(date.getTime()))
                .build();
    }

    @Test
    public void create_ResponseNoteDTO_Test() {
        var noteDTO = new ResponseNoteDTO(note);
        assertEquals(1, noteDTO.id());
        assertEquals("Лабораторная работа", noteDTO.title());
        assertEquals("Сделать лаборатоную работу по схемотехнике", noteDTO.text());
        assertEquals("03 февраля 2022 12:34:16", noteDTO.timeStr());
    }
}
