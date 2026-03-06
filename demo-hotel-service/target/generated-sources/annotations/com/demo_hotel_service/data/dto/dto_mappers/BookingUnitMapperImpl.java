package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.BookingRoomUnitDto;
import com.demo_hotel_service.data.dto.BookingSpaUnitDto;
import com.demo_hotel_service.data.dto.BookingUnitDto;
import com.demo_hotel_service.data.models.bookings.BookingRoomUnit;
import com.demo_hotel_service.data.models.bookings.BookingSpaUnit;
import com.demo_hotel_service.data.models.bookings.BookingUnit;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-06T11:59:58+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class BookingUnitMapperImpl extends BookingUnitMapper {

    @Autowired
    private BookingRoomUnitMapper bookingRoomUnitMapper;
    @Autowired
    private BookingSpaUnitMapper bookingSpaUnitMapper;
    @Autowired
    private ServiceUnitMapper serviceUnitMapper;

    @Override
    public BookingUnitDto toPolymorphicDto(BookingUnit bookingUnit) {
        if ( bookingUnit == null ) {
            return null;
        }

        if (bookingUnit instanceof BookingRoomUnit) {
            return bookingRoomUnitMapper.toDto( (BookingRoomUnit) bookingUnit );
        }
        else if (bookingUnit instanceof BookingSpaUnit) {
            return bookingSpaUnitMapper.toDto( (BookingSpaUnit) bookingUnit );
        }
        else {
            BookingUnitDto bookingUnitDto = new BookingUnitDto();

            bookingUnitDto.setServiceUnitId( bookingUnitServiceUnitId( bookingUnit ) );
            bookingUnitDto.setClientCount( bookingUnit.getClientCount() );
            bookingUnitDto.setEnd( bookingUnit.getEnd() );
            bookingUnitDto.setId( bookingUnit.getId() );
            bookingUnitDto.setOrderDateTime( bookingUnit.getOrderDateTime() );
            bookingUnitDto.setServiceUnit( serviceUnitMapper.toPolymorphicDto( bookingUnit.getServiceUnit() ) );
            bookingUnitDto.setStart( bookingUnit.getStart() );
            bookingUnitDto.setStatus( bookingUnit.getStatus() );

            return bookingUnitDto;
        }
    }

    @Override
    public BookingUnit toPolymorphicEntity(BookingUnitDto serviceUnit) {
        if ( serviceUnit == null ) {
            return null;
        }

        if (serviceUnit instanceof BookingRoomUnitDto) {
            return bookingRoomUnitMapper.toEntity( (BookingRoomUnitDto) serviceUnit );
        }
        else if (serviceUnit instanceof BookingSpaUnitDto) {
            return bookingSpaUnitMapper.toEntity( (BookingSpaUnitDto) serviceUnit );
        }
        else {
            BookingUnit bookingUnit = new BookingUnit();

            if ( serviceUnit.getClientCount() != null ) {
                bookingUnit.setClientCount( serviceUnit.getClientCount() );
            }
            bookingUnit.setEnd( serviceUnit.getEnd() );
            bookingUnit.setServiceUnitId( serviceUnit.getServiceUnitId() );
            bookingUnit.setStart( serviceUnit.getStart() );
            bookingUnit.setStatus( serviceUnit.getStatus() );

            return bookingUnit;
        }
    }

    @Override
    public List<BookingUnitDto> toPolymorphicDtos(List<BookingUnit> units) {
        if ( units == null ) {
            return null;
        }

        List<BookingUnitDto> list = new ArrayList<BookingUnitDto>( units.size() );
        for ( BookingUnit bookingUnit : units ) {
            list.add( toPolymorphicDto( bookingUnit ) );
        }

        return list;
    }

    @Override
    public List<BookingUnit> toPolymorphicEntities(List<BookingUnitDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<BookingUnit> list = new ArrayList<BookingUnit>( dtos.size() );
        for ( BookingUnitDto bookingUnitDto : dtos ) {
            list.add( toPolymorphicEntity( bookingUnitDto ) );
        }

        return list;
    }

    private Long bookingUnitServiceUnitId(BookingUnit bookingUnit) {
        if ( bookingUnit == null ) {
            return null;
        }
        ServiceUnit serviceUnit = bookingUnit.getServiceUnit();
        if ( serviceUnit == null ) {
            return null;
        }
        long id = serviceUnit.getId();
        return id;
    }
}
