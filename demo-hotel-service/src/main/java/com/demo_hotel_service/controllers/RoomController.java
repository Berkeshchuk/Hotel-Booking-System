package com.demo_hotel_service.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.common.security.AuthPrincipal;
import com.demo_hotel_service.data.dto.RoomUnitDto;
import com.demo_hotel_service.data.models.hotel_offerings.rooms.RoomUnit;
import com.demo_hotel_service.services.ServiceUnitService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {
    private final ServiceUnitService roomService;

    @GetMapping("/rooms")
    public String getRoomsHtml(Model model) {
        return roomService.getServiceUnitsHtml(model, "room-container.html", "rooms", RoomUnit.class);
    }

    @GetMapping("/api/rooms")
    public ResponseEntity<?> getRooms() {
        return ResponseEntity.ok(roomService.getServiceUnits(RoomUnit.class, PageRequest.of(0, 10)));
    }

    
    @PostMapping("/api/rooms")
    @CrossOrigin(origins = "http://localhost:8085")
    public ResponseEntity<?> addRoom(
            @RequestPart("room") RoomUnitDto room,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(roomService.addServiceUnit(room, imageFiles));
    }

    @PostMapping("/api/rooms2")
    public ResponseEntity<?> addRoom2(
            @RequestBody RoomUnitDto room) {
        return ResponseEntity.ok(roomService.addServiceUnit(room, null));
    }



    /*
     * <script>
     * async function submitRoom() {
     * // 1. Беремо файли
     * const files = document.getElementById('images').files;
     * 
     * // 2. Будуємо DTO
     * const roomDto = {
     * description: document.getElementById('description').value,
     * price: parseFloat(document.getElementById('price').value),
     * // додаткові поля DTO за потребою
     * };
     * 
     * // 3. Створюємо FormData
     * const formData = new FormData();
     * 
     * // додаємо DTO як JSON
     * formData.append('room', new Blob([JSON.stringify(roomDto)], { type:
     * 'application/json' }));
     * 
     * // додаємо файли
     * for (let i = 0; i < files.length; i++) {
     * formData.append('imageFiles', files[i]);
     * }
     * 
     * // 4. Відправляємо fetch
     * const response = await fetch('http://localhost:8085/rooms', {
     * method: 'POST',
     * body: formData,
     * });
     * 
     * const data = await response.json();
     * console.log(data);
     * }
     * </script>
     */
}

