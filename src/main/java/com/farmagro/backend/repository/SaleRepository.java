package com.farmagro.backend.repository;

import com.farmagro.backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findByStatus(String status);
    List<Sale> findByCustomerIdCustomer(Integer customerId);
    boolean existsByInvoiceNumberIgnoreCase(String invoiceNumber);
}