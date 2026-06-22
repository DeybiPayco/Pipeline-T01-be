package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UNIT_MEASURE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitMeasure {

    @Id
    // Sin @GeneratedValue: UNIT_MEASURE es un catálogo fijo (IDs asignados manualmente)
    @Column(name = "id_unit")
    private Integer idUnit;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "abbreviation", nullable = false, length = 10)
    private String abbreviation;
}