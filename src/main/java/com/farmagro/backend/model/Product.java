package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "brand", nullable = false, length = 150)
    private String brand;

    @Column(name = "sanitary_registration", nullable = false, length = 100)
    private String sanitaryRegistration;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "active_ingredient", length = 200)
    private String activeIngredient;

    @Column(name = "toxicity_level", nullable = false, length = 3)
    private String toxicityLevel;

    @Column(name = "requires_license", nullable = false)
    private Boolean requiresLicense;

    // URL de imagen del producto (puede ser de Google Drive u otro hosting)
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "technical_specs", columnDefinition = "NVARCHAR(MAX)")
    private String technicalSpecs;

    // BIT en SQL Server → Boolean en Java (true=activo, false=descontinuado)
    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIES_id_category", nullable = false)
    private Category category;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;
}