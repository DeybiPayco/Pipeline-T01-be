package com.farmagro.backend.repository;

import com.farmagro.backend.model.ProductPresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductPresentationRepository extends JpaRepository<ProductPresentation, Integer> {
    List<ProductPresentation> findByStatus(Boolean status);
    List<ProductPresentation> findByProductIdProduct(Integer productId);

    @Query("SELECT pp FROM ProductPresentation pp " +
            "JOIN FETCH pp.product JOIN FETCH pp.unitMeasure JOIN FETCH pp.supplier " +
            "WHERE pp.status = :status")
    List<ProductPresentation> findAllActiveWithRelations(@Param("status") Boolean status);
}