package com.farmagro.backend.rest;

import com.farmagro.backend.dto.InventoryMovementDTO;
import com.farmagro.backend.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventory-movements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryMovementController {

    private final InventoryMovementService movementService;

    @GetMapping
    public ResponseEntity<List<InventoryMovementDTO>> getAll() {
        return ResponseEntity.ok(movementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(movementService.findById(id));
    }

    @GetMapping("/presentation/{presentationId}")
    public ResponseEntity<List<InventoryMovementDTO>> getByPresentation(@PathVariable Integer presentationId) {
        return ResponseEntity.ok(movementService.findByPresentation(presentationId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<InventoryMovementDTO>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(movementService.findByType(type));
    }
}