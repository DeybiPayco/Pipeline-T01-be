package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDTO {

    private Integer idDelivery;

    // Delivery | Recojo | Courier  (CHECK en BD)
    @NotBlank(message = "El tipo de entrega es obligatorio")
    @Pattern(regexp = "^(Delivery|Recojo|Courier)$",
            message = "El tipo debe ser: Delivery, Recojo o Courier")
    private String deliveryType;

    @NotBlank(message = "La referencia es obligatoria")
    @Size(max = 150)
    private String reference;

    // Dirección textual — puede pre-rellenarse desde el perfil del cliente
    @Size(max = 400)
    private String deliveryAddress;

    // deliveryLat y deliveryLon eliminados — sin coordenadas GPS

    @NotNull(message = "La fecha programada es obligatoria")
    private LocalDateTime scheduledDate;

    private LocalDateTime actualDate;

    // ENTREGADO | PENDIENTE | EN_CAMINO | ANULADO  (CHECK en BD)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ENTREGADO|PENDIENTE|EN_CAMINO|ANULADO)$",
            message = "Estado debe ser: ENTREGADO, PENDIENTE, EN_CAMINO o ANULADO")
    private String status;

    @Size(max = 250)
    private String notes;

    @NotNull(message = "La venta es obligatoria")
    private Integer saleId;

    @NotNull(message = "El usuario es obligatorio")
    private Integer userId;

    // Solo lectura
    private String saleInvoice;
    private String userName;
}
