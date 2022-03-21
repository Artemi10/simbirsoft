package com.example.simbirsoft.service.email;

import com.example.simbirsoft.exception.EmailException;
import com.example.simbirsoft.service.email.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;

@Service
public class ResetEmailMessageService implements MessageService {
    private MessageSender sender;
    @Value("${server.domain.url}")
    private String domainURL;

    @Autowired
    public ResetEmailMessageService(MessageSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(String email, String content) {
        try{
            var message = emailMessage();
            setContent(message, email, content);
            sender.send(message);
        } catch (MessagingException exception) {
            throw new EmailException("Не удалось отправить email");
        }
    }

    private void setContent(Message message, String email, String content) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Восстановление пароля");
        var link = String.format("%s/user/update?email=%s&token=%s", domainURL, email, content);
        var text = String.format("Восстановить пароль можно по ссылке: %s", link);
        message.setText(text);
    }

    @Lookup
    protected Message emailMessage(){
        return null;
    }

    @Autowired
    public void setSender(MessageSender sender) {
        this.sender = sender;
    }
}
