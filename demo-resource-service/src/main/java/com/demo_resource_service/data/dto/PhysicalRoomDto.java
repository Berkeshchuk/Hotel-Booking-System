package com.demo_resource_service.data.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PhysicalRoomDto {
    private Long id; 
    private String roomNumber;
    private Integer cleaningTimeInMinutes;
    private Boolean outOfService;

}
