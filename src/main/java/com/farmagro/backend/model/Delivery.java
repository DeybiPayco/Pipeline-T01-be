package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DELIVERIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delivery")
    private Integer idDelivery;

    // Delivery | Recojo | Courier  (CHECK en BD)
    @Column(name = "delivery_type", nullable = false, length = 20)
    private String deliveryType;

    @Column(name = "reference", nullable = false, length = 150)
    private String reference;

    // Dirección textual de entrega — se puede pre-rellenar desde CUSTOMERS.street + UBIGEO
    @Column(name = "delivery_address", length = 400)
    private String deliveryAddress;

    // delivery_lat y delivery_lon eliminados — sin coordenadas GPS

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    // ENTREGADO | PENDIENTE | EN_CAMINO | ANULADO  (CHECK en BD)
    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "notes", length = 250)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALES_id_sale", nullable = false)
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_id_user", nullable = false)
    private User user;
}
