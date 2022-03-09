package com.example.simbirsoft.configuration;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;
import java.util.Properties;

@Configuration
public class EmailConfig {
    private Properties emailProperties;
    private Properties emailPropertiesConfig;

    @PostConstruct
    public void enableSSL(){
        var disabledAlgorithms =
                "RC4, DES, MD5withRSA, DH keySize < 1024, EC keySize < 224, 3DES_EDE_CBC, anon, NULL";
        Security.setProperty("jdk.tls.disabledAlgorithms", disabledAlgorithms);
    }

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

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Message emailMessage(Session session) throws MessagingException {
        var message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailProperties.getProperty("emailAddress")));
        return message;
    }

    @Bean
    protected Session session() {
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
