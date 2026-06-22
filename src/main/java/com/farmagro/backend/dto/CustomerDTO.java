package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * CustomerDTO — representa los datos del perfil de cliente.
 *
 * Los datos personales (nombre, documento, teléfono, email) se obtienen
 * del usuario asociado (userId) y se incluyen aquí como campos de solo
 * lectura para facilitar la visualización sin JOINs adicionales en el FE.
 *
 * Para crear un cliente desde la web se usa RegisterClientDTO (registro
 * conjunto User + Customer en una sola operación).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Integer idCustomer;

    // ── Datos propios del perfil de cliente ──────────────────────────────────

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 400, message = "La dirección no puede superar los 400 caracteres")
    private String street;

    @NotNull(message = "El ubigeo es obligatorio")
    private Integer ubigeoId;

    // Solo lectura en respuestas GET
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;

    // ── Datos del usuario asociado (solo lectura — vienen de USERS via JOIN) ─

    @NotNull(message = "El usuario es obligatorio")
    private Integer userId;

    private String docType;
    private String docNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    // Solo lectura — display del ubigeo (Departamento - Provincia - Distrito)
    private String ubigeoDisplay;
}
