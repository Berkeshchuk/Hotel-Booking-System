package com.demo_hotel_service.data.dto;

import com.common.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingSpaUnitDto extends BookingUnitDto {
    private Gender preferedGender;
}
