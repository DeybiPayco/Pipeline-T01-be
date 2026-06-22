package com.farmagro.backend.service;

import com.farmagro.backend.dto.ProductPresentationDTO;
import java.util.List;

public interface ProductPresentationService {
    List<ProductPresentationDTO> findAll();
    List<ProductPresentationDTO> findAllActive();
    List<ProductPresentationDTO> findByProduct(Integer productId);
    ProductPresentationDTO findById(Integer id);
    ProductPresentationDTO create(ProductPresentationDTO dto);
    ProductPresentationDTO update(Integer id, ProductPresentationDTO dto);
    void delete(Integer id);
}