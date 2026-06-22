package com.farmagro.backend.service;

import com.farmagro.backend.dto.DocumentQueryDTO;

public interface DocumentQueryService {
    DocumentQueryDTO queryByDocument(String docNumber);
}
