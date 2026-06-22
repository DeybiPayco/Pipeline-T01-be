package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa a un proveedor en el sistema FarmaGro.
 * <p>
 * Mapea la tabla {@code SUPPLIERS} en la base de datos y contiene
 * la información de identificación, contacto y estado del proveedor.
 * </p>
 */
@Entity
@Table(name = "SUPPLIERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    /** Identificador único del proveedor (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supplier")
    private Integer idSupplier;

    /** Tipo de documento del proveedor (RUC, DNI o CE). */
    @Column(name = "doc_type", nullable = false, length = 3)
    private String docType;

    /** Número de documento del proveedor. */
    @Column(name = "doc_number", nullable = false, length = 11)
    private String docNumber;

    /** Razón social o nombre de la empresa proveedora. */
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    /** Nombre de la persona de contacto en la empresa (opcional). */
    @Column(name = "contact_person", length = 200)
    private String contactPerson;

    /** Número de teléfono de contacto (exactamente 9 dígitos). */
    @Column(name = "phone_contact", nullable = false, length = 9)
    private String phoneContact;

    /** Correo electrónico del proveedor. */
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    /** Dirección física del proveedor. */
    @Column(name = "street", nullable = false, length = 150)
    private String street;

    /** Certificaciones y registros del proveedor en formato JSON (opcional). */
    @Column(name = "certifications", columnDefinition = "NVARCHAR(MAX)")
    private String certifications;

    /** Estado del proveedor: {@code true} activo, {@code false} eliminado lógicamente. */
    @Column(name = "status", nullable = false)
    private Boolean status;

    /** Ubicación geográfica del proveedor mediante la entidad Ubigeo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBIGEO_id_ubigeo", nullable = false)
    private Ubigeo ubigeo;

    /** Fecha y hora de creación del registro (no actualizable). */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Fecha y hora de la última actualización del registro. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Fecha y hora de eliminación lógica del proveedor (null si está activo). */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /** Fecha y hora de restauración del proveedor tras una eliminación lógica. */
    @Column(name = "restored_at")
    private LocalDateTime restoredAt;
}
