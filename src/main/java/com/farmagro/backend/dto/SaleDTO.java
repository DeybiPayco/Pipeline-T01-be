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
public class SaleDTO {

    private Integer idSale;

    @NotNull(message = "La fecha de venta es obligatoria")
    private LocalDateTime saleDate;

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50)
    private String invoiceNumber;

    // BD: CHECK (payment_method IN ('Efectivo','Transferencia','Yape','Plin')) varchar(15)
    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "^(Efectivo|Transferencia|Yape|Plin)$",
            message = "El método de pago debe ser: Efectivo, Transferencia, Yape o Plin")
    private String paymentMethod;

    private BigDecimal subtotal;   // calculado automáticamente
    private BigDecimal discount;   // calculado automáticamente
    private BigDecimal taxAmount;  // calculado automáticamente
    private BigDecimal total;      // calculado automáticamente

    // BD: CHECK (status IN ('PAGADO','PENDIENTE','ANULADO')) varchar(10)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PAGADO|PENDIENTE|ANULADO)$",
            message = "El estado debe ser: PAGADO, PENDIENTE o ANULADO")
    private String status;

    // BD: notes varchar(200) NULL
    @Size(max = 200)
    private String notes;

    // BD: CHECK (sales_channel IN ('Tienda','WhatsApp','Web','Telefono','Otro')) varchar(20)
    @NotBlank(message = "El canal de venta es obligatorio")
    @Pattern(regexp = "^(Tienda|WhatsApp|Web|Telefono|Otro)$",
            message = "El canal de venta debe ser: Tienda, WhatsApp, Web, Telefono u Otro")
    private String salesChannel;

    @NotNull(message = "El cliente es obligatorio")
    private Integer customerId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer userId;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    private List<SaleDetailDTO> details;

    // Solo lectura
    private String customerName;
    private String userName;
}
