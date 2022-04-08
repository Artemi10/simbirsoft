package com.example.simbirsoft.controller;

import com.example.simbirsoft.configuration.TestSecureUserDetailsService;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.service.stat.DayStatService;
import com.example.simbirsoft.service.stat.HourStatService;
import com.example.simbirsoft.service.stat.MonthStatService;
import com.example.simbirsoft.service.stat.StatService;
import com.example.simbirsoft.transfer.stat.StatRequestDTO;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(StatController.class)
public class StatControllerInTest {
    @MockBean(name = "days")
    private DayStatService dayStatService;
    @MockBean(name = "hours")
    private HourStatService hourStatService;
    @MockBean(name = "months")
    private MonthStatService monthStatService;
    @MockBean
    private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    private final MockMvc mvc;

    @Autowired
    public StatControllerInTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @BeforeEach
    public void initMock() {
        when(dayStatService.createStats(1, "lyah.artem10@mail.ru"))
                .thenThrow(EntityException.class);
        when(dayStatService.createStats(eq(1L), any(StatRequestDTO.class)))
                .thenThrow(EntityException.class);
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
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void create_Month_Stats_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=months&from=04.07.2022&to=07.07.2022")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(monthStatService, times(1))
                .createStats(eq(2L), any(StatRequestDTO.class));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void create_Days_Stats_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=days&from=04.07.2022&to=07.07.2022")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(dayStatService, times(1))
                .createStats(eq(2L), any(StatRequestDTO.class));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void create_Hours_Stats_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=hours&from=04.07.2022&to=07.07.2022")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(hourStatService, times(1))
                .createStats(eq(2L), any(StatRequestDTO.class));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void crete_Basic_Stats_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=months&from=&to=")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(monthStatService, times(1))
                .createStats(2,  "lyah.artem10@mail.ru");
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void return_Empty_Stats_When_Time_Is_Invalid_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=hours&from=rthte&to=tgdhjy")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void return_Empty_Stats_When_Create_Basic_Stat_If_App_Is_Not_Found_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/1/stat?type=days&from=&to=")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(dayStatService, times(1))
                .createStats(eq(1L), eq("lyah.artem10@mail.ru"));
    }

    @Test
    @WithUserDetails(
            value = "lyah.artem10@mail.ru",
            userDetailsServiceBeanName = "testUserDetailsService"
    )
    public void return_Empty_Stats_When_App_Is_Not_Found_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/1/stat?type=days&from=04.07.2022&to=07.07.2022")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().isOk());
        verify(dayStatService, times(1))
                .createStats(eq(1L), any(StatRequestDTO.class));
    }

    @Test
    public void redirect_When_Create_Stat_When_User_Is_Unauthorized_Test() throws Exception {
        var request = MockMvcRequestBuilders
                .get("/app/2/stat?type=days&from=&to=")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        mvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/log-in"));
    }
}
