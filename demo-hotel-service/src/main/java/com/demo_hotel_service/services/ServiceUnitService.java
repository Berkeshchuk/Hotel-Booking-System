package com.demo_hotel_service.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.demo_hotel_service.repositories.ServiceUnitRepository;

import jakarta.persistence.EntityNotFoundException;

import com.demo_hotel_service.data.dto.ImageRecordDto;
import com.demo_hotel_service.data.dto.ServiceUnitDto;
import com.demo_hotel_service.data.dto.dto_mappers.ServiceUnitMapper;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.data.models.images.ImageRecord;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceUnitService {
    private final ServiceUnitRepository serviceUnitRepository;
    private final ServiceUnitMapper serviceUnitMapper;
    private final ImageService imageService;

    private final int DEFAULT_PAGE_NUMBER = 0;
    private final int DEFAULT_PAGE_SIZE = 12;

    public String getServiceUnitsHtml(Model model, String template, String attributeName,
    Class<? extends ServiceUnit> classType) {
        List<ServiceUnitDto> serviceUnitDtos = getServiceUnits(classType,
                PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        model.addAttribute(attributeName, serviceUnitDtos);
        return template;
    }

    public List<ServiceUnitDto> getServiceUnits(Class<? extends ServiceUnit> classType, Pageable pageable) {
        List<ServiceUnit> serviceUnits = serviceUnitRepository.findByType(classType, pageable);
        List<ServiceUnitDto> serviceUnitDtos = serviceUnitMapper.toPolymorphicDtos(serviceUnits);
        sortImageRecordsOfServiceUnits(serviceUnitDtos);
        return serviceUnitDtos;
    }

    public ServiceUnitDto getById(long id) {
        ServiceUnit existing = serviceUnitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service with not found"));
        ServiceUnitDto existingDto = serviceUnitMapper.toPolymorphicDto(existing);
        sortImageRecordsOfServiceUnit(existingDto);
        return existingDto;
    }

    public ServiceUnitDto addServiceUnit(ServiceUnitDto serviceUnitDto, List<MultipartFile> imageFiles) {
        if (serviceUnitDto == null) {
            throw new IllegalArgumentException("Data can't be null");
        }

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<ImageRecordDto> imageDtos = imageService.prepareInitialImageDtos(imageFiles);
            serviceUnitDto.setImageRecords(imageDtos);
        }

        var entity = serviceUnitMapper.toPolymorphicEntity(serviceUnitDto);
        int i = 0;
        for (ImageRecord imageRecord : entity.getImageRecords()) {
            imageRecord.setServiceUnit(entity);
        }


        ServiceUnitDto savedDto = serviceUnitMapper
                .toPolymorphicDto(serviceUnitRepository.save(entity));

        return savedDto;
    }

    public ServiceUnitDto updateServiceUnit(ServiceUnitDto updatingData) {
        ServiceUnit toUpdate = serviceUnitRepository.findById(updatingData.getId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        serviceUnitMapper.updateEntity(updatingData, toUpdate);
        ServiceUnitDto updatedDto = serviceUnitMapper.toPolymorphicDto(serviceUnitRepository.save(toUpdate));
        return updatedDto;
    }

    @Transactional
    public boolean deleteServiceUnit(long serviceId) throws IOException {
        if (!serviceUnitRepository.existsById(serviceId)) {
            throw new EntityNotFoundException("Service not found");
        }
        imageService.removeImages(serviceId);
        serviceUnitRepository.deleteById(serviceId);
        return serviceUnitRepository.existsById(serviceId);
    }

    private void sortImageRecordsOfServiceUnits(List<ServiceUnitDto> serviceUnitDtos){
        serviceUnitDtos.forEach( su ->{
            su.getImageRecords().sort((img1, img2)-> img1.getPosition() > img2.getPosition() ? 1 : -1);
        });
    }

    private void sortImageRecordsOfServiceUnit(ServiceUnitDto serviceUnitDto){
        serviceUnitDto.getImageRecords().sort((img1, img2) -> img1.getPosition() < img2.getPosition() ? 1 : -1);
    }


}
