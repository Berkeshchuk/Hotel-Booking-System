package com.demo_resource_service.data.dto.dto_mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.demo_resource_service.data.dto.PhysicalSpaRoomDto;
import com.demo_resource_service.data.models.PhysicalSpaRoom;

@Mapper(componentModel = "spring")
public interface PhysicalSpaRoomMapper {

    public PhysicalSpaRoom toEntity(PhysicalSpaRoomDto dto);

    public PhysicalSpaRoomDto toDto(PhysicalSpaRoom entity);

    public List<PhysicalSpaRoom> toListEntities(List<PhysicalSpaRoomDto> dtos);

    public List<PhysicalSpaRoomDto> toListDtos(List<PhysicalSpaRoom> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public PhysicalSpaRoom updateEntity(PhysicalSpaRoomDto dto, @MappingTarget PhysicalSpaRoom entity);

}
