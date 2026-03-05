package com.demo_hotel_service.services;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.common.security.AuthPrincipal;
import com.demo_hotel_service.data.dto.GeneralBookingDto;
import com.demo_hotel_service.data.dto.dto_mappers.GeneralBookingMapper;
import com.demo_hotel_service.data.models.bookings.GeneralBooking;
import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;
import com.demo_hotel_service.repositories.GeneralBookingRepository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeneralBookingService {
    private final GeneralBookingMapper mapper;
    private final GeneralBookingRepository repository;
    private final EntityManager entityManager;

    private final int DEFAULT_PAGE_NUMBER = 0;
    private final int DEFAULT_PAGE_SIZE = 12;

    public String getGeneralBookingsForAdminHtml(Model model) {
        var gBokings = getGeneralBookingsForAdmin(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        model.addAttribute("generalBookings", gBokings);
        return "general-booking-container.html";
    }


    // не перевіряє userId у методі — будь-який залогінений користувач може на /bookings/user/** спробувати підставити чужий userId.*/
    // потрібно передати id/мобільний поточного користувача з сесії (краще використати @PreAuthorize(isOwner))
    public String getGeneralBookingsOfUserHtml(Model model, Long userId, String mobileNumber) {
        var gBokings = getGeneralBookingsOfUser(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_NUMBER));
        model.addAttribute("generalBookings", gBokings);
        return "general-booking-container.html";
    } 

    public List<GeneralBookingDto> getGeneralBookingsForAdmin(Pageable pageable) {
        List<GeneralBooking> gBokings = repository.findAll2(pageable);
        List<GeneralBookingDto> dtos = mapper.toDtos(gBokings);
        return dtos;
    }

    // не перевіряє userId у методі — будь-який залогінений користувач може на /bookings/user/** спробувати підставити чужий userId.*/
    // потрібно передати id/мобільний поточного користувача з сесії (краще використати @PreAuthorize(isOwner))
    // userId can be null mobileNumber - never!
    public List<GeneralBookingDto> getGeneralBookingsOfUser(Pageable pageable) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() ||
           authentication.getPrincipal().equals("anonymousUser")
        ){
            throw new SecurityException("User is not logged in");
        }

        AuthPrincipal currentUser = (AuthPrincipal) authentication.getPrincipal();

        Long userId = currentUser.getId();
        String mobileNumber = currentUser.getMobileNumber();

        List<GeneralBooking> gBokings = repository.findAllBy(userId, mobileNumber, pageable);
        
        return mapper.toDtos(gBokings);
    }

    public GeneralBookingDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return mapper.toDto(repository.findById(id).orElseThrow(null));
    }

    public GeneralBookingDto addGeneralBooking(GeneralBookingDto dto) {
        Map<Long, ServiceUnit> proxyMap = dto.getBookingUnits().stream()
                .map(b -> b.getServiceUnitId())
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> entityManager.getReference(ServiceUnit.class, id)));

        GeneralBooking entity = mapper.toEntity(dto);
        entity.getBookingUnits().forEach(bu -> {
            bu.setServiceUnit(proxyMap.get(bu.getServiceUnitId()));
            bu.setGeneralBooking(entity);
        });

        return mapper.toDto(repository.save(entity));
    }

    public GeneralBookingDto updateGeneralBooking(GeneralBookingDto dto) {
        var existing = repository.findById(dto.getId()).orElseThrow();
        var saved = repository.save(mapper.updateEntity(dto, existing));
        return mapper.toDto(saved);
    }
}
