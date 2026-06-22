package com.farmagro.backend.service;

import com.farmagro.backend.dto.DeliveryDTO;
import java.util.List;

public interface DeliveryService {
    List<DeliveryDTO> findAll();
    List<DeliveryDTO> findByStatus(String status);
    List<DeliveryDTO> findBySale(Integer saleId);
    DeliveryDTO findById(Integer id);
    DeliveryDTO create(DeliveryDTO dto);
    DeliveryDTO update(Integer id, DeliveryDTO dto);
    void cancel(Integer id);
}