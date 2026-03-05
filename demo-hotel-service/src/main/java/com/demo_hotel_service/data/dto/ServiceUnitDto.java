package com.demo_hotel_service.data.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "classType")
@JsonSubTypes({
                @JsonSubTypes.Type(value = SpaUnitDto.class, name = "SPA"),
                @JsonSubTypes.Type(value = RoomUnitDto.class, name = "ROOM")
})
public class ServiceUnitDto {
        private Long id;
        private BigDecimal price;
        private String description;
        private String type;
        private Integer guestCapacity;
        private Boolean hiddenFromClient;
        private Boolean outOfService;
        private Set<String> facilities;
        private List<ImageRecordDto> imageRecords;
}
