package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * CUSTOMERS — datos adicionales exclusivos del cliente web.
 *
 * Los datos personales (nombre, documento, teléfono, email, contraseña)
 * viven en USERS. Esta tabla solo almacena la información extra que
 * un cliente necesita: dirección (street) y ubicación (ubigeo).
 *
 * Relación 1:1 con USERS via user (UNIQUE en BD).
 * Solo existe una fila aquí si el usuario tiene role = 'CLIENT'.
 */
@Entity
@Table(name = "CUSTOMERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer idCustomer;

    // Descripción libre de la dirección del cliente (calle, número, referencia)
    @Column(name = "street", nullable = false, length = 400)
    private String street;

    // BIT en SQL Server → Boolean en Java (true=activo, false=inactivo)
    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    // FK 1:1 → USERS (UNIQUE en BD garantiza la relación uno a uno)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_id_user", nullable = false, unique = true)
    private User user;

    // FK → UBIGEO (departamento / provincia / distrito)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBIGEO_id_ubigeo", nullable = false)
    private Ubigeo ubigeo;
}
