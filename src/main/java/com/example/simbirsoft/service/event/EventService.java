package com.example.simbirsoft.service.event;

import com.example.simbirsoft.transfer.event.EventRequestDTO;
import com.example.simbirsoft.transfer.event.EventResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    void addEvent(EventRequestDTO requestDTO, String email);
    List<EventResponseDTO> findAppEvents(long appId, String email);
}
