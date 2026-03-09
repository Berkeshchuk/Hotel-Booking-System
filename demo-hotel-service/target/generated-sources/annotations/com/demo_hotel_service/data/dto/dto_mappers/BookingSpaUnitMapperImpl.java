package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.BookingSpaUnitDto;
import com.demo_hotel_service.data.dto.BookingUnitDto;
import com.demo_hotel_service.data.models.bookings.BookingSpaUnit;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-09T13:29:11+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class BookingSpaUnitMapperImpl implements BookingSpaUnitMapper {

    @Autowired
    private ServiceUnitMapper serviceUnitMapper;

    @Override
    public BookingSpaUnit updateEntity(BookingUnitDto dto, BookingSpaUnit entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getClientCount() != null ) {
            entity.setClientCount( dto.getClientCount() );
        }
        entity.setEnd( dto.getEnd() );
        entity.setOrderDateTime( dto.getOrderDateTime() );
        entity.setServiceUnit( serviceUnitMapper.toPolymorphicEntity( dto.getServiceUnit() ) );
        entity.setServiceUnitId( dto.getServiceUnitId() );
        entity.setStart( dto.getStart() );
        entity.setStatus( dto.getStatus() );

        return entity;
    }

    @Override
    public BookingSpaUnit toEntity(BookingSpaUnitDto dto) {
        if ( dto == null ) {
            return null;
        }

        BookingSpaUnit bookingSpaUnit = new BookingSpaUnit();

        if ( dto.getClientCount() != null ) {
            bookingSpaUnit.setClientCount( dto.getClientCount() );
        }
        bookingSpaUnit.setEnd( dto.getEnd() );
        bookingSpaUnit.setOrderDateTime( dto.getOrderDateTime() );
        bookingSpaUnit.setServiceUnitId( dto.getServiceUnitId() );
        bookingSpaUnit.setStart( dto.getStart() );
        bookingSpaUnit.setStatus( dto.getStatus() );
        bookingSpaUnit.setPreferedGender( dto.getPreferedGender() );

        return bookingSpaUnit;
    }

    @Override
    public BookingSpaUnitDto toDto(BookingSpaUnit entity) {
        if ( entity == null ) {
            return null;
        }

        BookingSpaUnitDto bookingSpaUnitDto = new BookingSpaUnitDto();

        bookingSpaUnitDto.setServiceUnitId( entityServiceUnitId( entity ) );
        bookingSpaUnitDto.setClientCount( entity.getClientCount() );
        bookingSpaUnitDto.setEnd( entity.getEnd() );
        bookingSpaUnitDto.setId( entity.getId() );
        bookingSpaUnitDto.setOrderDateTime( entity.getOrderDateTime() );
        bookingSpaUnitDto.setServiceUnit( serviceUnitMapper.toPolymorphicDto( entity.getServiceUnit() ) );
        bookingSpaUnitDto.setStart( entity.getStart() );
        bookingSpaUnitDto.setStatus( entity.getStatus() );
        bookingSpaUnitDto.setPreferedGender( entity.getPreferedGender() );

        return bookingSpaUnitDto;
    }

    @Override
    public BookingSpaUnit updateEntity(BookingSpaUnitDto dto, BookingSpaUnit entity) {
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
        if ( dto.getServiceUnitId() != null ) {
            entity.setServiceUnitId( dto.getServiceUnitId() );
        }
        if ( dto.getStart() != null ) {
            entity.setStart( dto.getStart() );
        }
        if ( dto.getStatus() != null ) {
            entity.setStatus( dto.getStatus() );
        }
        if ( dto.getPreferedGender() != null ) {
            entity.setPreferedGender( dto.getPreferedGender() );
        }

        return entity;
    }

    private Long entityServiceUnitId(BookingSpaUnit bookingSpaUnit) {
        if ( bookingSpaUnit == null ) {
            return null;
        }
        ServiceUnit serviceUnit = bookingSpaUnit.getServiceUnit();
        if ( serviceUnit == null ) {
            return null;
        }
        long id = serviceUnit.getId();
        return id;
    }
}
