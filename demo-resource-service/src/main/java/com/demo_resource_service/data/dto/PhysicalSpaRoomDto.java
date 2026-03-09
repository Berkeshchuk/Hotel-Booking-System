package com.demo_resource_service.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhysicalSpaRoomDto {
    private Long id;
    private String spaRoomNumber;
    private Integer clientCapacity;
    private Integer cleaningTimeInMinutes;
    private Boolean outOfService;
}
