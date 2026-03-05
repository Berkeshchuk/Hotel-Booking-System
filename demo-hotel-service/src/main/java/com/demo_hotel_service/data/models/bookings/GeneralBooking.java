package com.demo_hotel_service.data.models.bookings;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.common.enums.BookingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Setter
@Getter
@ToString

@Entity
@Table(name = "general_bookings")
public class GeneralBooking {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = true)
    private Long userId;
    @Column(length = 50, nullable = false)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @OneToMany(mappedBy = "generalBooking", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<BookingUnit> bookingUnits;

    @Column(length = 2000)
    private String clientComment;

    public List<BookingUnit> getBookingUnits(){
        return List.copyOf(bookingUnits);
    }
}

// 1. Знайти всі кімнати де:
// isAvaliable== true

// 2. capacity >= numberOfClients

// 3. кімната не зайнята в цей час
// (no overlapping bookings)

// 4. обрати найменшу можливу кімнату
