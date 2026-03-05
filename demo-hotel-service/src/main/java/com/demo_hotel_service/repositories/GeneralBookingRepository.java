package com.demo_hotel_service.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo_hotel_service.data.models.bookings.GeneralBooking;

public interface GeneralBookingRepository extends JpaRepository<GeneralBooking, Long> {
@Query("""
        SELECT g_b FROM GeneralBooking g_b
        LEFT JOIN FETCH g_b.bookingUnits bu
        LEFT JOIN FETCH bu.serviceUnit
        WHERE g_b.userId = :userId or g_b.phoneNumber = :phoneNumber
    """)
  public List<GeneralBooking> findAllBy(Long userId, String phoneNumber, Pageable pageable);

  @Query("""
        SELECT g_b FROM GeneralBooking g_b
        LEFT JOIN FETCH g_b.bookingUnits bu
        LEFT JOIN FETCH bu.serviceUnit
    """)
  public List<GeneralBooking> findAll2(Pageable pageable);


} 
