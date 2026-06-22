package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPresentationDTO {

    private Integer idPresentation;

    @NotBlank(message = "La etiqueta es obligatoria")
    @Size(max = 50)
    private String label;

    @NotNull(message = "El contenido es obligatorio")
    @DecimalMin(value = "0.01", message = "El contenido debe ser mayor a 0")
    private BigDecimal content;

    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor a 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de venta debe ser mayor a 0")
    private BigDecimal salePrice;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer currentStock;

    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 50)
    private String barcode;

    @Min(value = 1, message = "La vida útil debe ser mayor a 0")
    private Integer shelfLifeDays;

    private Double minTempC;

    private Double maxTempC;

    private Boolean requiresRefrigeration;

    // true = disponible, false = descontinuada (BIT en BD)
    private Boolean status;

    @NotNull(message = "El producto es obligatorio")
    private Integer productId;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Integer unitMeasureId;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer supplierId;

    // Solo lectura
    private String productName;
    private String unitMeasureName;
    private String supplierName;
}