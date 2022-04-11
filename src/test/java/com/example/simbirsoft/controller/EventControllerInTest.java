package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.TestSecureUserDetailsService;
import com.example.simbirsoft.entity.Event;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.service.event.EventService;
import com.example.simbirsoft.transfer.event.EventRequestDTO;
import com.example.simbirsoft.transfer.event.EventResponseDTO;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
class EventControllerInTest {
    @MockBean
    private EventService eventService;
    @MockBean
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    private final MockMvc mvc;

    @Autowired
    public EventControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMock(){
        var events = List.of(
                Event.builder()
                        .id(1)
                        .name("User successfully signed up")
                        .extraInformation("Extra information")
                        .time(new Timestamp(new Date().getTime()))
                        .build(),
                Event.builder()
                        .id(2)
                        .name("User successfully logged in")
                        .extraInformation("Extra information")
                        .time(new Timestamp(new Date().getTime()))
                        .build(),
                Event.builder()
                        .id(3)
                        .name("Add new note")
                        .extraInformation("Extra information")
                        .time(new Timestamp(new Date().getTime()))
                        .build(),
                Event.builder()
                        .id(4)
                        .name("Update note")
                        .extraInformation("Extra information")
                        .time(new Timestamp(new Date().getTime()))
                        .build()
        );
        var responseDTOs = events.stream()
                .map(event -> new EventResponseDTO(1, event))
                .toList();
        when(eventService.findAppEvents(eq(1L), eq("lyah.artem10@mail.ru")))
                .thenReturn(responseDTOs);
        when(eventService.findAppEvents(eq(2L), eq("lyah.artem10@mail.ru")))
                .thenReturn(new ArrayList<>());
        doNothing().when(eventService).addEvent(
                eq(1L),
                any(EventRequestDTO.class),
                eq("lyah.artem10@mail.ru")
        );
        doThrow(new EntityException("Приложение не найдено"))
                .when(eventService).addEvent(
                        eq(2L),
                        any(EventRequestDTO.class),
                        eq("lyah.artem10@mail.ru")
                );
    }

    @TestConfiguration
    public static class SecurityTestConfig {
        @Bean("testUserDetailsService")
        public UserDetailsService testUserDetailsService(){
            return new TestSecureUserDetailsService();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    public void redirect_When_ShowAppEvents_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/1/events");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void addAppEvent_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/app/event")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("appId","1")
                .param("name","Log In Event")
                .param("extraInformation", "Extra Information");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps"));
    }

    @Test
    public void redirect_addAppEvent_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/app/event")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("appId","1")
                .param("name","Log In Event")
                .param("extraInformation", "Extra Information");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void redirect_addAppEvent_If_User_Does_Not_Have_App() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/app/event")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("appId","2")
                .param("name","Log In Event")
                .param("extraInformation", "Extra Information");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
