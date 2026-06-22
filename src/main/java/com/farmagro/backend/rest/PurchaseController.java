package com.farmagro.backend.rest;

import com.farmagro.backend.dto.PurchaseDTO;
import com.farmagro.backend.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAll() {
        return ResponseEntity.ok(purchaseService.findAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PurchaseDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(purchaseService.findByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(purchaseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> create(@Valid @RequestBody PurchaseDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDTO> update(@PathVariable Integer id, @Valid @RequestBody PurchaseDTO dto) {
        return ResponseEntity.ok(purchaseService.update(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Integer id) {
        purchaseService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}