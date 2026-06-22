package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDetailDTO {

    private Integer idPurchaseDetail;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal unitPrice;

    private BigDecimal subtotal; // calculado automáticamente

    @NotBlank(message = "El número de lote es obligatorio")
    @Size(max = 50)
    private String batchNumber;

    @NotNull(message = "La fecha de fabricación es obligatoria")
    private LocalDateTime manufactureDate;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDateTime expiryDate;

    @NotNull(message = "La presentación es obligatoria")
    private Integer presentationId;

    // Solo lectura
    private String presentationLabel;
}
