package com.demo_hotel_service.data.models.bookings;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.common.enums.BookingStatus;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "booking_units")
@Inheritance(strategy = InheritanceType.JOINED)
public class BookingUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private GeneralBooking generalBooking;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ServiceUnit serviceUnit;
    @Transient
    private Long serviceUnitId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    @Column(nullable = false)
    private int clientCount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private LocalDateTime start;
    @Column(nullable = false)
    private LocalDateTime end;

    @Version
    Integer version;

    public BookingUnit(long id, GeneralBooking generalBooking, ServiceUnit serviceUnit,
            int clientCount, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.generalBooking = generalBooking;
        this.serviceUnit = serviceUnit;
        this.clientCount = clientCount;
        this.start = start;
        this.end = end;
    }
}
