package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("""
              SELECT note FROM Note note
              WHERE note.user.email = :email
              ORDER BY note.id DESC
              """)
    Page<Note> findAllByUserEmail(String email, Pageable pageable);

    @Query("""
              SELECT count(note) FROM Note note
              WHERE note.user.email = :email""")
    int getUserNotesAmount(String email);

    @Query("""
              SELECT note FROM Note note
              WHERE note.user.email = :email
              AND note.id = :noteId""")
    Optional<Note> findUserNoteById(long noteId, String email);

    @Modifying
    @Query("""
              DELETE FROM Note note
              WHERE note.user.email = :email
              AND note.id = :noteId""")
    void deleteUserNoteById(long noteId, String email);
}
