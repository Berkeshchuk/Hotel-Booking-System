package com.demo_hotel_service.data.models.bookings;

import java.time.LocalDateTime;

import com.demo_hotel_service.data.models.hotel_offerings.rooms.RoomUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)

@Entity
@Table(name = "booking_room_units")
public class BookingRoomUnit extends BookingUnit {
    public BookingRoomUnit(long id, GeneralBooking booking, int clientCount, LocalDateTime start, LocalDateTime end, RoomUnit roomUnit){
        super(id, booking, roomUnit, clientCount, start, end);
    }
}
