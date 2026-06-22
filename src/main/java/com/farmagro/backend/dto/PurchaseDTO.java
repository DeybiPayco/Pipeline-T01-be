package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDTO {

    private Integer idPurchase;

    @NotNull(message = "La fecha de compra es obligatoria")
    private LocalDateTime purchaseDate;

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50)
    private String invoiceNumber;

    private LocalDateTime expectedDeliveryDate;

    private BigDecimal subtotal;  // calculado automáticamente
    private BigDecimal taxAmount; // calculado automáticamente
    private BigDecimal total;     // calculado automáticamente

    // BD: CHECK (status IN ('PAGADO','PENDIENTE','ANULADO')) varchar(10)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PAGADO|PENDIENTE|ANULADO)$",
            message = "El estado debe ser: PAGADO, PENDIENTE o ANULADO")
    private String status;

    // BD: notes varchar(300) NULL
    @Size(max = 300)
    private String notes;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer supplierId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer userId;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    private List<PurchaseDetailDTO> details;

    // Solo lectura
    private String supplierName;
    private String userName;
}
