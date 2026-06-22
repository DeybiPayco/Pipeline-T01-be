package com.farmagro.backend.service;

import com.farmagro.backend.dto.PurchaseDTO;
import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> findAll();
    List<PurchaseDTO> findByStatus(String status);
    PurchaseDTO findById(Integer id);
    PurchaseDTO create(PurchaseDTO dto);
    PurchaseDTO update(Integer id, PurchaseDTO dto);
    void cancel(Integer id);
}