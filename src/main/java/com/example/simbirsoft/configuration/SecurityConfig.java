package com.example.simbirsoft.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${remember-me.key}")
    private String rememberMeKey;
    private final UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequestsConfig -> authorizeRequestsConfig
                        .antMatchers("/", "/css/*", "/js/*", "/images/*").permitAll()
                        .antMatchers("/auth/sign-up", "/auth/log-in",  "/auth/email", "/auth/email/*",
                                "/auth/log-in/*", "/user", "/user/update", "/user/update/*", "/email").not().authenticated()
                        .anyRequest().authenticated())
                .formLogin(formLoginConfig -> formLoginConfig
                        .defaultSuccessUrl("/")
                        .loginPage("/auth/log-in")
                        .failureUrl("/auth/log-in?error=true")
                        .usernameParameter("email"))
                .logout(logoutConfig -> logoutConfig
                        .permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/auth/log-out")
                        .logoutSuccessUrl("/auth/log-in"))
                .rememberMe(rememberMeConfig -> rememberMeConfig
                        .key(rememberMeKey)
                        .rememberMeCookieName("authorization")
                        .useSecureCookie(true)
                        .userDetailsService(userDetailsService))
                .csrf(csrfConfig -> csrfConfig
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                        .ignoringAntMatchers("/auth/sign-up", "/auth/log-in",  "/auth/email", "/auth/email/*",
                                "/auth/log-in/*", "/user", "/user/update", "/user/update/*", "/email")
                );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
