package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDetailDTO {

    private Integer idSaleDetail;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal unitPrice;

    @NotNull(message = "El descuento es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El descuento no puede ser negativo")
    private BigDecimal itemDiscount;

    private BigDecimal subtotal; // calculado automáticamente

    @Size(max = 50)
    private String batchNumber;

    @NotNull(message = "La presentación es obligatoria")
    private Integer presentationId;

    // Solo lectura
    private String presentationLabel;
}