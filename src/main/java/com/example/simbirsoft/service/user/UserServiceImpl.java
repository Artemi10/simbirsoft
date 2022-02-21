package com.example.simbirsoft.service.user;

import com.example.simbirsoft.entity.User;
import com.example.simbirsoft.exception.AuthenticationException;
import com.example.simbirsoft.repository.UserRepository;
import com.example.simbirsoft.transfer.auth.LogInDTO;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public void signUp(SignUpDTO signUpDTO) {
        signUpDTO.check();
        var userOptional = userRepository.findByEmail(signUpDTO.email());
        if (userOptional.isEmpty()){
            var user = User.builder()
                    .email(signUpDTO.email())
                    .password(signUpDTO.password())
                    .build();
            userRepository.save(user);
        }
        else throw new AuthenticationException("Пользователь уже существует");
    }

    @Override
    public void logIn(LogInDTO logInDTO) {
        logInDTO.check();
        var user = userRepository.findByEmail(logInDTO.email())
                .orElseThrow(() -> new AuthenticationException("Пользователя не существует"));
        if (!user.getPassword().equals(logInDTO.password())){
            throw new AuthenticationException("Введён неверный логин или пароль");
        }
    }
}
