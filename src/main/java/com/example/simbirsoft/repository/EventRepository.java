package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
