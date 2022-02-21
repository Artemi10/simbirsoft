package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByUserId(long userId, Pageable pageable);
}
