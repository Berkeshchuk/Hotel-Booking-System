package com.demo_user_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.common.security.AuthPrincipalDto;
import com.demo_user_service.data.dto.UserDto;
import com.demo_user_service.data.dto.dto_mappers.UserMapper;
import com.demo_user_service.data.enums.AccountState;
import com.demo_user_service.data.models.User;
import com.demo_user_service.exceptions.LoginAlreadyInUseException;
import com.demo_user_service.repositories.UserRepository;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService; 

    @Test
    void addUser_shouldSaveUser_whenLoginIsFree() {
        UserDto dto = new UserDto();
        dto.setLogin("test");
        dto.setPassword("1234");

        User entity = new User();
        
        when(userRepository.existsByLogin("test")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("1234")).thenReturn("hashed");

        authService.addUser(dto);

        verify(userRepository).save(entity);
        assertEquals("hashed", entity.getHashPassword());
        assertEquals("USER", entity.getRole());
        assertEquals(AccountState.ACTIVE, entity.getAccountState());
    }

    @Test
    void addUser_shouldThrowException_whenDtoIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.addUser(null));
    }

    @Test
    void addUser_shouldThrowException_whenLoginExists() {
        UserDto dto = new UserDto();
        dto.setLogin("admin");

        when(userRepository.existsByLogin("admin")).thenReturn(true);

        assertThrows(LoginAlreadyInUseException.class,
                () -> authService.addUser(dto));
    }

    @Test
    void loadUserByUsername_shouldReturnPrincipal_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setLogin("test");
        user.setHashPassword("hash");
        user.setRole("USER");
        user.setAccountState(AccountState.ACTIVE);

        when(userRepository.findByLogin("test")).thenReturn(Optional.of(user));

        AuthPrincipalDto dto = authService.loadUserByUsername("test");

        assertEquals("test", dto.getLogin());
        assertEquals("hash", dto.getPassword());
        assertTrue(dto.getIsEnabled());
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByLogin("test"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> authService.loadUserByUsername("test"));
    }
}