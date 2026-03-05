package com.demo_hotel_service.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.demo_hotel_service.data.dto.SpaUnitDto;
import com.demo_hotel_service.data.models.hotel_offerings.spa.SpaUnit;
import com.demo_hotel_service.services.ServiceUnitService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SpaController {
    private final ServiceUnitService spaService;

    @GetMapping("/spas")
    public String getSpasHtml(Model model) {
        return spaService.getServiceUnitsHtml(model, "spas", "spas", SpaUnit.class);
    }

    @GetMapping("/api/spas")
    public ResponseEntity<?> getSpas() {
        return ResponseEntity.ok(spaService.getServiceUnits(SpaUnit.class, PageRequest.of(0, 10)));
    }

    // @PostMapping("/api/spas")
    // public ResponseEntity<?> addSpa(
    //         @RequestPart("spa") SpaUnitDto spa,
    //         @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
    //     return ResponseEntity.ok(spaService.addServiceUnit(spa, imageFiles));
    // }

    @PostMapping("/api/spas")
    public ResponseEntity<?> addSpa( @RequestBody SpaUnitDto spa) {
        return ResponseEntity.ok(spaService.addServiceUnit(spa, null));
    }
}
