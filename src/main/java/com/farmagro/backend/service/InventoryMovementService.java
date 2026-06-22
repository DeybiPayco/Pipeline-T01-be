package com.farmagro.backend.service;

import com.farmagro.backend.dto.InventoryMovementDTO;
import java.util.List;

public interface InventoryMovementService {
    List<InventoryMovementDTO> findAll();
    List<InventoryMovementDTO> findByPresentation(Integer presentationId);
    List<InventoryMovementDTO> findByType(String type);
    InventoryMovementDTO findById(Integer id);
}