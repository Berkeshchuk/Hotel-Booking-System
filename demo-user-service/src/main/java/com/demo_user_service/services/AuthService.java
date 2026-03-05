package com.demo_user_service.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.security.AuthPrincipalDto;
import com.demo_user_service.data.dto.UserDto;
import com.demo_user_service.data.dto.dto_mappers.UserMapper;
import com.demo_user_service.data.enums.AccountState;
import com.demo_user_service.data.models.User;
import com.demo_user_service.exceptions.LoginAlreadyInUseException;
import com.demo_user_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManager authenticationManager;
    // private final RememberMeServices rememberMeServices;

    @Transactional
    public void addUser(UserDto dto){
        if(dto == null){
            throw new IllegalArgumentException("cant be null");
        }
        if(userRepository.existsByLogin(dto.getLogin())){
            throw new LoginAlreadyInUseException("login already in use");
        }


        User entity = userMapper.toEntity(dto);
        entity.setHashPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRole("USER");
        entity.setAccountState(AccountState.ACTIVE);
        entity.setTrustLevel(0);
        entity.setConsecutiveCancellations(0);
        userRepository.save(entity);
    }


    public void resetPassword() {
    }

    // при оновленні логіна потрібно оновити і відповідний username в таблиці persistanse_logins
    // бо redis-сесія спробує загрузити користувача із старим логіном(username), який не змінився в persistanse_logins
    public void updateLogin(){}


    public AuthPrincipalDto loadUserByUsername(String login) throws UsernameNotFoundException {
        if(login == null){
            throw new IllegalArgumentException();
        }

        User user = userRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var authPrincipal = new AuthPrincipalDto();
        authPrincipal.setId(user.getId());
        authPrincipal.setLogin(user.getLogin());
        authPrincipal.setMobileNumber(user.getPhoneNumber());
        authPrincipal.setPassword(user.getHashPassword());
        authPrincipal.setRole(user.getRole());
        authPrincipal.setIsEnabled(true);
        authPrincipal.setIsAccountNonLocked(user.getAccountState() != AccountState.BLOCKED);
        return authPrincipal;
    }

}
