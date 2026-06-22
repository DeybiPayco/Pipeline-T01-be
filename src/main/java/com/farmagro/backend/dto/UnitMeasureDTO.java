package com.farmagro.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitMeasureDTO {

    private Integer idUnit;
    private String name;
    private String abbreviation;
}