package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.TestSecureUserDetailsService;
import com.example.simbirsoft.service.note.NoteService;
import com.example.simbirsoft.transfer.note.ResponseNoteDTO;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(NoteController.class)
public class NoteControllerInTest {
    @MockBean
    private NoteService noteService;
    private final MockMvc mvc;

    @Autowired
    public NoteControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMock(){
        when(noteService.findUserNote(1, "lyah.artem10@mail.ru"))
                .thenReturn(new ResponseNoteDTO(1, "Gym", "Go to the gym", "21 марта 2022 16:24:54"));
    }

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean("testUserDetailsService")
        public UserDetailsService testUserDetailsService(){
           return new TestSecureUserDetailsService();
        }
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void showUserNotes_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes");
        mvc.perform(request).andExpect(status().isOk());
        verify(noteService, times(1))
                .findUserNotes(1, "lyah.artem10@mail.ru");
    }

    @Test
    public void forbidden_When_ShowUserNotes_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes");
        mvc.perform(request).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void createUserNote_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/notes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", "Gym")
                .param("text", "Go to gym on Thursday");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
        verify(noteService, times(1))
                .addUserNote(
                        eq(1L),
                        argThat(requestNoteDTO ->
                                requestNoteDTO.title().equals("Gym")
                                && requestNoteDTO.text().equals("Go to gym on Thursday")));
    }

    @Test
    public void forbidden_When_CreateUserNote_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/notes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", "Спортзал")
                .param("text", "Сходить в спортзал в четверг");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void updateUserNotes_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/notes/1/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", "Gym")
                .param("text", "Go to gym on Thursday");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(noteService, times(1))
                .updateUserNote(
                        eq(1L),
                        argThat(requestNoteDTO -> requestNoteDTO.title().equals("Gym")
                                && requestNoteDTO.text().equals("Go to gym on Thursday")),
                        eq("lyah.artem10@mail.ru"));
    }

    @Test
    public void forbidden_When_UpdateUserNotes_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/notes/1/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("title", "Gym")
                .param("text", "Go to gym on Thursday");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void deleteUserNote_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/1/delete")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
        verify(noteService, times(1))
                .deleteUserNote(1L, "lyah.artem10@mail.ru");
    }

    @Test
    public void forbidden_When_DeleteUserNote_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/1/delete")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void showCreateNoteForm_If_User_Is_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/add")
                .with(csrf());
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void forbidden_When_ShowCreateNoteForm_If_User_Is_Not_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/add")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void showUpdateNoteForm_If_User_Is_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/1/update")
                .with(csrf());
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void forbidden_When_ShowUpdateNoteForm_If_User_Is_Not_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/notes/1/update")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }
}
