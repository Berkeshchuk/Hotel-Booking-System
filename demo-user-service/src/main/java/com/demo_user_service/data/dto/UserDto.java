package com.demo_user_service.data.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    Long id;
    String login;
    String role;
    String password;
    String email;
    String phoneNumber;
    Integer trustLevel;
    Integer consecutiveCancellations;
    LocalDateTime registered;
}
