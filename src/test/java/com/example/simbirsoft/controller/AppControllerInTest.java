package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.TestSecureUserDetailsService;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.service.application.AppService;
import com.example.simbirsoft.transfer.application.AppResponseDTO;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AppController.class)
public class AppControllerInTest {
    @MockBean
    private AppService appService;
    @MockBean
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    private final MockMvc mvc;

    @Autowired
    public AppControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMock(){
        doThrow(new EntityException("Приложение не найдено"))
                .when(appService)
                .findUserApp(4, "lyah.artem10@mail.ru");
        when(appService.findUserApp(1, "lyah.artem10@mail.ru"))
                .thenReturn(new AppResponseDTO(1, "Simple CRUD App", "21 марта 2022 16:24:54"));
        doThrow(new ValidatorException("Введёно некорректное название приложения"))
                .when(appService)
                .addUserApp(
                        anyLong(),
                        argThat(requestNoteDTO -> requestNoteDTO.name().equals("")));
        doThrow(new ValidatorException("Введёно некорректное название приложения"))
                .when(appService)
                .updateUserApp(
                        anyLong(),
                        argThat(requestNoteDTO -> requestNoteDTO.name().equals("")),
                        anyString());
    }

    @TestConfiguration
    static class SecurityTestConfig {
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
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void showUserApps_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps");
        mvc.perform(request).andExpect(status().isOk());
        verify(appService, times(1))
                .findUserApps(1, "lyah.artem10@mail.ru");
    }

    @Test
    public void forbidden_When_ShowUserApps_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps");
        mvc.perform(request).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void createUserApp_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "Todo list");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps"));
        verify(appService, times(1))
                .addUserApp(
                        eq(1L),
                        argThat(appRequestDTO -> appRequestDTO.name().equals("Todo list")));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void show_Error_Message_When_CreateUserApp_RequestBody_Is_Invalid() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/p")
                        .string("Введёно некорректное название приложения"));
    }

    @Test
    public void forbidden_When_CreateUserApp_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "Todo list");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void updateUserApp_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps/1/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "Todo list");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(appService, times(1))
                .updateUserApp(
                        eq(1L),
                        argThat(appRequestDTO -> appRequestDTO.name().equals("Todo list")),
                        eq("lyah.artem10@mail.ru"));
    }

    @Test
    public void forbidden_When_UpdateUserApps_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps/1/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "Todo list");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void show_Error_Message_When_UpdateUserApps_RequestBody_Is_Invalid() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/apps/2/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", "");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/p")
                        .string("Введёно некорректное название приложения"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void deleteUserApp_If_User_Is_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/1/delete")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps"));
        verify(appService, times(1))
                .deleteUserApp(1L, "lyah.artem10@mail.ru");
    }

    @Test
    public void forbidden_When_DeleteUserApp_If_User_Is_Not_Authenticated() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/1/delete")
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
    public void showCreateAppForm_If_App_Is_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/add")
                .with(csrf());
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void forbidden_When_ShowCreateAppForm_If_User_Is_Not_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/add")
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
    public void showUpdateAppForm_If_User_Is_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/1/update")
                .with(csrf());
        mvc.perform(request).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void redirect_When_ShowUpdateAppForm_If_App_Not_Found_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/4/update")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/apps"));
    }

    @Test
    public void forbidden_When_ShowUpdateAppForm_If_User_Is_Not_Authenticated_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/apps/1/update")
                .with(csrf());
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }
}
