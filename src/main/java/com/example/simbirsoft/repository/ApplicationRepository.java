package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
