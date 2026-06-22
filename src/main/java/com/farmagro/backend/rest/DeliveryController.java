package com.farmagro.backend.rest;

import com.farmagro.backend.dto.DeliveryDTO;
import com.farmagro.backend.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(deliveryService.findByStatus(status));
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<DeliveryDTO>> getBySale(@PathVariable Integer saleId) {
        return ResponseEntity.ok(deliveryService.findBySale(saleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> create(@Valid @RequestBody DeliveryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> update(@PathVariable Integer id, @Valid @RequestBody DeliveryDTO dto) {
        return ResponseEntity.ok(deliveryService.update(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Integer id) {
        deliveryService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}