package com.example.simbirsoft.service.user;

import com.example.simbirsoft.transfer.auth.SignUpDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void createUser(SignUpDTO signUpDTO);
    void resetUser(String email, String token);
    void activateUser(String email);
    boolean isUpdateAllowed(String email, String token);
}
