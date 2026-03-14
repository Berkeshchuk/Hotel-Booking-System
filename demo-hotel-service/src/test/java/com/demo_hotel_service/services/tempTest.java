package com.demo_hotel_service.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo_hotel_service.data.dto.ServiceUnitDto;
import com.demo_hotel_service.data.dto.dto_mappers.ServiceUnitMapper;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.repositories.ServiceUnitRepository;

import jakarta.persistence.EntityNotFoundException;

public class tempTest {
    // @Mock
    // private ServiceUnitRepository serviceUnitRepository;

    // @Mock
    // private ServiceUnitMapper serviceUnitMapper;

    // @Mock
    // private ImageService imageService;

    // @InjectMocks
    // private ServiceUnitService service;

    // @BeforeEach
    // void setup() {
    //     MockitoAnnotations.openMocks(this); // Ініціалізація моків
    // }

    // @Test
    // void shouldThrowIfServiceUnitNotFound() {

    //     ServiceUnit entity = new ServiceUnit();
    //     ServiceUnitDto dto = new ServiceUnitDto();
    //     dto.setImageRecords(new ArrayList<>());

    //     when(serviceUnitRepository.findById(1L))
    //             .thenReturn(Optional.of(entity));

    //     when(serviceUnitMapper.toPolymorphicDto(entity))
    //             .thenReturn(dto);

    //     assertThrows(EntityNotFoundException.class,
    //             () -> service.getById(1L));
    // }
}
