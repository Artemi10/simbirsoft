package com.example.simbirsoft.service.event;

import com.example.simbirsoft.entity.App;
import com.example.simbirsoft.entity.Event;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.repository.EventRepository;
import com.example.simbirsoft.service.application.AppService;
import com.example.simbirsoft.transfer.event.EventRequestDTO;
import com.example.simbirsoft.transfer.event.EventResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final AppService appService;
    private final EventRepository eventRepository;

    @Override
    public void addEvent(EventRequestDTO requestDTO, String email) {
        requestDTO.check();
        if (appService.isUserApp(requestDTO.appId(), email)){
            var time = new Timestamp(new Date().getTime());
            var app = App.builder()
                    .id(requestDTO.appId())
                    .build();
            var event = Event.builder()
                    .name(requestDTO.name())
                    .extraInformation(requestDTO.extraInformation())
                    .time(time)
                    .app(app)
                    .build();
            eventRepository.save(event);
        }
        else throw new EntityException("Приложение не найдено");
    }

    @Override
    public List<EventResponseDTO> findAppEvents(long appId, String email) {
        return eventRepository.findEventsByApp(appId, email)
                .stream()
                .map(event -> new EventResponseDTO(appId, event))
                .toList();
    }
}
