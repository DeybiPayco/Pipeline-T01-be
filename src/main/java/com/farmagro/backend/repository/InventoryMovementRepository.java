package com.farmagro.backend.repository;

import com.farmagro.backend.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Integer> {
    List<InventoryMovement> findByMovementType(String movementType);
    List<InventoryMovement> findByPresentationIdPresentation(Integer presentationId);
}