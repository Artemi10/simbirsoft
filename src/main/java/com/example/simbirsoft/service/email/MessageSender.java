package com.example.simbirsoft.service.email;

import org.springframework.stereotype.Service;

@Service
public interface MessageSender {
    void sendMessage(String email, String content);
}
