package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    // DNI | CE | RUC | PAS  (CHECK en BD)
    @Column(name = "doc_type", nullable = false, length = 3)
    private String docType;

    @Column(name = "doc_number", nullable = false, length = 12)
    private String docNumber;

    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "phone", length = 9)
    private String phone;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    // ADMIN | VEND | ALMAC | DELIV | CLIENT  (CHECK en BD)
    @Column(name = "role", nullable = false, length = 6)
    private String role;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

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
}
