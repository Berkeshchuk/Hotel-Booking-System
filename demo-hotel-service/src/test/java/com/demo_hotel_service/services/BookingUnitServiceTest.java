package com.demo_hotel_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo_hotel_service.data.dto.BookingUnitDto;
import com.demo_hotel_service.data.dto.dto_mappers.BookingUnitMapper;
import com.demo_hotel_service.data.models.bookings.BookingUnit;
import com.demo_hotel_service.data.models.bookings.GeneralBooking;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.repositories.BookingUnitRepository;
import com.demo_hotel_service.repositories.GeneralBookingRepository;

import jakarta.persistence.EntityManager;

public class BookingUnitServiceTest {

    @Mock
    private BookingUnitRepository bookingRepository;

    @Mock
    private BookingUnitMapper bookingMapper;

    @Mock
    private GeneralBookingRepository gBookingRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BookingUnitService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBookingUnits() {

        Pageable pageable = PageRequest.of(0, 10);

        List<BookingUnit> entities = List.of(new BookingUnit());
        List<BookingUnitDto> dtos = List.of(new BookingUnitDto());

        when(bookingRepository.findAllByGeneralBookingId(1L, pageable))
                .thenReturn(entities);

        when(bookingMapper.toPolymorphicDtos(entities))
                .thenReturn(dtos);

        List<BookingUnitDto> result = service.getBookingUnits(1L, pageable);

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnBookingUnitById() {

        BookingUnit entity = new BookingUnit();
        BookingUnitDto dto = new BookingUnitDto();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(bookingMapper.toPolymorphicDto(entity))
                .thenReturn(dto);

        BookingUnitDto result = service.getBookingUnitById(1L);

        assertNotNull(result);
    }

    @Test
    void shouldThrowIfBookingUnitNotFound() {

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.getBookingUnitById(1L));
    }

    @Test
    void shouldAddBookingUnits() {

        BookingUnitDto dto = new BookingUnitDto();
        dto.setServiceUnitId(1L);

        when(gBookingRepository.existsById(1L))
                .thenReturn(true);

        when(entityManager.getReference(ServiceUnit.class, 1L))
                .thenReturn(new ServiceUnit());

        when(entityManager.getReference(GeneralBooking.class, 1L))
                .thenReturn(new GeneralBooking());

        when(bookingMapper.toPolymorphicEntities(any()))
                .thenReturn(List.of(new BookingUnit()));

        List<BookingUnitDto> result = service.addBookingUnits(1L, List.of(dto));

        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowIfGeneralBookingNotExists() {

        when(gBookingRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.addBookingUnits(1L, List.of()));
    }

    @Test
    void shouldSaveBookingUnits() {

        BookingUnitDto dto = new BookingUnitDto();
        dto.setServiceUnitId(1L);

        when(gBookingRepository.existsById(1L))
                .thenReturn(true);

        when(entityManager.getReference(ServiceUnit.class, 1L))
                .thenReturn(new ServiceUnit());

        when(entityManager.getReference(GeneralBooking.class, 1L))
                .thenReturn(new GeneralBooking());

        when(bookingMapper.toPolymorphicEntities(any()))
                .thenReturn(List.of(new BookingUnit()));

        service.addBookingUnits(1L, List.of(dto));

        verify(bookingRepository).saveAll(any());
    }

    @Test
    void shouldUpdateBookingUnit() {

        BookingUnitDto dto = new BookingUnitDto();
        dto.setId(1L);

        BookingUnit existing = new BookingUnit();

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        service.updateBookingUnit(dto);

        verify(bookingRepository).save(any());
    }
}
