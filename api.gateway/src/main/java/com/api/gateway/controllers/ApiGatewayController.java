package com.api.gateway.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import com.common.security.AuthPrincipal;

@Controller
public class ApiGatewayController {
    private RestClient restClient = RestClient.create();

    @Value("${booking_service_url}")
    private String bookingServiceUrl;

    @GetMapping("/home")
    public String home() {
        return "/booking/home.html";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {

        String htmlBookingContaier =
                restClient.get()
                .uri(bookingServiceUrl + "/bookings")
                .retrieve()
                .body(String.class);

        model.addAttribute("htmlBookingContaier", htmlBookingContaier);

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
    public String rooms(Model model){

        String roomContainer =
                restClient.get()
                .uri(bookingServiceUrl + "/rooms")
                .retrieve()
                .body(String.class);

        model.addAttribute("roomContainer", roomContainer);

        return "/booking/rooms.html";
    }
}
