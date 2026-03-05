package com.demo_hotel_service.data.dto.dto_mappers.polymorphic;

import org.mapstruct.MappingTarget;

import com.demo_hotel_service.data.dto.ServiceUnitDto;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;

public interface PolymorphicUpdateServiceUnitMapper<E extends ServiceUnit, D extends ServiceUnitDto> {
    public E updateEntity(D dto, @MappingTarget E entity);

    public Class<?> getEntityClass();
}
