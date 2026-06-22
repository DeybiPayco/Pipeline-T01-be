package com.farmagro.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovementDTO {

    private Integer idMovement;
    private Integer idReference;
    // 'ENTRADA' | 'SALIDA' | 'AJUSTE'
    private String movementType;
    private Integer quantity;
    private Integer stockBefore;
    private Integer stockAfter;
    private String reason;
    private Double unitCost;
    private String batchNumber;
    private LocalDateTime expiryDate;
    private LocalDateTime movementDate;
    private Integer presentationId;
    private String presentationLabel;
    private Integer userId;
    private String userName;
}