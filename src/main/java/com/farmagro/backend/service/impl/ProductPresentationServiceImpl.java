package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.ProductPresentationDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.*;
import com.farmagro.backend.repository.*;
import com.farmagro.backend.service.ProductPresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPresentationServiceImpl implements ProductPresentationService {

    private final ProductPresentationRepository presentationRepository;
    private final ProductRepository productRepository;
    private final UnitMeasureRepository unitMeasureRepository;
    private final SupplierRepository supplierRepository;

    @Override @Transactional(readOnly = true)
    public List<ProductPresentationDTO> findAll() {
        return presentationRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<ProductPresentationDTO> findAllActive() {
        return presentationRepository.findAllActiveWithRelations(true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<ProductPresentationDTO> findByProduct(Integer productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("el producto", productId));
        return presentationRepository.findByProductIdProduct(productId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public ProductPresentationDTO findById(Integer id) {
        return toDTO(presentationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la presentación", id)));
    }

    @Override @Transactional
    public ProductPresentationDTO create(ProductPresentationDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("el producto", dto.getProductId()));
        UnitMeasure unit = unitMeasureRepository.findById(dto.getUnitMeasureId())
                .orElseThrow(() -> new ResourceNotFoundException("la unidad de medida", dto.getUnitMeasureId()));
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", dto.getSupplierId()));

        ProductPresentation entity = ProductPresentation.builder()
                .label(dto.getLabel())
                .content(dto.getContent())
                .purchasePrice(dto.getPurchasePrice())
                .salePrice(dto.getSalePrice())
                .currentStock(dto.getCurrentStock())
                .barcode(dto.getBarcode())
                .shelfLifeDays(dto.getShelfLifeDays())
                .minTempC(dto.getMinTempC() != null ? dto.getMinTempC() : 5.0)
                .maxTempC(dto.getMaxTempC() != null ? dto.getMaxTempC() : 35.0)
                .requiresRefrigeration(dto.getRequiresRefrigeration() != null ? dto.getRequiresRefrigeration() : false)
                .status(true)
                .product(product)
                .unitMeasure(unit)
                .supplier(supplier)
                .build();
        return toDTO(presentationRepository.save(entity));
    }

    @Override @Transactional
    public ProductPresentationDTO update(Integer id, ProductPresentationDTO dto) {
        ProductPresentation entity = presentationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la presentación", id));
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("el producto", dto.getProductId()));
        UnitMeasure unit = unitMeasureRepository.findById(dto.getUnitMeasureId())
                .orElseThrow(() -> new ResourceNotFoundException("la unidad de medida", dto.getUnitMeasureId()));
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", dto.getSupplierId()));

        entity.setLabel(dto.getLabel());
        entity.setContent(dto.getContent());
        entity.setPurchasePrice(dto.getPurchasePrice());
        entity.setSalePrice(dto.getSalePrice());
        entity.setCurrentStock(dto.getCurrentStock());
        entity.setBarcode(dto.getBarcode());
        entity.setShelfLifeDays(dto.getShelfLifeDays());
        entity.setMinTempC(dto.getMinTempC());
        entity.setMaxTempC(dto.getMaxTempC());
        entity.setRequiresRefrigeration(dto.getRequiresRefrigeration());
        entity.setStatus(dto.getStatus());
        entity.setProduct(product);
        entity.setUnitMeasure(unit);
        entity.setSupplier(supplier);
        return toDTO(presentationRepository.save(entity));
    }

    @Override @Transactional
    public void delete(Integer id) {
        ProductPresentation entity = presentationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la presentación", id));
        entity.setStatus(false);
        presentationRepository.save(entity);
    }

    private ProductPresentationDTO toDTO(ProductPresentation e) {
        return ProductPresentationDTO.builder()
                .idPresentation(e.getIdPresentation())
                .label(e.getLabel())
                .content(e.getContent())
                .purchasePrice(e.getPurchasePrice())
                .salePrice(e.getSalePrice())
                .currentStock(e.getCurrentStock())
                .barcode(e.getBarcode())
                .shelfLifeDays(e.getShelfLifeDays())
                .minTempC(e.getMinTempC())
                .maxTempC(e.getMaxTempC())
                .requiresRefrigeration(e.getRequiresRefrigeration())
                .status(e.getStatus())
                .productId(e.getProduct() != null ? e.getProduct().getIdProduct() : null)
                .productName(e.getProduct() != null ? e.getProduct().getName() : null)
                .unitMeasureId(e.getUnitMeasure() != null ? e.getUnitMeasure().getIdUnit() : null)
                .unitMeasureName(e.getUnitMeasure() != null ? e.getUnitMeasure().getName() : null)
                .supplierId(e.getSupplier() != null ? e.getSupplier().getIdSupplier() : null)
                .supplierName(e.getSupplier() != null ? e.getSupplier().getCompanyName() : null)
                .build();
    }
}