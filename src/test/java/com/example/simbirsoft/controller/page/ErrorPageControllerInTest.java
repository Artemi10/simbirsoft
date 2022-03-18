package com.example.simbirsoft.controller.page;

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
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ErrorPageController.class)
public class ErrorPageControllerInTest {
    @MockBean
    private UserDetailsService userDetailsService;
    private final MockMvc mvc;

    @Autowired
    public ErrorPageControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    public void redirect_When_ShowErrorPage_For_Unauthorized_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/error");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }

    @Test
    @WithMockUser(
            username = "lyah.artem10@mail.ru",
            password = "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
            authorities = { "ACTIVE" }
    )
    public void showErrorPage_For_Active_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/error");
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
    public void forbidden_When_ShowErrorPage_For_Rest_User_Test() throws Exception {
        var request = MockMvcRequestBuilders.get("/error");
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
