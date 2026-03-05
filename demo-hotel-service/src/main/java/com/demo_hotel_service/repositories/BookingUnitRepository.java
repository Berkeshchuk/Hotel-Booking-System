package com.demo_hotel_service.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo_hotel_service.data.models.bookings.BookingUnit;

public interface BookingUnitRepository extends JpaRepository<BookingUnit, Long> {
    public List<BookingUnit> findAllByGeneralBookingId(Long generalBookingId, Pageable pageable);
}
