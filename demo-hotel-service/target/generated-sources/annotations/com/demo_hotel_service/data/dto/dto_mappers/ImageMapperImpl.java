package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.ImageRecordDto;
import com.demo_hotel_service.data.models.images.ImageRecord;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-17T11:04:22+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImageRecord toEntity(ImageRecordDto dto) {
        if ( dto == null ) {
            return null;
        }

        ImageRecord imageRecord = new ImageRecord();

        if ( dto.getPosition() != null ) {
            imageRecord.setPosition( dto.getPosition() );
        }
        imageRecord.setUrl( dto.getUrl() );

        return imageRecord;
    }

    @Override
    public ImageRecordDto toDto(ImageRecord entity) {
        if ( entity == null ) {
            return null;
        }

        ImageRecordDto imageRecordDto = new ImageRecordDto();

        imageRecordDto.setId( entity.getId() );
        imageRecordDto.setPosition( entity.getPosition() );
        imageRecordDto.setUrl( entity.getUrl() );

        return imageRecordDto;
    }

    @Override
    public List<ImageRecordDto> toListDtos(List<ImageRecord> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<ImageRecordDto> list = new ArrayList<ImageRecordDto>( dtos.size() );
        for ( ImageRecord imageRecord : dtos ) {
            list.add( toDto( imageRecord ) );
        }

        return list;
    }

    @Override
    public List<ImageRecord> toListEntities(List<ImageRecordDto> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ImageRecord> list = new ArrayList<ImageRecord>( entities.size() );
        for ( ImageRecordDto imageRecordDto : entities ) {
            list.add( toEntity( imageRecordDto ) );
        }

        return list;
    }

    @Override
    public ImageRecord updateEntity(ImageRecordDto dto, ImageRecord entity) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getPosition() != null ) {
            entity.setPosition( dto.getPosition() );
        }
        if ( dto.getUrl() != null ) {
            entity.setUrl( dto.getUrl() );
        }

        return entity;
    }
}
