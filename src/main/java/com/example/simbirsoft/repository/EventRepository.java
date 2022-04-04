package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("""
              SELECT event
              FROM Event event
              WHERE event.app.id = :appId
              AND event.app.user.email = :email""")
    List<Event> findEventsByApp(long appId, String email);

}
