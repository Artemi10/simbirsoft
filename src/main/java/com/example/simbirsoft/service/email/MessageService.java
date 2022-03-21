package com.example.simbirsoft.service.email;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendMessage(String email, String content);
}
