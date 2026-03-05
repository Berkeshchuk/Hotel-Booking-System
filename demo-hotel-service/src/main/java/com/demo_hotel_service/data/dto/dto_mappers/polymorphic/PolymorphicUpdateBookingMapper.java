package com.demo_hotel_service.data.dto.dto_mappers.polymorphic;

import org.mapstruct.MappingTarget;

import com.demo_hotel_service.data.dto.BookingUnitDto;
import com.demo_hotel_service.data.models.bookings.BookingUnit;

public interface PolymorphicUpdateBookingMapper<E extends BookingUnit, D extends BookingUnitDto> {
    public E updateEntity(D dto, @MappingTarget E entity);
    public Class<?> getEntityClass();
}
