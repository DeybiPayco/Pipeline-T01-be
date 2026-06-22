package com.farmagro.backend.rest;

import com.farmagro.backend.dto.SaleDTO;
import com.farmagro.backend.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAll() {
        return ResponseEntity.ok(saleService.findAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SaleDTO>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(saleService.findByCustomer(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SaleDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(saleService.findByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(saleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleDTO> create(@Valid @RequestBody SaleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> update(@PathVariable Integer id, @Valid @RequestBody SaleDTO dto) {
        return ResponseEntity.ok(saleService.update(id, dto));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Integer id) {
        saleService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}