package com.farmagro.backend.rest;

import com.farmagro.backend.dto.CatalogItemDTO;
import com.farmagro.backend.dto.ProductDTO;
import com.farmagro.backend.service.FileUploadService;
import com.farmagro.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final FileUploadService fileUploadService;

    /** Catálogo público — productos activos con presentaciones disponibles */
    @GetMapping("/catalog")
    public ResponseEntity<List<CatalogItemDTO>> getCatalog() {
        return ResponseEntity.ok(productService.getCatalog());
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getAllActive() {
        return ResponseEntity.ok(productService.findAllActive());
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<ProductDTO>> getAllDeleted() {
        return ResponseEntity.ok(productService.findAllDeleted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(productService.findByCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id,
                                             @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ProductDTO> restore(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.restore(id));
    }

    /**
     * POST /products/upload-image
     * Recibe una imagen, la guarda en el servidor y devuelve la URL relativa.
     * El FE usa esa URL para guardarla en el campo imageUrl del producto.
     *
     * Respuesta: { "url": "/images/products/uuid.jpg" }
     */
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {
        String url = fileUploadService.saveProductImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }
}
