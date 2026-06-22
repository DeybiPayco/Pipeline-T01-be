package com.farmagro.backend.service;

import com.farmagro.backend.dto.CatalogItemDTO;
import com.farmagro.backend.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    List<ProductDTO> findAllActive();
    List<ProductDTO> findAllDeleted();
    List<ProductDTO> findByCategory(Integer categoryId);
    ProductDTO findById(Integer id);
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Integer id, ProductDTO dto);
    void delete(Integer id);
    ProductDTO restore(Integer id);
    List<CatalogItemDTO> getCatalog();
}