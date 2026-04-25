package com.demo_hotel_service.data.dto.dto_mappers;

import com.demo_hotel_service.data.dto.SpaUnitDto;
import com.demo_hotel_service.data.dto.StringPairDto;
import com.demo_hotel_service.data.models.hotel_offerings.spa.SpaUnit;
import com.demo_hotel_service.data.models.hotel_offerings.spa.StringPair;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-21T13:03:43+0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class SpaUnitMapperImpl implements SpaUnitMapper {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public SpaUnit toEntity(SpaUnitDto dto) {
        if ( dto == null ) {
            return null;
        }

        SpaUnit spaUnit = new SpaUnit();

        spaUnit.setDescription( dto.getDescription() );
        Set<String> set = dto.getFacilities();
        if ( set != null ) {
            spaUnit.setFacilities( new LinkedHashSet<String>( set ) );
        }
        if ( dto.getGuestCapacity() != null ) {
            spaUnit.setGuestCapacity( dto.getGuestCapacity() );
        }
        if ( dto.getHiddenFromClient() != null ) {
            spaUnit.setHiddenFromClient( dto.getHiddenFromClient() );
        }
        spaUnit.setImageRecords( imageMapper.toListEntities( dto.getImageRecords() ) );
        if ( dto.getOutOfService() != null ) {
            spaUnit.setOutOfService( dto.getOutOfService() );
        }
        spaUnit.setPrice( dto.getPrice() );
        spaUnit.setType( dto.getType() );
        spaUnit.setCareProductsDescriptions( stringPairDtoListToStringPairList( dto.getCareProductsDescriptions() ) );
        List<String> list2 = dto.getCautionNotes();
        if ( list2 != null ) {
            spaUnit.setCautionNotes( new ArrayList<String>( list2 ) );
        }
        List<String> list3 = dto.getContraindications();
        if ( list3 != null ) {
            spaUnit.setContraindications( new ArrayList<String>( list3 ) );
        }
        if ( dto.getDurationInMinutes() != null ) {
            spaUnit.setDurationInMinutes( dto.getDurationInMinutes() );
        }
        spaUnit.setName( dto.getName() );
        spaUnit.setPreparingInfoForClient( dto.getPreparingInfoForClient() );
        spaUnit.setSpaStagesDescriptions( stringPairDtoListToStringPairList( dto.getSpaStagesDescriptions() ) );

        return spaUnit;
    }

    @Override
    public SpaUnitDto toDto(SpaUnit entity) {
        if ( entity == null ) {
            return null;
        }

        SpaUnitDto spaUnitDto = new SpaUnitDto();

        spaUnitDto.setDescription( entity.getDescription() );
        Set<String> set = entity.getFacilities();
        if ( set != null ) {
            spaUnitDto.setFacilities( new LinkedHashSet<String>( set ) );
        }
        spaUnitDto.setGuestCapacity( entity.getGuestCapacity() );
        spaUnitDto.setHiddenFromClient( entity.isHiddenFromClient() );
        spaUnitDto.setId( entity.getId() );
        spaUnitDto.setImageRecords( imageMapper.toListDtos( entity.getImageRecords() ) );
        spaUnitDto.setOutOfService( entity.isOutOfService() );
        spaUnitDto.setPrice( entity.getPrice() );
        spaUnitDto.setType( entity.getType() );
        spaUnitDto.setCareProductsDescriptions( stringPairListToStringPairDtoList( entity.getCareProductsDescriptions() ) );
        List<String> list2 = entity.getCautionNotes();
        if ( list2 != null ) {
            spaUnitDto.setCautionNotes( new ArrayList<String>( list2 ) );
        }
        List<String> list3 = entity.getContraindications();
        if ( list3 != null ) {
            spaUnitDto.setContraindications( new ArrayList<String>( list3 ) );
        }
        spaUnitDto.setDurationInMinutes( entity.getDurationInMinutes() );
        spaUnitDto.setName( entity.getName() );
        spaUnitDto.setPreparingInfoForClient( entity.getPreparingInfoForClient() );
        spaUnitDto.setSpaStagesDescriptions( stringPairListToStringPairDtoList( entity.getSpaStagesDescriptions() ) );

        return spaUnitDto;
    }

    @Override
    public SpaUnit updateEntity(SpaUnitDto dto, SpaUnit entity) {
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
        if ( entity.getCareProductsDescriptions() != null ) {
            List<StringPair> list = stringPairDtoListToStringPairList( dto.getCareProductsDescriptions() );
            if ( list != null ) {
                entity.getCareProductsDescriptions().clear();
                entity.getCareProductsDescriptions().addAll( list );
            }
        }
        else {
            List<StringPair> list = stringPairDtoListToStringPairList( dto.getCareProductsDescriptions() );
            if ( list != null ) {
                entity.setCareProductsDescriptions( list );
            }
        }
        if ( entity.getCautionNotes() != null ) {
            List<String> list1 = dto.getCautionNotes();
            if ( list1 != null ) {
                entity.getCautionNotes().clear();
                entity.getCautionNotes().addAll( list1 );
            }
        }
        else {
            List<String> list1 = dto.getCautionNotes();
            if ( list1 != null ) {
                entity.setCautionNotes( new ArrayList<String>( list1 ) );
            }
        }
        if ( entity.getContraindications() != null ) {
            List<String> list2 = dto.getContraindications();
            if ( list2 != null ) {
                entity.getContraindications().clear();
                entity.getContraindications().addAll( list2 );
            }
        }
        else {
            List<String> list2 = dto.getContraindications();
            if ( list2 != null ) {
                entity.setContraindications( new ArrayList<String>( list2 ) );
            }
        }
        if ( dto.getDurationInMinutes() != null ) {
            entity.setDurationInMinutes( dto.getDurationInMinutes() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getPreparingInfoForClient() != null ) {
            entity.setPreparingInfoForClient( dto.getPreparingInfoForClient() );
        }
        if ( entity.getSpaStagesDescriptions() != null ) {
            List<StringPair> list3 = stringPairDtoListToStringPairList( dto.getSpaStagesDescriptions() );
            if ( list3 != null ) {
                entity.getSpaStagesDescriptions().clear();
                entity.getSpaStagesDescriptions().addAll( list3 );
            }
        }
        else {
            List<StringPair> list3 = stringPairDtoListToStringPairList( dto.getSpaStagesDescriptions() );
            if ( list3 != null ) {
                entity.setSpaStagesDescriptions( list3 );
            }
        }

        return entity;
    }

    protected StringPair stringPairDtoToStringPair(StringPairDto stringPairDto) {
        if ( stringPairDto == null ) {
            return null;
        }

        StringPair stringPair = new StringPair();

        stringPair.setDescription( stringPairDto.getDescription() );
        stringPair.setName( stringPairDto.getName() );

        return stringPair;
    }

    protected List<StringPair> stringPairDtoListToStringPairList(List<StringPairDto> list) {
        if ( list == null ) {
            return null;
        }

        List<StringPair> list1 = new ArrayList<StringPair>( list.size() );
        for ( StringPairDto stringPairDto : list ) {
            list1.add( stringPairDtoToStringPair( stringPairDto ) );
        }

        return list1;
    }

    protected StringPairDto stringPairToStringPairDto(StringPair stringPair) {
        if ( stringPair == null ) {
            return null;
        }

        StringPairDto stringPairDto = new StringPairDto();

        stringPairDto.setDescription( stringPair.getDescription() );
        stringPairDto.setName( stringPair.getName() );

        return stringPairDto;
    }

    protected List<StringPairDto> stringPairListToStringPairDtoList(List<StringPair> list) {
        if ( list == null ) {
            return null;
        }

        List<StringPairDto> list1 = new ArrayList<StringPairDto>( list.size() );
        for ( StringPair stringPair : list ) {
            list1.add( stringPairToStringPairDto( stringPair ) );
        }

        return list1;
    }
}
