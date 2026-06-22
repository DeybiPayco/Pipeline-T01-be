package com.farmagro.backend.repository;

import com.farmagro.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByStatus(Boolean status);
    boolean existsByNameIgnoreCase(String name);
}