package com.demo_hotel_service.data.dto;

import java.time.LocalDateTime;

import com.common.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "classType")
@JsonSubTypes({
                @JsonSubTypes.Type(value = BookingSpaUnitDto.class, name = "SPA_BOOKING"),
                @JsonSubTypes.Type(value = BookingRoomUnitDto.class, name = "ROOM_BOOKING")
})
public class BookingUnitDto {
        private Long id;
        private ServiceUnitDto serviceUnit;
        private Long serviceUnitId;
        private BookingStatus status;
        private Integer clientCount;
        private LocalDateTime start;
        private LocalDateTime end;
        private LocalDateTime orderDateTime;
}
