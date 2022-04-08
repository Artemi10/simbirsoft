package com.example.simbirsoft.controller;

import com.example.simbirsoft.exception.EmailException;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.service.email.MessageService;
import com.example.simbirsoft.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailController.class)
public class EmailControllerInTest {
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    @MockBean
    private UserService userService;
    @MockBean
    private MessageService messageService;
    private final MockMvc mvc;

    @Autowired
    public EmailControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMocks(){
        doThrow(new EntityException("Введён неверный email"))
                .when(userService)
                .resetUser(eq("lyah.artem10@gmail.com"), anyString());
        doNothing()
                .when(userService)
                .resetUser(eq("lyah.artem10@mail.ru"), anyString());
        doThrow(new EmailException("Не удалось отправить email"))
                .when(messageService)
                .sendMessage(eq("d@mail.ru"), anyString());
    }

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    public void sendEmail_When_Email_Is_Valid_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("email=lyah.artem10@mail.ru");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/div/p")
                        .string("На вашу почту было отправлено письмо с инструкцией по восстановлению пароля"));
        verify(userService, times(1))
                .resetUser(eq("lyah.artem10@mail.ru"), anyString());
        verify(messageService, times(1))
                .sendMessage(eq("lyah.artem10@mail.ru"), anyString());
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void forbidden_When_SendEmail_With_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("email=lyah.artem10@mail.ru");
        mvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void show_Error_When_sendEmail_If_Email_Does_Not_Exist_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("email=lyah.artem10@gmail.com");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/p").string("Введён неверный email"));
        verify(userService, times(1))
                .resetUser(eq("lyah.artem10@gmail.com"), anyString());
        verify(messageService, times(0))
                .sendMessage(anyString(), anyString());
    }

    @Test
    public void show_Error_When_Can_Not_sendEmail_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("email=d@mail.ru");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/p").string("Не удалось отправить email"));
        verify(userService, times(1))
                .resetUser(eq("d@mail.ru"), anyString());
        verify(messageService, times(1))
                .sendMessage(anyString(), anyString());
    }
}
