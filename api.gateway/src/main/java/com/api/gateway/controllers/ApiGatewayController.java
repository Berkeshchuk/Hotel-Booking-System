package com.api.gateway.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import com.api.gateway.config.security.JwtUtil;
import com.common.security.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ApiGatewayController {
    private final RestClient restClient = RestClient.create();
    private final JwtUtil jwtUtil;

    @Value("${booking_service_url}")
    private String bookingServiceUrl;

    @GetMapping("/home")
    public String home() {
        return "/booking/home.html";
    }

    @GetMapping("/bookings")
    public String bookings(Model model, @AuthenticationPrincipal AuthPrincipal userDetails) {

        // Якщо користувач не авторизований - не виконуємо запит до мікросервісу
        // бронювання, а вказуємо про потребу авторизуватися
        if (userDetails == null) {
            model.addAttribute("showLoginOverlay", true);
            return "/booking/bookings.html";
        }

        String htmlBookingContaier = restClient.get()
                .uri(bookingServiceUrl + "/bookings")
                .header("Authorization", "Bearer " + jwtUtil.generateUserToken(
                        userDetails.getId(),
                        userDetails.getLogin(),
                        userDetails.getRole()))
                .retrieve()
                .body(String.class);

        model.addAttribute("htmlBookingContaier", htmlBookingContaier);
        model.addAttribute("showLoginOverlay", false);

        return "/booking/bookings.html";
    }

    @GetMapping("/auth")
    public String auth(@AuthenticationPrincipal AuthPrincipal userDetails, Model model) {
        if (userDetails != null) {
            // Якщо користувач вже залогінений, перенаправляємо його на /home
            return "redirect:" + "/home";
        }
        model.addAttribute("isAuthPage", true);
        return "/auth/auth.html";
    }

    @GetMapping("/about-us")
    public String about_us() {
        return "/common/about-us.html";
    }

    @GetMapping("/rooms")
    public String rooms(Model model, @AuthenticationPrincipal AuthPrincipal userDetails) {

        String roomContainer = "";
        if (userDetails == null) {
            roomContainer = restClient.get()
                    .uri(bookingServiceUrl + "/rooms")
                    .retrieve()
                    .body(String.class);
        } else {
            roomContainer = restClient.get()
                    .uri(bookingServiceUrl + "/rooms")
                    .header("Authorization", "Bearer " + jwtUtil.generateUserToken(
                        userDetails.getId(),
                        userDetails.getLogin(),
                        userDetails.getRole()))
                    .retrieve()
                    .body(String.class);
        }

        model.addAttribute("roomContainer", roomContainer);

        return "/booking/rooms.html";
    }
}
