package com.example.simbirsoft.controller;


import com.example.simbirsoft.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerInTest {
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private UserService userService;
    private final MockMvc mvc;

    @Autowired
    public UserControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMock(){
        when(userService.isUpdateAllowed(
                "lyah.artem10@mail.ru",
                "c93d03da-fd05-4beb-99f5-5619dc478da9")).thenReturn(true);
        when(userService.isUpdateAllowed(
                "lyah.artem10@mail.ru",
                "89ca527a-45b6-46ed-8d02-8e3c1c077d72")).thenReturn(false);
    }

    @Test
    public void createUser_If_User_Is_Unauthorized_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("password", "qwerty")
                .param("rePassword", "qwerty");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/log-in"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void forbidden_If_User_Is_Authorized_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("password", "qwerty")
                .param("rePassword", "qwerty");
        mvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void showUpdatePasswordPage_When_Allowed() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("token", "c93d03da-fd05-4beb-99f5-5619dc478da9");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/button").string("Изменить"));
    }

    @Test
    public void redirect_When_ShowUpdatePasswordPage_If_Not_Allowed() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("token", "89ca527a-45b6-46ed-8d02-8e3c1c077d72");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void updateUser_When_User_If_Is_Reset_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("password", "qwerty")
                .param("rePassword", "qwerty")
                .param("token", "c93d03da-fd05-4beb-99f5-5619dc478da9");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/log-in"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void forbidden_When_UpdateUser_If_User_Is_Active_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .post("/user/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("email", "lyah.artem10@mail.ru")
                .param("password", "qwerty")
                .param("rePassword", "qwerty")
                .param("token", "c93d03da-fd05-4beb-99f5-5619dc478da9");
        mvc.perform(request).andExpect(status().isForbidden());
    }
}
