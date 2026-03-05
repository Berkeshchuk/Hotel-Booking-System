package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.RoomUnitDto;
import com.demo_hotel_service.data.dto.ServiceUnitDto;
import com.demo_hotel_service.data.dto.SpaUnitDto;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.data.models.hotel_offerings.rooms.RoomUnit;
import com.demo_hotel_service.data.models.hotel_offerings.spa.SpaUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-05T18:14:23+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ServiceUnitMapperImpl extends ServiceUnitMapper {

    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private RoomUnitMapper roomUnitMapper;
    @Autowired
    private SpaUnitMapper spaUnitMapper;

    @Override
    public ServiceUnitDto toPolymorphicDto(ServiceUnit serviceUnit) {
        if ( serviceUnit == null ) {
            return null;
        }

        if (serviceUnit instanceof RoomUnit) {
            return roomUnitMapper.toDto( (RoomUnit) serviceUnit );
        }
        else if (serviceUnit instanceof SpaUnit) {
            return spaUnitMapper.toDto( (SpaUnit) serviceUnit );
        }
        else {
            ServiceUnitDto serviceUnitDto = new ServiceUnitDto();

            serviceUnitDto.setDescription( serviceUnit.getDescription() );
            Set<String> set = serviceUnit.getFacilities();
            if ( set != null ) {
                serviceUnitDto.setFacilities( new LinkedHashSet<String>( set ) );
            }
            serviceUnitDto.setGuestCapacity( serviceUnit.getGuestCapacity() );
            serviceUnitDto.setHiddenFromClient( serviceUnit.isHiddenFromClient() );
            serviceUnitDto.setId( serviceUnit.getId() );
            serviceUnitDto.setImageRecords( imageMapper.toListDtos( serviceUnit.getImageRecords() ) );
            serviceUnitDto.setOutOfService( serviceUnit.isOutOfService() );
            serviceUnitDto.setPrice( serviceUnit.getPrice() );
            serviceUnitDto.setType( serviceUnit.getType() );

            return serviceUnitDto;
        }
    }

    @Override
    public ServiceUnit toPolymorphicEntity(ServiceUnitDto serviceUnit) {
        if ( serviceUnit == null ) {
            return null;
        }

        if (serviceUnit instanceof RoomUnitDto) {
            return roomUnitMapper.toEntity( (RoomUnitDto) serviceUnit );
        }
        else if (serviceUnit instanceof SpaUnitDto) {
            return spaUnitMapper.toEntity( (SpaUnitDto) serviceUnit );
        }
        else {
            ServiceUnit serviceUnit1 = new ServiceUnit();

            serviceUnit1.setDescription( serviceUnit.getDescription() );
            Set<String> set = serviceUnit.getFacilities();
            if ( set != null ) {
                serviceUnit1.setFacilities( new LinkedHashSet<String>( set ) );
            }
            if ( serviceUnit.getGuestCapacity() != null ) {
                serviceUnit1.setGuestCapacity( serviceUnit.getGuestCapacity() );
            }
            if ( serviceUnit.getHiddenFromClient() != null ) {
                serviceUnit1.setHiddenFromClient( serviceUnit.getHiddenFromClient() );
            }
            serviceUnit1.setImageRecords( imageMapper.toListEntities( serviceUnit.getImageRecords() ) );
            if ( serviceUnit.getOutOfService() != null ) {
                serviceUnit1.setOutOfService( serviceUnit.getOutOfService() );
            }
            serviceUnit1.setPrice( serviceUnit.getPrice() );
            serviceUnit1.setType( serviceUnit.getType() );

            return serviceUnit1;
        }
    }

    @Override
    public List<ServiceUnitDto> toPolymorphicDtos(List<ServiceUnit> units) {
        if ( units == null ) {
            return null;
        }

        List<ServiceUnitDto> list = new ArrayList<ServiceUnitDto>( units.size() );
        for ( ServiceUnit serviceUnit : units ) {
            list.add( toPolymorphicDto( serviceUnit ) );
        }

        return list;
    }

    @Override
    public List<ServiceUnit> toPolymorphicEntities(List<ServiceUnitDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ServiceUnit> list = new ArrayList<ServiceUnit>( dtos.size() );
        for ( ServiceUnitDto serviceUnitDto : dtos ) {
            list.add( toPolymorphicEntity( serviceUnitDto ) );
        }

        return list;
    }
}
