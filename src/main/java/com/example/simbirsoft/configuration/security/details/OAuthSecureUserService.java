package com.example.simbirsoft.configuration.security.details;

import com.example.simbirsoft.entity.user.Authority;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OAuthSecureUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        var email = (String) userRequest.getIdToken().getClaims().get("email");
        if (email != null) {
            var userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                return new SecureUser(
                        userOptional.get(),
                        userRequest.getIdToken().getClaims(),
                        userRequest.getIdToken());
            }
            else {
                var hashPassword = passwordEncoder.encode(UUID.randomUUID().toString());
                var user = User.builder()
                        .email(email)
                        .password(hashPassword)
                        .authority(Authority.ACTIVE)
                        .resetToken(null)
                        .apps(new ArrayList<>())
                        .build();
                return new SecureUser(
                        userRepository.save(user),
                        userRequest.getIdToken().getClaims(),
                        userRequest.getIdToken());
            }
        }
        else return new SecureUser();
    }
}
