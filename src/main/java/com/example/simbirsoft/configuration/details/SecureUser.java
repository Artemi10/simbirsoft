package com.example.simbirsoft.configuration.details;

import com.example.simbirsoft.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public record SecureUser(String email, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled)
        implements UserDetails {

    public SecureUser(User user) {
       this(user.getEmail(), user.getPassword(), Collections.emptyList(), true);
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

    public static SecureUser empty(){
        return new SecureUser(null, null, null, false);
    }
}
