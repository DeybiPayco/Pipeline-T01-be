package com.farmagro.backend.repository;

import com.farmagro.backend.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByStatus(String status);
    boolean existsByInvoiceNumberIgnoreCase(String invoiceNumber);
}