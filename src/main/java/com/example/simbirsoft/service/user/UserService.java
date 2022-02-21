package com.example.simbirsoft.service.user;

import com.example.simbirsoft.transfer.auth.LogInDTO;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void signUp(SignUpDTO signUpDTO);
    void logIn(LogInDTO logInDTO);
}
