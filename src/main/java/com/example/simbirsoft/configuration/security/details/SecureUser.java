package com.example.simbirsoft.configuration.security.details;

import com.example.simbirsoft.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public record SecureUser(
        long id,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        boolean enabled,
        Map<String, Object> claims,
        OidcIdToken token) implements UserDetails, OidcUser {

    public SecureUser(User user) {
       this(
               user.getId(),
               user.getEmail(),
               user.getPassword(),
               Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority().name())),
               true,
               new HashMap<>(),
               null);
    }

    public SecureUser(User user, Map<String, Object> claims, OidcIdToken token) {
        this(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority().name())),
                true,
                claims,
                token);
    }

    public SecureUser() {
        this(
                -1,
                null,
                null,
                Collections.emptyList(),
                false,
                new HashMap<>(),
                null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return new OidcUserInfo(getClaims());
    }

    @Override
    public OidcIdToken getIdToken() {
        return token;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return claims;
    }

    @Override
    public String getName() {
        return (String) claims.get("name");
    }
}
