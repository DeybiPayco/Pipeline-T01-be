package com.farmagro.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UBIGEO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubigeo {

    @Id
    // Sin @GeneratedValue: UBIGEO usa IDs asignados manualmente (catálogo INEI)
    @Column(name = "id_ubigeo")
    private Integer idUbigeo;

    @Column(name = "departament", nullable = false, length = 150)
    private String departament;

    @Column(name = "province", nullable = false, length = 150)
    private String province;

    @Column(name = "district", nullable = false, length = 150)
    private String district;

    // latitude y longitude eliminados — la ubicación se maneja con ubigeo + street
}
