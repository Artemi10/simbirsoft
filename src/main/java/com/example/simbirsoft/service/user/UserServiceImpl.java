package com.example.simbirsoft.service.user;

import com.example.simbirsoft.entity.User;
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
}
