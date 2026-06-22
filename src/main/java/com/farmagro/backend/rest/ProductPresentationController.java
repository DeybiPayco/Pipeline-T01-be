package com.farmagro.backend.rest;

import com.farmagro.backend.dto.ProductPresentationDTO;
import com.farmagro.backend.service.ProductPresentationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/presentations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductPresentationController {

    private final ProductPresentationService presentationService;

    @GetMapping
    public ResponseEntity<List<ProductPresentationDTO>> getAll() {
        return ResponseEntity.ok(presentationService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductPresentationDTO>> getAllActive() {
        return ResponseEntity.ok(presentationService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPresentationDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(presentationService.findById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductPresentationDTO>> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(presentationService.findByProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductPresentationDTO> create(@Valid @RequestBody ProductPresentationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(presentationService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPresentationDTO> update(@PathVariable Integer id,
                                                         @Valid @RequestBody ProductPresentationDTO dto) {
        return ResponseEntity.ok(presentationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        presentationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}