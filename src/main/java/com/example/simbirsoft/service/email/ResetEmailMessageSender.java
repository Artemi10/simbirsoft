package com.example.simbirsoft.service.email;

import com.example.simbirsoft.exception.EmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ResetEmailMessageSender implements MessageSender {
    @Value("${server.domain.url}")
    private String domainURL;
    private Properties emailProperties;
    private Properties emailPropertiesConfig;

    @PostConstruct
    public void init() throws IOException {
        emailProperties = loadProperties("email-properties/email.properties");
        emailPropertiesConfig = loadProperties("email-properties/email-config.properties");
    }

    private Properties loadProperties(String path) throws IOException {
        var properties = new Properties();
        var inputStream = new ClassPathResource(path).getInputStream();
        properties.load(inputStream);
        return properties;
    }

    @Override
    public void sendMessage(String email, String content) {
        try{
            var message = createMessage(email);
            setContent(message, email, content);
            Transport.send(message);
        } catch (MessagingException exception) {
            throw new EmailException("Не удалость отправить email");
        }
    }

    protected void setContent(Message message, String email, String content) throws MessagingException {
        message.setSubject("Востановление пароля");
        var link = String.format("%s/users/update?email=%s&token=%s", domainURL, email, content);
        var text = String.format("Востановить пароль можно по ссылке: %s", link);
        message.setText(text);
    }

    private Message createMessage(String email) throws MessagingException {
        var message = new MimeMessage(createSession());
        message.setFrom(new InternetAddress(emailProperties.getProperty("emailAddress")));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        return message;
    }

    private Session createSession() {
        return Session.getDefaultInstance(emailPropertiesConfig, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                var address = emailProperties.getProperty("emailAddress");
                var password = emailProperties.getProperty("emailPassword");
                return new PasswordAuthentication(address, password);
            }
        });
    }
}
