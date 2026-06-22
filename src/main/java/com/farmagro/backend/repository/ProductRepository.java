package com.farmagro.backend.repository;

import com.farmagro.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByStatus(Boolean status);
    List<Product> findByCategoryIdCategory(Integer categoryId);
    boolean existsBySanitaryRegistrationIgnoreCase(String sanitaryRegistration);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdProductNot(String name, Integer idProduct);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.status = :status")
    List<Product> findAllActiveWithCategory(@Param("status") Boolean status);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.status = false ORDER BY p.deletedAt DESC")
    List<Product> findAllDeleted();
}