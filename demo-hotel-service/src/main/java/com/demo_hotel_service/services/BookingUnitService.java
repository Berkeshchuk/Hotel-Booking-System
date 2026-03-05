package com.demo_hotel_service.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo_hotel_service.data.dto.BookingUnitDto;
import com.demo_hotel_service.data.dto.dto_mappers.BookingUnitMapper;
import com.demo_hotel_service.data.models.bookings.BookingUnit;
import com.demo_hotel_service.data.models.bookings.GeneralBooking;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.repositories.BookingUnitRepository;
import com.demo_hotel_service.repositories.GeneralBookingRepository;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingUnitService {
    private final BookingUnitRepository bookingRepository;
    private final GeneralBookingRepository gBookingRepository;
    private final BookingUnitMapper bookingMapper;
    private final EntityManager entityManager;

    //потрібно додати перевірку що generalBookingId належить відповідному користувачу або користувач адмін
    public List<BookingUnitDto> getBookingUnits(Long generalBookingId, Pageable pageable){
        if(generalBookingId == null || pageable == null){
            throw new IllegalArgumentException("cant be null");
        }
        List<BookingUnit> bookingUnits = bookingRepository.findAllByGeneralBookingId(generalBookingId, pageable);

        return bookingMapper.toPolymorphicDtos(bookingUnits);
    }

    //потрібно додати перевірку що BookingUnit належить відповідному користувачу або користувач адмін
    public BookingUnitDto getBookingUnitById(Long id){
        BookingUnit bookingUnit = bookingRepository.findById(id).orElseThrow();
        return bookingMapper.toPolymorphicDto(bookingUnit);
    }

    //потрібно додати перевірку що generalBookingId належить відповідному користувачу або користувач адмін
    public List<BookingUnitDto> addBookingUnits(Long generalBookingId, List<BookingUnitDto> dtos){
        if(generalBookingId == null){
            throw new IllegalArgumentException("cant be null");
        }

        if( ! gBookingRepository.existsById(generalBookingId)){
            throw new IllegalArgumentException("general booking with id do not exists");
        }

        Map<Long, ServiceUnit> proxyMap = dtos.stream()
                .map(b -> b.getServiceUnitId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> entityManager.getReference(ServiceUnit.class, id)));

        var generalBookingRef = entityManager.getReference(GeneralBooking.class, generalBookingId);

        List<BookingUnit> entities = bookingMapper.toPolymorphicEntities(dtos);
        entities.forEach(e -> {
            e.setGeneralBooking(generalBookingRef);
            e.setServiceUnit(proxyMap.get(e.getServiceUnitId()));
        });

        bookingRepository.saveAll(entities);

        return dtos;
    }



    //потрібно додати перевірку що BookingUnitDto належить відповідному користувачу або користувач адмін
    public void updateBookingUnit(BookingUnitDto dto){
        var existing = bookingRepository.findById(dto.getId()).orElseThrow();
        var updated = bookingMapper.updateEntity(dto, existing);
        bookingRepository.save(updated);
    }



}
