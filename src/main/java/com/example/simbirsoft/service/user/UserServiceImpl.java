package com.example.simbirsoft.service.user;

import com.example.simbirsoft.entity.user.Authority;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.repository.UserRepository;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(SignUpDTO signUpDTO) {
        signUpDTO.check();
        var userOptional = userRepository.findByEmail(signUpDTO.email());
        if (userOptional.isEmpty()){
            var hashPassword = passwordEncoder.encode(signUpDTO.password());
            var user = User.builder()
                    .email(signUpDTO.email())
                    .password(hashPassword)
                    .build();
            userRepository.save(user);
        }
        else throw new ValidatorException("Пользователь уже существует");
    }

    @Override
    public void resetUser(String email, String token) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityException("Введён неверный email"));
        user.setResetToken(token);
        user.setAuthority(Authority.RESET_UNCONFIRMED);
        userRepository.save(user);
    }

    @Override
    public void activateUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityException("Введён неверный email"));
        user.setResetToken(null);
        user.setAuthority(Authority.ACTIVE);
        userRepository.save(user);
    }
}
