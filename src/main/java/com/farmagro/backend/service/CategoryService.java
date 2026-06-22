package com.farmagro.backend.service;

import com.farmagro.backend.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    List<CategoryDTO> findAllActive();
    CategoryDTO findById(Integer id);
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(Integer id, CategoryDTO dto);
    void delete(Integer id);
    CategoryDTO restore(Integer id);  // ← nuevo
}