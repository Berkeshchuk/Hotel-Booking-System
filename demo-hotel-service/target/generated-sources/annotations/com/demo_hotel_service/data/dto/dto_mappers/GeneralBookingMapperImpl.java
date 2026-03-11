package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.GeneralBookingDto;
import com.demo_hotel_service.data.models.bookings.GeneralBooking;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T17:15:33+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class GeneralBookingMapperImpl implements GeneralBookingMapper {

    @Autowired
    private BookingUnitMapper bookingUnitMapper;

    @Override
    public GeneralBooking toEntity(GeneralBookingDto dto) {
        if ( dto == null ) {
            return null;
        }

        GeneralBooking generalBooking = new GeneralBooking();

        generalBooking.setBookingUnits( bookingUnitMapper.toPolymorphicEntities( dto.getBookingUnits() ) );
        generalBooking.setClientComment( dto.getClientComment() );
        generalBooking.setPhoneNumber( dto.getPhoneNumber() );
        generalBooking.setStatus( dto.getStatus() );
        generalBooking.setUserId( dto.getUserId() );

        return generalBooking;
    }

    @Override
    public GeneralBookingDto toDto(GeneralBooking entity) {
        if ( entity == null ) {
            return null;
        }

        GeneralBookingDto generalBookingDto = new GeneralBookingDto();

        generalBookingDto.setBookingUnits( bookingUnitMapper.toPolymorphicDtos( entity.getBookingUnits() ) );
        generalBookingDto.setClientComment( entity.getClientComment() );
        generalBookingDto.setId( entity.getId() );
        generalBookingDto.setOrderDateTime( entity.getOrderDateTime() );
        generalBookingDto.setPhoneNumber( entity.getPhoneNumber() );
        generalBookingDto.setStatus( entity.getStatus() );
        generalBookingDto.setUserId( entity.getUserId() );

        return generalBookingDto;
    }

    @Override
    public List<GeneralBookingDto> toDtos(Page<GeneralBooking> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GeneralBookingDto> list = new ArrayList<GeneralBookingDto>();
        for ( GeneralBooking generalBooking : entities ) {
            list.add( toDto( generalBooking ) );
        }

        return list;
    }

    @Override
    public List<GeneralBookingDto> toDtos(List<GeneralBooking> entities) {
        if ( entities == null ) {
            return null;
        }

        List<GeneralBookingDto> list = new ArrayList<GeneralBookingDto>( entities.size() );
        for ( GeneralBooking generalBooking : entities ) {
            list.add( toDto( generalBooking ) );
        }

        return list;
    }

    @Override
    public GeneralBooking updateEntity(GeneralBookingDto dto, GeneralBooking entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getClientComment() != null ) {
            entity.setClientComment( dto.getClientComment() );
        }
        if ( dto.getPhoneNumber() != null ) {
            entity.setPhoneNumber( dto.getPhoneNumber() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }

        return entity;
    }
}
