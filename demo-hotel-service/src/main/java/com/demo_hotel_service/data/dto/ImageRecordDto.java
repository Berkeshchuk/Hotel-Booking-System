package com.demo_hotel_service.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRecordDto {
    private Long id;
    private String url;
    private Integer position;

    // private Long serviceUnitId;
}
