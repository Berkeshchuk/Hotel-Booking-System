package com.demo_hotel_service.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.demo_hotel_service.data.dto.GeneralBookingDto;
import com.demo_hotel_service.services.GeneralBookingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookingController {
    private final GeneralBookingService gBookingService;

    @GetMapping("/bookings")
    public String getGeneralBookings(Model model){
        return gBookingService.getGeneralBookingsForAdminHtml(model);
    }


    @GetMapping("/api/bookings")
    public ResponseEntity<?> getGeneralBookings(){
        return ResponseEntity.ok( gBookingService.getGeneralBookingsForAdmin(PageRequest.of(0, 12)));
    }

    @PostMapping("/api/bookings")
    public ResponseEntity<?> addGeneralBooking(@RequestBody GeneralBookingDto dto){
        return ResponseEntity.ok(gBookingService.addGeneralBooking(dto));
    }
}
