package com.demo_resource_service.data.dto.dto_mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.demo_resource_service.data.dto.PhysicalRoomDto;
import com.demo_resource_service.data.models.PhysicalRoom;

@Mapper(componentModel = "spring")
public interface PhysicalRoomMapper {
    public PhysicalRoom toEntity(PhysicalRoomDto dto);

    public PhysicalRoomDto toDto(PhysicalRoom entity);

    public List<PhysicalRoom> toListEntities(List<PhysicalRoomDto> dtos);

    public List<PhysicalRoomDto> toListDtos(List<PhysicalRoom> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public PhysicalRoom updateEntity(PhysicalRoomDto dto, @MappingTarget PhysicalRoom entity);
}
