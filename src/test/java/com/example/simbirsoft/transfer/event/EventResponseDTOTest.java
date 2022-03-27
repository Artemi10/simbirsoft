package com.example.simbirsoft.transfer.event;

import com.example.simbirsoft.entity.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class EventResponseDTOTest {
    private static SimpleDateFormat DATE_FORMAT;
    private Event event;

    @BeforeAll
    public static void init() {
        DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy HH:mm:s");
    }

    @BeforeEach
    public void initApp() throws ParseException {
        var date = DATE_FORMAT.parse("03 Февраля 2022 12:34:16");
        event = Event.builder()
                .id(1)
                .name("LogIn Event")
                .extraInformation("User successfully logged in")
                .time(new Timestamp(date.getTime()))
                .build();
    }

    @Test
    public void create_ResponseAppDTO_Test() {
        var eventDTO = new EventResponseDTO(1, event);
        assertEquals(1, eventDTO.appId());
        assertEquals("LogIn Event", eventDTO.name());
        assertEquals("User successfully logged in", eventDTO.extraInformation());
        assertEquals("03 февраля 2022 12:34:16", eventDTO.time());
    }
}
