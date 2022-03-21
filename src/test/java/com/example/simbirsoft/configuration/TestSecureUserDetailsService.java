package com.example.simbirsoft.configuration;

import com.example.simbirsoft.configuration.details.SecureUser;
import com.example.simbirsoft.entity.user.Authority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class TestSecureUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("lyah.artem10@mail.ru")){
            var authorities = List
                    .of(new SimpleGrantedAuthority(Authority.ACTIVE.name()));
            return new SecureUser(
                    1,
                    "lyah.artem10@mail.ru",
                    "$2y$10$LHUDwkfwe1GsXZ7Z0qJKWO6JlDFjQRfrQMclOI9ceQBF4V2Eo7AF",
                    authorities,
                    true);
        }
        else throw new UsernameNotFoundException("User not found");
    }
}
