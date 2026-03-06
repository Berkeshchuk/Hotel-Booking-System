package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.BookingRoomUnitDto;
import com.demo_hotel_service.data.models.bookings.BookingRoomUnit;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-06T11:59:58+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class BookingRoomUnitMapperImpl implements BookingRoomUnitMapper {

    @Autowired
    private ServiceUnitMapper serviceUnitMapper;

    @Override
    public BookingRoomUnit toEntity(BookingRoomUnitDto dto) {
        if ( dto == null ) {
            return null;
        }

        BookingRoomUnit bookingRoomUnit = new BookingRoomUnit();

        if ( dto.getClientCount() != null ) {
            bookingRoomUnit.setClientCount( dto.getClientCount() );
        }
        bookingRoomUnit.setEnd( dto.getEnd() );
        bookingRoomUnit.setOrderDateTime( dto.getOrderDateTime() );
        bookingRoomUnit.setServiceUnit( serviceUnitMapper.toPolymorphicEntity( dto.getServiceUnit() ) );
        bookingRoomUnit.setServiceUnitId( dto.getServiceUnitId() );
        bookingRoomUnit.setStart( dto.getStart() );
        bookingRoomUnit.setStatus( dto.getStatus() );

        return bookingRoomUnit;
    }

    @Override
    public BookingRoomUnitDto toDto(BookingRoomUnit entity) {
        if ( entity == null ) {
            return null;
        }

        BookingRoomUnitDto bookingRoomUnitDto = new BookingRoomUnitDto();

        bookingRoomUnitDto.setServiceUnitId( entityServiceUnitId( entity ) );
        bookingRoomUnitDto.setClientCount( entity.getClientCount() );
        bookingRoomUnitDto.setEnd( entity.getEnd() );
        bookingRoomUnitDto.setId( entity.getId() );
        bookingRoomUnitDto.setOrderDateTime( entity.getOrderDateTime() );
        bookingRoomUnitDto.setServiceUnit( serviceUnitMapper.toPolymorphicDto( entity.getServiceUnit() ) );
        bookingRoomUnitDto.setStart( entity.getStart() );
        bookingRoomUnitDto.setStatus( entity.getStatus() );

        return bookingRoomUnitDto;
    }

    @Override
    public BookingRoomUnit updateEntity(BookingRoomUnitDto dto, BookingRoomUnit entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getClientCount() != null ) {
            entity.setClientCount( dto.getClientCount() );
        }
        if ( dto.getEnd() != null ) {
            entity.setEnd( dto.getEnd() );
        }
        if ( dto.getOrderDateTime() != null ) {
            entity.setOrderDateTime( dto.getOrderDateTime() );
        }
        if ( dto.getServiceUnit() != null ) {
            entity.setServiceUnit( serviceUnitMapper.toPolymorphicEntity( dto.getServiceUnit() ) );
        }
        if ( dto.getServiceUnitId() != null ) {
            entity.setServiceUnitId( dto.getServiceUnitId() );
        }
        if ( dto.getStart() != null ) {
            entity.setStart( dto.getStart() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }

        return entity;
    }

    private Long entityServiceUnitId(BookingRoomUnit bookingRoomUnit) {
        if ( bookingRoomUnit == null ) {
            return null;
        }
        ServiceUnit serviceUnit = bookingRoomUnit.getServiceUnit();
        if ( serviceUnit == null ) {
            return null;
        }
        long id = serviceUnit.getId();
        return id;
    }
}
