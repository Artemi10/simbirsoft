package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    @Query("""
              SELECT app
              FROM App app
              WHERE app.user.email = :email
              ORDER BY app.id DESC""")
    Page<App> findAllByUserEmail(String email, Pageable pageable);

    @Query("""
              SELECT count(app)
              FROM App app
              WHERE app.user.email = :email""")
    int getUserAppsAmount(String email);

    @Query("""
              SELECT app
              FROM App app
              WHERE app.user.email = :email
              AND app.id = :noteId""")
    Optional<App> findUserAppById(long appId, String email);

    @Modifying
    @Transactional
    void deleteByIdAndUserEmail(long appId, String email);
}
