package com.demo_hotel_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo_hotel_service.data.dto.ImageRecordDto;
import com.demo_hotel_service.data.dto.ServiceUnitDto;
import com.demo_hotel_service.data.dto.dto_mappers.ServiceUnitMapper;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.data.models.hotel_offerings.rooms.RoomUnit;
import com.demo_hotel_service.repositories.ServiceUnitRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceUnitServiceTest {
        @Mock
        private ServiceUnitRepository serviceUnitRepository;
        @Mock
        private ServiceUnitMapper serviceUnitMapper;
        @Mock
        private ImageService imageService;

        @InjectMocks
        private ServiceUnitService service;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void shouldReturnServiceUnits() {
                Pageable pageable = PageRequest.of(0, 12);

                List<ServiceUnit> entities = List.of(new ServiceUnit());

                ServiceUnitDto dto = new ServiceUnitDto();
                dto.setImageRecords(new ArrayList<>());

                List<ServiceUnitDto> dtos = List.of(dto);

                when(serviceUnitRepository.findByType(RoomUnit.class, pageable))
                                .thenReturn(entities);
                when(serviceUnitMapper.toPolymorphicDtos(entities))
                                .thenReturn(dtos);

                List<ServiceUnitDto> result = service.getServiceUnits(RoomUnit.class, pageable);

                assertEquals(1, result.size());
        }

        @Test
        void shouldSortImages() {
                ServiceUnitDto dto = new ServiceUnitDto();

                ImageRecordDto img1 = new ImageRecordDto();
                img1.setPosition(2);

                ImageRecordDto img2 = new ImageRecordDto();
                img2.setPosition(1);

                dto.setImageRecords(new ArrayList<>(List.of(img1, img2)));

                when(serviceUnitRepository.findByType(any(), any()))
                                .thenReturn(List.of(new ServiceUnit()));
                when(serviceUnitMapper.toPolymorphicDtos(any()))
                                .thenReturn(List.of(dto));

                service.getServiceUnits(RoomUnit.class, PageRequest.of(0, 12));

                assertEquals(1, dto.getImageRecords().get(0).getPosition());
        }

        @Test
        void shouldReturnServiceUnitById() {
                ServiceUnit entity = new ServiceUnit();
                ServiceUnitDto dto = new ServiceUnitDto();
                dto.setImageRecords(new ArrayList<>());

                when(serviceUnitRepository.findById(1L))
                                .thenReturn(Optional.of(entity));

                when(serviceUnitMapper.toPolymorphicDto(entity))
                                .thenReturn(dto);

                ServiceUnitDto result = service.getById(1L);

                assertNotNull(result);
        }

        @Test
        void shouldThrowIfServiceUnitNotFound() {

                when(serviceUnitRepository.findById(1L))
                                .thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class,
                                () -> service.getById(1L));
        }

        @Test
        void shouldAddServiceUnit() {

                ServiceUnitDto dto = new ServiceUnitDto();
                ServiceUnit entity = new ServiceUnit();

                when(serviceUnitMapper.toPolymorphicEntity(dto))
                                .thenReturn(entity);

                when(serviceUnitRepository.save(entity))
                                .thenReturn(entity);

                when(serviceUnitMapper.toPolymorphicDto(entity))
                                .thenReturn(dto);

                ServiceUnitDto result = service.addServiceUnit(dto, List.of());

                assertNotNull(result);
        }

        @Test
        void shouldPrepareImages() {
                ServiceUnitDto dto = new ServiceUnitDto();
                dto.setImageRecords(new ArrayList<>());

                MultipartFile file = mock(MultipartFile.class);

                when(imageService.prepareInitialImageDtos(any()))
                                .thenReturn(List.of(new ImageRecordDto()));

                ServiceUnit entity = new ServiceUnit();
                entity.setImageRecords(new ArrayList<>());
                when(serviceUnitMapper.toPolymorphicEntity(any())).thenReturn(entity);

                when(serviceUnitRepository.save(entity)).thenReturn(entity);
                when(serviceUnitMapper.toPolymorphicDto(entity)).thenReturn(dto);

                ServiceUnitDto result = service.addServiceUnit(dto, List.of(file));

                assertNotNull(result.getImageRecords());
                assertFalse(result.getImageRecords().isEmpty());
        }

        @Test
        void shouldUpdateServiceUnit() {

                ServiceUnit existing = new ServiceUnit();
                ServiceUnitDto dto = new ServiceUnitDto();
                dto.setId(1L);

                when(serviceUnitRepository.findById(1L))
                                .thenReturn(Optional.of(existing));

                when(serviceUnitRepository.save(existing))
                                .thenReturn(existing);

                when(serviceUnitMapper.toPolymorphicDto(existing))
                                .thenReturn(dto);

                ServiceUnitDto result = service.updateServiceUnit(dto, List.of());

                assertNotNull(result);
        }

        @Test
        void shouldDeleteServiceUnit() {

                when(serviceUnitRepository.existsById(1L))
                                .thenReturn(true)
                                .thenReturn(false);

                Boolean result = service.deleteServiceUnit(1L);

                assertFalse(result);
        }
}
