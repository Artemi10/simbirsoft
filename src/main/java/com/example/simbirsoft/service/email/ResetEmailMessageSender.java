package com.example.simbirsoft.service.email;

import com.example.simbirsoft.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;

@Service
@RequiredArgsConstructor
public class ResetEmailMessageSender implements MessageSender {
    @Value("${server.domain.url}")
    private String domainURL;
    private final Message message;

    @Override
    public void sendMessage(String email, String content) {
        try{
            setContent(email, content);
            Transport.send(message);
        } catch (MessagingException exception) {
            throw new EmailException("Не удалость отправить email");
        }
    }

    protected void setContent(String email, String content) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Востановление пароля");
        var link = String.format("%s/user/update?email=%s&token=%s", domainURL, email, content);
        var text = String.format("Востановить пароль можно по ссылке: %s", link);
        message.setText(text);
    }
}
