package com.example.simbirsoft.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerInTest {
    @MockBean
    private UserDetailsService userDetailsService;
    private final MockMvc mvc;

    @Autowired
    public AuthControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void showLogInPage_For_Unauthorized_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/log-in");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/button").string("Вход"));
    }

    @Test
    public void showLogInPage_With_Error_Message_For_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/log-in?error=true");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/p[3]").string("Введён неверный логин или пароль"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void redirect_To_Main_When_showLogInPage_For_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/log-in");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "RESET" }
    )
    public void redirect_To_Main_When_showLogInPage_For_Reset_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/log-in");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void showSignUpPage_For_Unauthorized_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/sign-up");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/button").string("Регистрация"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void forbidden_When_showSignUpPage_For_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/sign-up");
        mvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "RESET" }
    )
    public void forbidden_When_showSignUpPage_For_Reset_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/sign-up");
        mvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    public void showEmailPage_For_Unauthorized_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/email");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[2]/div/form/button").string("Отправить"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void forbidden_When_showEmailPage_For_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/email");
        mvc.perform(request).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "RESET" }
    )
    public void forbidden_When_showEmailPage_For_Reset_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/auth/email");
        mvc.perform(request).andExpect(status().isForbidden());
    }
}
