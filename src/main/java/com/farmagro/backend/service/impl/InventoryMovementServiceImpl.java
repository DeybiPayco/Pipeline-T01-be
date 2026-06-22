package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.InventoryMovementDTO;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.InventoryMovement;
import com.farmagro.backend.repository.InventoryMovementRepository;
import com.farmagro.backend.service.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryMovementServiceImpl implements InventoryMovementService {

    private final InventoryMovementRepository movementRepository;

    @Override @Transactional(readOnly = true)
    public List<InventoryMovementDTO> findAll() {
        return movementRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<InventoryMovementDTO> findByPresentation(Integer presentationId) {
        return movementRepository.findByPresentationIdPresentation(presentationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<InventoryMovementDTO> findByType(String type) {
        return movementRepository.findByMovementType(type)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public InventoryMovementDTO findById(Integer id) {
        return toDTO(movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el movimiento", id)));
    }

    private InventoryMovementDTO toDTO(InventoryMovement e) {
        return InventoryMovementDTO.builder()
                .idMovement(e.getIdMovement())
                .idReference(e.getIdReference())
                .movementType(e.getMovementType())
                .quantity(e.getQuantity())
                .stockBefore(e.getStockBefore())
                .stockAfter(e.getStockAfter())
                .reason(e.getReason())
                .unitCost(e.getUnitCost())
                .batchNumber(e.getBatchNumber())
                .expiryDate(e.getExpiryDate())
                .movementDate(e.getMovementDate())
                .presentationId(e.getPresentation() != null ? e.getPresentation().getIdPresentation() : null)
                .presentationLabel(e.getPresentation() != null ? e.getPresentation().getLabel() : null)
                .userId(e.getUser() != null ? e.getUser().getIdUser() : null)
                .userName(e.getUser() != null ? e.getUser().getFirstName() + " " + e.getUser().getLastName() : null)
                .build();
    }
}