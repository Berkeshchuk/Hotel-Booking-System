package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.RoomUnitDto;
import com.demo_hotel_service.data.models.hotel_offerings.rooms.RoomUnit;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-17T11:04:22+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class RoomUnitMapperImpl implements RoomUnitMapper {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public RoomUnit toEntity(RoomUnitDto dto) {
        if ( dto == null ) {
            return null;
        }

        RoomUnit roomUnit = new RoomUnit();

        roomUnit.setDescription( dto.getDescription() );
        Set<String> set = dto.getFacilities();
        if ( set != null ) {
            roomUnit.setFacilities( new LinkedHashSet<String>( set ) );
        }
        if ( dto.getGuestCapacity() != null ) {
            roomUnit.setGuestCapacity( dto.getGuestCapacity() );
        }
        if ( dto.getHiddenFromClient() != null ) {
            roomUnit.setHiddenFromClient( dto.getHiddenFromClient() );
        }
        roomUnit.setImageRecords( imageMapper.toListEntities( dto.getImageRecords() ) );
        if ( dto.getOutOfService() != null ) {
            roomUnit.setOutOfService( dto.getOutOfService() );
        }
        roomUnit.setPrice( dto.getPrice() );
        roomUnit.setType( dto.getType() );

        return roomUnit;
    }

    @Override
    public RoomUnitDto toDto(RoomUnit entity) {
        if ( entity == null ) {
            return null;
        }

        RoomUnitDto roomUnitDto = new RoomUnitDto();

        roomUnitDto.setDescription( entity.getDescription() );
        Set<String> set = entity.getFacilities();
        if ( set != null ) {
            roomUnitDto.setFacilities( new LinkedHashSet<String>( set ) );
        }
        roomUnitDto.setGuestCapacity( entity.getGuestCapacity() );
        roomUnitDto.setHiddenFromClient( entity.isHiddenFromClient() );
        roomUnitDto.setId( entity.getId() );
        roomUnitDto.setImageRecords( imageMapper.toListDtos( entity.getImageRecords() ) );
        roomUnitDto.setOutOfService( entity.isOutOfService() );
        roomUnitDto.setPrice( entity.getPrice() );
        roomUnitDto.setType( entity.getType() );

        return roomUnitDto;
    }

    @Override
    public RoomUnit updateEntity(RoomUnitDto dto, RoomUnit entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getDescription() != null ) {
            entity.setDescription( dto.getDescription() );
        }
        if ( entity.getFacilities() != null ) {
            Set<String> set = dto.getFacilities();
            if ( set != null ) {
                entity.getFacilities().clear();
                entity.getFacilities().addAll( set );
            }
        }
        else {
            Set<String> set = dto.getFacilities();
            if ( set != null ) {
                entity.setFacilities( new LinkedHashSet<String>( set ) );
            }
        }
        if ( dto.getGuestCapacity() != null ) {
            entity.setGuestCapacity( dto.getGuestCapacity() );
        }
        if ( dto.getHiddenFromClient() != null ) {
            entity.setHiddenFromClient( dto.getHiddenFromClient() );
        }
        if ( dto.getOutOfService() != null ) {
            entity.setOutOfService( dto.getOutOfService() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }
        if ( dto.getType() != null ) {
            entity.setType( dto.getType() );
        }

        return entity;
    }
}
