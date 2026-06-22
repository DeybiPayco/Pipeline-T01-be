package com.farmagro.backend.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO público para el catálogo de productos.
 * Agrupa un producto con todas sus presentaciones activas con stock disponible.
 * No expone precios de compra ni datos internos (proveedor, registro sanitario).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItemDTO {

    private Integer idProduct;
    private String  name;
    private String  brand;
    private String  description;
    private String  activeIngredient;
    private String  toxicityLevel;
    private Boolean requiresLicense;
    private String  imageUrl;

    // Categoría
    private Integer categoryId;
    private String  categoryName;

    // Precio mínimo entre todas las presentaciones (para mostrar "Desde S/ X.XX")
    private BigDecimal minPrice;

    // Presentaciones disponibles (stock > 0)
    private List<CatalogPresentationDTO> presentations;

    // ── Presentación anidada ──────────────────────────────────────────────────
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CatalogPresentationDTO {
        private Integer    idPresentation;
        private String     label;
        private BigDecimal content;
        private String     unitMeasureName;
        private String     unitMeasureAbbreviation;
        private BigDecimal salePrice;
        private Integer    currentStock;
        private Boolean    requiresRefrigeration;
    }
}
