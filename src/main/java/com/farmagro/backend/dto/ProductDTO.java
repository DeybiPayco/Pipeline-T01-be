package com.farmagro.backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para el maestro de Productos agroquímicos.
 *
 * <p>Se usa tanto en las peticiones de entrada (POST/PUT) como en las
 * respuestas de salida (GET). Los campos de auditoría ({@code createdAt},
 * {@code updatedAt}, etc.) son de solo lectura y no deben enviarse en
 * las peticiones de creación o edición.</p>
 *
 * <p>El campo {@code status} NO se expone en el formulario de edición:
 * el ciclo de vida activo/inactivo se controla exclusivamente mediante
 * los endpoints {@code DELETE /{id}} (eliminación lógica) y
 * {@code PATCH /{id}/restore} (restauración).</p>
 *
 * @author Deybi Payco
 * @since Sprint 2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    /** Identificador único del producto. Nulo en peticiones de creación. */
    private Integer idProduct;

    /**
     * Nombre comercial del producto agroquímico.
     * Obligatorio, máximo 150 caracteres.
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String name;

    /**
     * Marca o fabricante del producto.
     * Obligatorio, máximo 150 caracteres.
     */
    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 150, message = "La marca no puede superar los 150 caracteres")
    private String brand;

    /**
     * Número de registro sanitario emitido por SENASA u organismo equivalente.
     * Formato recomendado: {@code SENASA-XXX}. Máximo 100 caracteres.
     */
    @Size(max = 100, message = "El registro sanitario no puede superar los 100 caracteres")
    private String sanitaryRegistration;

    /**
     * Descripción del uso, composición y características del producto.
     * Obligatorio, entre 10 y 300 caracteres.
     */
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 300, message = "La descripción debe tener entre 10 y 300 caracteres")
    private String description;

    /**
     * Principio activo del agroquímico (ej: "Glifosato 48%").
     * Opcional, máximo 200 caracteres.
     */
    @Size(max = 200, message = "El ingrediente activo no puede superar los 200 caracteres")
    private String activeIngredient;

    /**
     * Nivel de toxicidad según clasificación OMS.
     * Valores aceptados: {@code I}, {@code II}, {@code III}, {@code IV}.
     */
    @Pattern(regexp = "^(I|II|III|IV)$", message = "El nivel de toxicidad debe ser: I, II, III o IV")
    private String toxicityLevel;

    /**
     * Indica si el producto requiere licencia especial de aplicación.
     * Por defecto {@code false}.
     */
    private Boolean requiresLicense;

    /**
     * URL de la imagen del producto.
     * Puede ser una URL relativa del servidor ({@code /images/products/uuid.jpg})
     * o una URL externa. Máximo 255 caracteres.
     */
    @Size(max = 255, message = "La URL de imagen no puede superar los 255 caracteres")
    private String imageUrl;

    /**
     * Especificaciones técnicas de aplicación en formato JSON.
     * Ejemplo: {@code {"dose_ha":"2L","preharvest_days":7,"reentry_hours":4}}.
     * El servicio sanitiza el JSON antes de persistirlo.
     */
    private String technicalSpecs;

    /**
     * Estado lógico del producto.
     * {@code true} = activo, {@code false} = eliminado lógicamente.
     * No se debe modificar directamente desde el formulario de edición.
     */
    private Boolean status;

    /**
     * ID de la categoría a la que pertenece el producto.
     * Obligatorio.
     */
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoryId;

    /** Nombre de la categoría. Solo lectura en respuestas GET. */
    private String categoryName;

    /** Fecha y hora de creación del registro. Solo lectura. */
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización. Solo lectura. */
    private LocalDateTime updatedAt;

    /** Fecha y hora de la eliminación lógica. Nulo si está activo. Solo lectura. */
    private LocalDateTime deletedAt;

    /** Fecha y hora de la última restauración. Solo lectura. */
    private LocalDateTime restoredAt;
}