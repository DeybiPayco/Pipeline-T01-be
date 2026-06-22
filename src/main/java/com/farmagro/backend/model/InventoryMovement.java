package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "INVENTORY_MOVEMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movement")
    private Integer idMovement;

    @Column(name = "id_reference", nullable = false)
    private Integer idReference;

    // 'ENTRADA' | 'SALIDA' | 'AJUSTE'
    @Column(name = "movement_type", nullable = false, length = 10)
    private String movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "stock_before", nullable = false)
    private Integer stockBefore;

    @Column(name = "stock_after", nullable = false)
    private Integer stockAfter;

    @Column(name = "reason", nullable = false, length = 150)
    private String reason;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "movement_date", nullable = false, updatable = false)
    private LocalDateTime movementDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_PRESENTATIONS_id_presentation", nullable = false)
    private ProductPresentation presentation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_id_user", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (movementDate == null) movementDate = LocalDateTime.now();
    }
}
