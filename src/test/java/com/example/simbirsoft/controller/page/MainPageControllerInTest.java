package com.example.simbirsoft.controller.page;

import com.example.simbirsoft.configuration.TestSecureUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(MainPageController.class)
public class MainPageControllerInTest {
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    private final MockMvc mvc;

    @Autowired
    public MainPageControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Test
    public void show_Unauthorized_MainPage_For_Unauthorized_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[1]/a").string("На главную"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[2]/a").string("Регистрация"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[3]/a").string("Вход"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void show_Active_MainPage_For_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/");
        mvc.perform(request)
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[1]/a").string("На главную"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[2]/a").string("Приложения"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[3]/a").string("Выход"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "RESET" }
    )
    public void show_Unauthorized_MainPage_For_Reset_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[1]/a").string("На главную"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[2]/a").string("Приложения"))
                .andExpect(xpath("/html/body/div[1]/div/nav/div/ul/li[3]/a").string("Выход"));
    }
}
