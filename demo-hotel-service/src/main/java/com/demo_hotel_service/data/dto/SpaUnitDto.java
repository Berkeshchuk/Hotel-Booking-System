package com.demo_hotel_service.data.dto;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaUnitDto extends ServiceUnitDto{
    private String name;
    private List<StringPairDto> careProductsDescriptions; 
    private List<StringPairDto> spaStagesDescriptions;
    private String preparingInfoForClient;

    private List<String> contraindications;
    private List<String> cautionNotes;

    private Integer durationInMinutes;
}
