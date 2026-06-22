package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.CatalogItemDTO;
import com.farmagro.backend.dto.ProductDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Category;
import com.farmagro.backend.model.Product;
import com.farmagro.backend.model.ProductPresentation;
import com.farmagro.backend.repository.CategoryRepository;
import com.farmagro.backend.repository.ProductPresentationRepository;
import com.farmagro.backend.repository.ProductRepository;
import com.farmagro.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Productos agroquímicos.
 *
 * <p>Gestiona el ciclo de vida completo del producto:
 * creación, edición, eliminación lógica y restauración.
 * También expone el catálogo público con presentaciones disponibles.</p>
 *
 * <p>El campo {@code status} solo se modifica a través de
 * {@link #delete(Integer)} y {@link #restore(Integer)}, nunca
 * desde {@link #update(Integer, ProductDTO)}.</p>
 *
 * @author Deybi Payco
 * @since Sprint 2
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPresentationRepository presentationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Retorna solo los productos con {@code status = true}. */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllActive() {
        return productRepository.findAllActiveWithCategory(true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Retorna solo los productos con {@code status = false} (eliminados lógicamente), ordenados por fecha de eliminación descendente. */
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllDeleted() {
        return productRepository.findAllDeleted()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCategory(Integer categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", categoryId));
        return productRepository.findByCategoryIdCategory(categoryId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Integer id) {
        return toDTO(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el producto", id)));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        if (productRepository.existsBySanitaryRegistrationIgnoreCase(dto.getSanitaryRegistration()))
            throw new BusinessException("Ya existe un producto con el registro sanitario: "
                    + dto.getSanitaryRegistration());

        if (productRepository.existsByNameIgnoreCase(dto.getName()))
            throw new BusinessException("Ya existe un producto con el nombre: " + dto.getName());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", dto.getCategoryId()));

        Product entity = Product.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .sanitaryRegistration(dto.getSanitaryRegistration())
                .description(dto.getDescription())
                .activeIngredient(dto.getActiveIngredient())
                .toxicityLevel(dto.getToxicityLevel() != null ? dto.getToxicityLevel() : "IV")
                .requiresLicense(dto.getRequiresLicense() != null ? dto.getRequiresLicense() : false)
                .imageUrl(dto.getImageUrl())
                .technicalSpecs(sanitizeJson(dto.getTechnicalSpecs()))
                .status(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();

        return toDTO(productRepository.save(entity));
    }

    @Override
    @Transactional
    public ProductDTO update(Integer id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el producto", id));

        if (!entity.getSanitaryRegistration().equalsIgnoreCase(dto.getSanitaryRegistration())
                && productRepository.existsBySanitaryRegistrationIgnoreCase(dto.getSanitaryRegistration()))
            throw new BusinessException("Ya existe un producto con el registro sanitario: "
                    + dto.getSanitaryRegistration());

        if (!entity.getName().equalsIgnoreCase(dto.getName())
                && productRepository.existsByNameIgnoreCaseAndIdProductNot(dto.getName(), id))
            throw new BusinessException("Ya existe un producto con el nombre: " + dto.getName());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("la categoría", dto.getCategoryId()));

        entity.setName(dto.getName());
        entity.setBrand(dto.getBrand());
        entity.setSanitaryRegistration(dto.getSanitaryRegistration());
        entity.setDescription(dto.getDescription());
        entity.setActiveIngredient(dto.getActiveIngredient());
        entity.setToxicityLevel(dto.getToxicityLevel());
        entity.setRequiresLicense(dto.getRequiresLicense());
        entity.setImageUrl(dto.getImageUrl());
        entity.setTechnicalSpecs(sanitizeJson(dto.getTechnicalSpecs()));
        // El status NO se modifica desde el formulario de edición.
        // Solo se cambia mediante los endpoints delete() y restore().
        entity.setCategory(category);
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(productRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el producto", id));

        entity.setStatus(false);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setRestoredAt(null);

        productRepository.save(entity);
    }

    @Override
    @Transactional
    public ProductDTO restore(Integer id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el producto", id));

        if (Boolean.TRUE.equals(entity.getStatus()))
            throw new BusinessException("El producto ya se encuentra activo");

        entity.setStatus(true);
        entity.setRestoredAt(LocalDateTime.now());
        entity.setDeletedAt(null);

        return toDTO(productRepository.save(entity));
    }

    // ── Catálogo público ──────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<CatalogItemDTO> getCatalog() {
        // Traer solo productos activos con su categoría
        List<Product> products = productRepository.findAllActiveWithCategory(true);

        return products.stream().map(p -> {
            // Presentaciones activas con stock > 0 para este producto
            List<CatalogItemDTO.CatalogPresentationDTO> presentations =
                presentationRepository.findByProductIdProduct(p.getIdProduct())
                    .stream()
                    .filter(pp -> Boolean.TRUE.equals(pp.getStatus()) && pp.getCurrentStock() > 0)
                    .map(pp -> CatalogItemDTO.CatalogPresentationDTO.builder()
                        .idPresentation(pp.getIdPresentation())
                        .label(pp.getLabel())
                        .content(pp.getContent())
                        .unitMeasureName(pp.getUnitMeasure() != null ? pp.getUnitMeasure().getName() : null)
                        .unitMeasureAbbreviation(pp.getUnitMeasure() != null ? pp.getUnitMeasure().getAbbreviation() : null)
                        .salePrice(pp.getSalePrice())
                        .currentStock(pp.getCurrentStock())
                        .requiresRefrigeration(pp.getRequiresRefrigeration())
                        .build())
                    .sorted(Comparator.comparing(CatalogItemDTO.CatalogPresentationDTO::getSalePrice))
                    .collect(Collectors.toList());

            // Precio mínimo entre presentaciones disponibles
            BigDecimal minPrice = presentations.stream()
                .map(CatalogItemDTO.CatalogPresentationDTO::getSalePrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

            return CatalogItemDTO.builder()
                .idProduct(p.getIdProduct())
                .name(p.getName())
                .brand(p.getBrand())
                .description(p.getDescription())
                .activeIngredient(p.getActiveIngredient())
                .toxicityLevel(p.getToxicityLevel())
                .requiresLicense(p.getRequiresLicense())
                .imageUrl(p.getImageUrl())
                .categoryId(p.getCategory() != null ? p.getCategory().getIdCategory() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .minPrice(minPrice)
                .presentations(presentations)
                .build();
        })
        // Solo productos que tengan al menos una presentación disponible
        .filter(item -> !item.getPresentations().isEmpty())
        .sorted(Comparator.comparing(CatalogItemDTO::getCategoryName)
            .thenComparing(CatalogItemDTO::getName))
        .collect(Collectors.toList());
    }

    // ── Sanitización de JSON ──────────────────────────────────────────────────
    /**
     * Limpia el JSON recibido eliminando saltos de línea, retornos de carro
     * y tabulaciones para que SQL Server acepte el valor con el constraint
     * {@code ISJSON()}. Colapsa también múltiples espacios seguidos.
     * Si la cadena no es un JSON válido devuelve {@code null} en lugar de
     * persistir un valor que rompería el constraint de la base de datos.
     *
     * @param raw cadena JSON sin sanitizar (puede ser {@code null})
     * @return JSON limpio en una sola línea, o {@code null} si la entrada está vacía o es inválida
     */
    private String sanitizeJson(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String cleaned = raw.replaceAll("[\\r\\n\\t]", " ").trim();
        cleaned = cleaned.replaceAll("\\s{2,}", " ");
        // Verificar que sea JSON válido antes de persistir
        try {
            new com.fasterxml.jackson.databind.ObjectMapper().readTree(cleaned);
        } catch (Exception e) {
            return null;
        }
        return cleaned;
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private ProductDTO toDTO(Product e) {
        return ProductDTO.builder()
                .idProduct(e.getIdProduct())
                .name(e.getName())
                .brand(e.getBrand())
                .sanitaryRegistration(e.getSanitaryRegistration())
                .description(e.getDescription())
                .activeIngredient(e.getActiveIngredient())
                .toxicityLevel(e.getToxicityLevel())
                .requiresLicense(e.getRequiresLicense())
                .imageUrl(e.getImageUrl())
                .technicalSpecs(e.getTechnicalSpecs())
                .status(e.getStatus())
                .categoryId(e.getCategory() != null ? e.getCategory().getIdCategory() : null)
                .categoryName(e.getCategory() != null ? e.getCategory().getName() : null)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .build();
    }
}