package com.example.simbirsoft.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${remember-me.key}")
    private String rememberMeKey;
    private final OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequestsConfig -> authorizeRequestsConfig
                        .antMatchers("/", "/css/*", "/js/*", "/images/*").permitAll()
                        .antMatchers("/auth/**", "/user/**", "/email").not().authenticated()
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
                                "/auth/log-in/*", "/user", "/user/update", "/user/update/*", "/email"))
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint()
                        .baseUri("/auth/log-in")
                        .and()
                        .userInfoEndpoint()
                        .oidcUserService(oAuth2UserService));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.supports(UsernamePasswordAuthenticationToken.class);
        return provider;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
