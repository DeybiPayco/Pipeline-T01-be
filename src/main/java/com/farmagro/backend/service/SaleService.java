package com.farmagro.backend.service;

import com.farmagro.backend.dto.SaleDTO;
import java.util.List;

public interface SaleService {
    List<SaleDTO> findAll();
    List<SaleDTO> findByStatus(String status);
    List<SaleDTO> findByCustomer(Integer customerId);
    SaleDTO findById(Integer id);
    SaleDTO create(SaleDTO dto);
    SaleDTO update(Integer id, SaleDTO dto);
    void cancel(Integer id);
}