package com.example.simbirsoft.service.user;

import com.example.simbirsoft.entity.user.Authority;
import com.example.simbirsoft.entity.user.User;
import com.example.simbirsoft.exception.EntityException;
import com.example.simbirsoft.exception.ValidatorException;
import com.example.simbirsoft.repository.UserRepository;
import com.example.simbirsoft.transfer.auth.SignUpDTO;
import com.example.simbirsoft.transfer.auth.UpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplTest(PasswordEncoder passwordEncoder) {
        this.userRepository = spy(UserRepository.class);
        this.userService = new UserServiceImpl(userRepository, passwordEncoder);
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    public void initMock() {
        var activeUser = User.builder()
                .id(1)
                .email("lyah.artem10@mail.ru")
                .password(passwordEncoder.encode("2424285"))
                .authority(Authority.ACTIVE)
                .resetToken("")
                .notes(new ArrayList<>())
                .build();
        var resetUser = User.builder()
                .id(2)
                .email("lyah.artem10@gmail.com")
                .password(passwordEncoder.encode("qwerty"))
                .authority(Authority.RESET)
                .resetToken("d00a6c00-b554-4637-8939-77ad8e715244")
                .notes(new ArrayList<>())
                .build();
        when(userRepository.findByEmail("lyah.artem10@mail.ru"))
                .thenReturn(Optional.of(activeUser));
        when(userRepository.findByEmail("lyah.artem10@gmail.com"))
                .thenReturn(Optional.of(resetUser));
        when(userRepository.findByEmail(
                argThat(email -> !email.equals("lyah.artem10@gmail.com") && !email.equals("lyah.artem10@mail.ru"))))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenAnswer(answer -> {
                    var user = (User) answer.getArgument(0);
                    return User.builder()
                            .id(3)
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .authority(user.getAuthority())
                            .resetToken(user.getResetToken())
                            .notes(user.getNotes())
                            .build();
                });
    }

    @Test
    public void create_New_User_When_Not_Exist_And_Valid() {
        var validUserDTO = new SignUpDTO("lyah.artem11@mail.ru", "qwerty", "qwerty");
        assertDoesNotThrow(() -> userService.createUser(validUserDTO));
        verify(userRepository, times(1))
                .findByEmail("lyah.artem11@mail.ru");
        verify(userRepository, times(1))
                .save(argThat(
                        userToCreate -> userToCreate.getAuthority().equals(Authority.ACTIVE)
                                && userToCreate.getEmail().equals(validUserDTO.email())
                                && passwordEncoder.matches(validUserDTO.password(), userToCreate.getPassword())
                                && userToCreate.getResetToken().equals("")
                                && userToCreate.getNotes().isEmpty())
                );
    }

    @Test
    public void throw_Exception_When_Create_Invalid_User() {
        var inValidUserDTO = new SignUpDTO("lyah.artem10@mail.ru", "qwerty", "qwerty1");
        var exception = assertThrows(ValidatorException.class, () -> userService.createUser(inValidUserDTO));
        assertEquals("Пароли не совпадают", exception.getMessage());
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void throw_Exception_Create_Existent_User() {
        var existedUserDTO = new SignUpDTO("lyah.artem10@mail.ru", "qwerty", "qwerty");
        var exception = assertThrows(ValidatorException.class, () -> userService.createUser(existedUserDTO));
        assertEquals("Пользователь уже существует", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem10@mail.ru");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void reset_Existent_Active_User() {
        assertDoesNotThrow(() -> userService
                .resetUser("lyah.artem10@mail.ru", "d00a6c00-b554-4637-8939-77ad8e715244"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@mail.ru");
        verify(userRepository, times(1))
                .save(argThat(
                        userToReset -> userToReset.getAuthority().equals(Authority.RESET)
                                && userToReset.getEmail().equals("lyah.artem10@mail.ru")
                                && passwordEncoder.matches("2424285", userToReset.getPassword())
                                && userToReset.getResetToken().equals("d00a6c00-b554-4637-8939-77ad8e715244")
                                && userToReset.getNotes().isEmpty())
                );
    }

    @Test
    public void reset_Existent_Rest_User() {
        assertDoesNotThrow(() -> userService
                .resetUser("lyah.artem10@gmail.com", "d00a6c00-b554-4637-8939-77ad8e715278"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
        verify(userRepository, times(1))
                .save(argThat(
                        userToReset -> userToReset.getAuthority().equals(Authority.RESET)
                                && userToReset.getEmail().equals("lyah.artem10@gmail.com")
                                && passwordEncoder.matches("qwerty", userToReset.getPassword())
                                && userToReset.getResetToken().equals("d00a6c00-b554-4637-8939-77ad8e715278")
                                && userToReset.getNotes().isEmpty())
                );
    }

    @Test
    public void throw_Exception_When_Rest_Nonexistent_User() {
        var exception = assertThrows(EntityException.class, () -> userService
                .resetUser("lyah.artem03@gmail.com", "d00a6c00-b554-4637-8939-77ad8e715278"));
        assertEquals("Введён неверный email", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem03@gmail.com");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void activate_Existent_Active_User() {
        assertDoesNotThrow(() -> userService.activateUser("lyah.artem10@mail.ru"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@mail.ru");
        verify(userRepository, times(1))
                .save(argThat(
                        userToReset -> userToReset.getAuthority().equals(Authority.ACTIVE)
                                && userToReset.getEmail().equals("lyah.artem10@mail.ru")
                                && passwordEncoder.matches("2424285", userToReset.getPassword())
                                && userToReset.getResetToken() == null
                                && userToReset.getNotes().isEmpty())
                );
    }

    @Test
    public void activate_Existent_Rest_User() {
        assertDoesNotThrow(() -> userService.activateUser("lyah.artem10@gmail.com"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
        verify(userRepository, times(1))
                .save(argThat(
                        userToReset -> userToReset.getAuthority().equals(Authority.ACTIVE)
                                && userToReset.getEmail().equals("lyah.artem10@gmail.com")
                                && passwordEncoder.matches("qwerty", userToReset.getPassword())
                                && userToReset.getResetToken() == null
                                && userToReset.getNotes().isEmpty())
                );
    }

    @Test
    public void throw_Exception_When_Activate_Nonexistent_User() {
        var exception = assertThrows(EntityException.class, () -> userService
                .activateUser("lyah.artem03@gmail.com"));
        assertEquals("Введён неверный email", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem03@gmail.com");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void update_Not_Allowed_When_User_Not_Exist() {
        assertFalse(userService.isUpdateAllowed("lyah.artem03@gmail.com", "d00a6c00-b554-4637-8939-77ad8e715244"));
        verify(userRepository, times(1)).findByEmail("lyah.artem03@gmail.com");
    }

    @Test
    public void update_Not_Allowed_When_User_Is_Active() {
        assertFalse(userService.isUpdateAllowed("lyah.artem10@mail.ru", "d00a6c00-b554-4637-8939-77ad8e715244"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@mail.ru");
    }

    @Test
    public void update_Not_Allowed_When_Tokens_Not_Match() {
        assertFalse(userService.isUpdateAllowed("lyah.artem10@gmail.com", "d00a6c00-b554-4637-8939-77ad8e715267"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
    }

    @Test
    public void update_Allowed_When_User_Is_Reset() {
        assertTrue(userService.isUpdateAllowed("lyah.artem10@gmail.com", "d00a6c00-b554-4637-8939-77ad8e715244"));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
    }

    @Test
    public void update_Reset_User_When_Tokens_Match() {
        var validUpdateDTO = new UpdateDTO("lyah.artem10@gmail.com", "2424285",
                "2424285", "d00a6c00-b554-4637-8939-77ad8e715244");
        assertDoesNotThrow(() -> userService.updateUser(validUpdateDTO));
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
        verify(userRepository, times(1))
                .save(argThat(
                        user -> user.getResetToken() == null
                            && user.getAuthority().equals(Authority.ACTIVE)
                            && passwordEncoder.matches(validUpdateDTO.newPassword(), user.getPassword())
                            && user.getEmail().equals("lyah.artem10@gmail.com")
                            && user.getNotes().isEmpty()
                ));
    }

    @Test
    public void throw_Exception_When_Update_Active_User() {
        var updateDTO = new UpdateDTO("lyah.artem10@mail.ru", "qwerty",
                "qwerty", "d00a6c00-b554-4637-8939-77ad8e715244");
        var exception = assertThrows(EntityException.class, () -> userService.updateUser(updateDTO));
        assertEquals("Введён неверный токен", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem10@mail.ru");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void throw_Exception_When_Update_Nonexistent_User() {
        var updateDTO = new UpdateDTO("lyah.artem03@mail.ru", "qwerty",
                "qwerty", "d00a6c00-b554-4637-8939-77ad8e715244");
        var exception = assertThrows(EntityException.class, () -> userService.updateUser(updateDTO));
        assertEquals("Введён неверный email", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem03@mail.ru");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void throw_Exception_When_Update_With_Invalid_Token() {
        var updateDTO = new UpdateDTO("lyah.artem10@gmail.com", "2424285",
                "2424285", "d00a6c00-b554-4637-8939-77ad8e715265");
        var exception = assertThrows(EntityException.class, () -> userService.updateUser(updateDTO));
        assertEquals("Введён неверный токен", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("lyah.artem10@gmail.com");
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void throw_Exception_When_Update_With_Invalid_Password() {
        var updateDTO = new UpdateDTO("lyah.artem10@gmail.com", "2424285",
                "qwerty", "d00a6c00-b554-4637-8939-77ad8e715265");
        var exception = assertThrows(ValidatorException.class, () -> userService.updateUser(updateDTO));
        assertEquals("Пароли не совпадают", exception.getMessage());
        verify(userRepository, times(0)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any());
    }
}
