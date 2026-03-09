package com.demo_hotel_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo_hotel_service.services.ImageService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping("/api/images")
    public ResponseEntity<?> deleteImage(@RequestParam Long imageId, @RequestParam String imageUrl){
        return ResponseEntity.ok(imageService.removeImage(imageId, imageUrl));
    }
}
