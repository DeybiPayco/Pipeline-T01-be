package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para transferir datos de proveedores
 * entre las capas de la aplicación.
 * <p>
 * Incluye validaciones de formato y obligatoriedad para garantizar
 * la integridad de los datos antes de persistirlos.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierDTO {

    /** Identificador único del proveedor. Null al crear un nuevo registro. */
    private Integer idSupplier;

    /**
     * Tipo de documento del proveedor.
     * Valores permitidos: {@code RUC}, {@code DNI} o {@code CE}.
     */
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^(RUC|DNI|CE)$", message = "El tipo de documento debe ser RUC, DNI o CE")
    private String docType;

    /**
     * Número de documento del proveedor.
     * Máximo 11 caracteres según el tipo de documento.
     */
    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 11, message = "El número de documento no puede superar los 11 caracteres")
    private String docNumber;

    /**
     * Razón social o nombre de la empresa proveedora.
     * Máximo 150 caracteres.
     */
    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 150, message = "La razón social no puede superar los 150 caracteres")
    private String companyName;

    /**
     * Nombre de la persona de contacto en la empresa (campo opcional).
     * Máximo 200 caracteres.
     */
    @Size(max = 200, message = "El nombre de contacto no puede superar los 200 caracteres")
    private String contactPerson;

    /**
     * Número de teléfono del proveedor.
     * Debe contener exactamente 9 dígitos numéricos.
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener exactamente 9 dígitos")
    private String phoneContact;

    /**
     * Correo electrónico del proveedor.
     * Debe tener un formato válido y máximo 150 caracteres.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150)
    private String email;

    /**
     * Dirección física del proveedor.
     * Máximo 150 caracteres.
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150)
    private String street;

    /** Certificaciones y registros del proveedor en formato JSON (campo opcional). */
    private String certifications;

    /** Estado del proveedor: {@code true} activo, {@code false} eliminado lógicamente. */
    private Boolean status;

    /** Identificador del ubigeo asociado al proveedor. */
    @NotNull(message = "El ubigeo es obligatorio")
    private Integer ubigeoId;

    /** Representación legible del ubigeo (distrito, provincia). Solo lectura. */
    private String ubigeoDisplay;

    /** Fecha y hora de creación del registro. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización. */
    private LocalDateTime updatedAt;

    /** Fecha y hora de eliminación lógica (null si está activo). */
    private LocalDateTime deletedAt;

    /** Fecha y hora de restauración tras eliminación lógica. */
    private LocalDateTime restoredAt;
}
