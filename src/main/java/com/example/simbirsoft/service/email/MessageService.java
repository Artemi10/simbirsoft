package com.example.simbirsoft.service.email;

import org.springframework.stereotype.Service;

import javax.mail.Message;

@Service
public interface MessageService {
    void sendMessage(String email, String content);
}
