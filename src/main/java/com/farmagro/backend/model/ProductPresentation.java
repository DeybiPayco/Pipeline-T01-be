package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT_PRESENTATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_presentation")
    private Integer idPresentation;

    @Column(name = "label", nullable = false, length = 50)
    private String label;

    @Column(name = "content", nullable = false, precision = 8, scale = 2)
    private BigDecimal content;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "barcode", nullable = false, length = 50)
    private String barcode;

    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;

    @Column(name = "min_temp_c", nullable = false)
    private Double minTempC;

    @Column(name = "max_temp_c", nullable = false)
    private Double maxTempC;

    @Column(name = "requires_refrigeration", nullable = false)
    private Boolean requiresRefrigeration;

    // BIT en SQL Server → Boolean en Java (true=disponible, false=descontinuada)
    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTS_id_product", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_MEASURE_id_unit", nullable = false)
    private UnitMeasure unitMeasure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLIERS_id_supplier", nullable = false)
    private Supplier supplier;
}