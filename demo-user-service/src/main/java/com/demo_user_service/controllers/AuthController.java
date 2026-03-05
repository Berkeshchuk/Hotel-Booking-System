package com.demo_user_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo_user_service.data.dto.UserDto;
import com.demo_user_service.services.AuthService;

import com.common.security.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(@ModelAttribute UserDto userDto) {
        authService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/auth")
    public String authPage(
        @AuthenticationPrincipal AuthPrincipal userDetails,
        @Value("${booking_service_url}") String bookingServiceUrl
    ) {
        if (userDetails != null) {
            // Якщо користувач вже залогінений, перенаправляємо його на /home
            return "redirect:" + bookingServiceUrl + "/home";
        }
        return "auth.html";
    }

    @GetMapping("api/internal/auth-principal")
    public ResponseEntity<?> authPrincipal(@RequestParam() String login){
        return ResponseEntity.ok(authService.loadUserByUsername(login));
    }


}
