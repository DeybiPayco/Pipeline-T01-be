package com.farmagro.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentQueryDTO {

    private String documentId;
    private String names;
    private String paternalLastName;
    private String maternalLastName;
    private String fullName;
}
