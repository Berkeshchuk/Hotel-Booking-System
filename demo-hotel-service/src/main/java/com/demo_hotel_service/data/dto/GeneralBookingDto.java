package com.demo_hotel_service.data.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.common.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GeneralBookingDto {
    private Long id;
    private LocalDateTime orderDateTime;

    private Long userId;
    private String phoneNumber;
    private BookingStatus status;

    private List<BookingUnitDto> bookingUnits;
    private String clientComment;
}


