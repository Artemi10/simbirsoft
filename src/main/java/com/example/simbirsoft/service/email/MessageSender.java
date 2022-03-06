package com.example.simbirsoft.service.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface MessageSender {
    @Async
    void sendMessage(String email, String content);
}
