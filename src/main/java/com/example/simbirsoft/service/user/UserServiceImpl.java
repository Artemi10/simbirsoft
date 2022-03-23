package com.example.simbirsoft.service.user;

import com.example.simbirsoft.entity.user.Authority;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.repository.UserRepository;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import com.example.simbirsoft.transfer.auth.UpdateDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
                    .authority(Authority.ACTIVE)
                    .resetToken(null)
                    .applications(new ArrayList<>())
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
        user.setAuthority(Authority.RESET);
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

    @Override
    public boolean isUpdateAllowed(String email, String token) {
        var userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            return user.getResetToken().equals(token)
                    && user.getResetToken() != null
                    && !user.getResetToken().isBlank()
                    && user.getAuthority().equals(Authority.RESET);
        }
        return false;
    }

    @Override
    public void updateUser(UpdateDTO updateDTO) {
        updateDTO.check();
        var user = userRepository.findByEmail(updateDTO.email())
                .orElseThrow(() -> new EntityException("Введён неверный email"));
        if (user.getResetToken().equals(updateDTO.token())
                && user.getAuthority().equals(Authority.RESET)) {
            var hashPassword = passwordEncoder.encode(updateDTO.newPassword());
            user.setPassword(hashPassword);
            user.setAuthority(Authority.ACTIVE);
            user.setResetToken(null);
            userRepository.save(user);
        }
        else throw new EntityException("Введён неверный токен");
    }
}
