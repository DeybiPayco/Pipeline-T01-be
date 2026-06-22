package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.PurchaseDTO;
import com.farmagro.backend.dto.PurchaseDetailDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.*;
import com.farmagro.backend.repository.*;
import com.farmagro.backend.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProductPresentationRepository presentationRepository;
    private final InventoryMovementRepository movementRepository;

    @Override @Transactional(readOnly = true)
    public List<PurchaseDTO> findAll() {
        return purchaseRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<PurchaseDTO> findByStatus(String status) {
        return purchaseRepository.findByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public PurchaseDTO findById(Integer id) {
        return toDTO(purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la compra", id)));
    }

    @Override @Transactional
    public PurchaseDTO create(PurchaseDTO dto) {
        if (purchaseRepository.existsByInvoiceNumberIgnoreCase(dto.getInvoiceNumber()))
            throw new BusinessException("Ya existe una compra con el número de factura: " + dto.getInvoiceNumber());

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", dto.getSupplierId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", dto.getUserId()));

        // Construir detalles y calcular subtotal
        List<PurchaseDetail> details = dto.getDetails().stream().map(d -> {
            ProductPresentation presentation = presentationRepository.findById(d.getPresentationId())
                    .orElseThrow(() -> new ResourceNotFoundException("la presentación", d.getPresentationId()));
            BigDecimal sub = d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity()));
            return PurchaseDetail.builder()
                    .quantity(d.getQuantity())
                    .unitPrice(d.getUnitPrice())
                    .subtotal(sub)
                    .batchNumber(d.getBatchNumber())
                    .manufactureDate(d.getManufactureDate())
                    .expiryDate(d.getExpiryDate())
                    .presentation(presentation)
                    .build();
        }).collect(Collectors.toList());

        BigDecimal subtotal = details.stream()
                .map(PurchaseDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal igv = subtotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal total = subtotal.add(igv);

        Purchase purchase = Purchase.builder()
                .purchaseDate(dto.getPurchaseDate())
                .invoiceNumber(dto.getInvoiceNumber())
                .expectedDeliveryDate(dto.getExpectedDeliveryDate())
                .subtotal(subtotal)
                .taxAmount(igv)
                .total(total)
                .status("PENDIENTE")
                .notes(dto.getNotes())
                .supplier(supplier)
                .user(user)
                .details(details)
                .build();

        details.forEach(d -> d.setPurchase(purchase));
        Purchase saved = purchaseRepository.save(purchase);

        // Registrar movimientos de inventario (ENTRADA) por cada detalle
        saved.getDetails().forEach(d -> {
            ProductPresentation pres = d.getPresentation();
            int before = pres.getCurrentStock();
            int after = before + d.getQuantity();
            pres.setCurrentStock(after);
            presentationRepository.save(pres);

            movementRepository.save(InventoryMovement.builder()
                    .idReference(saved.getIdPurchase())
                    .movementType("ENTRADA")
                    .quantity(d.getQuantity())
                    .stockBefore(before)
                    .stockAfter(after)
                    .reason("Ingreso por compra " + saved.getInvoiceNumber())
                    .presentation(pres)
                    .user(user)
                    .build());
        });

        return toDTO(saved);
    }

    @Override @Transactional
    public PurchaseDTO update(Integer id, PurchaseDTO dto) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la compra", id));
        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("No se puede editar una compra anulada");

        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setNotes(dto.getNotes());
        entity.setStatus(dto.getStatus());
        return toDTO(purchaseRepository.save(entity));
    }

    @Override @Transactional
    public void cancel(Integer id) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la compra", id));
        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("La compra ya está anulada");
        entity.setStatus("ANULADO");
        purchaseRepository.save(entity);
    }

    private PurchaseDTO toDTO(Purchase e) {
        List<PurchaseDetailDTO> details = e.getDetails() == null ? List.of() :
                e.getDetails().stream().map(d -> PurchaseDetailDTO.builder()
                        .idPurchaseDetail(d.getIdPurchaseDetail())
                        .quantity(d.getQuantity())
                        .unitPrice(d.getUnitPrice())
                        .subtotal(d.getSubtotal())
                        .batchNumber(d.getBatchNumber())
                        .manufactureDate(d.getManufactureDate())
                        .expiryDate(d.getExpiryDate())
                        .presentationId(d.getPresentation() != null ? d.getPresentation().getIdPresentation() : null)
                        .presentationLabel(d.getPresentation() != null ? d.getPresentation().getLabel() : null)
                        .build()).collect(Collectors.toList());

        return PurchaseDTO.builder()
                .idPurchase(e.getIdPurchase())
                .purchaseDate(e.getPurchaseDate())
                .invoiceNumber(e.getInvoiceNumber())
                .expectedDeliveryDate(e.getExpectedDeliveryDate())
                .subtotal(e.getSubtotal())
                .taxAmount(e.getTaxAmount())
                .total(e.getTotal())
                .status(e.getStatus())
                .notes(e.getNotes())
                .supplierId(e.getSupplier() != null ? e.getSupplier().getIdSupplier() : null)
                .supplierName(e.getSupplier() != null ? e.getSupplier().getCompanyName() : null)
                .userId(e.getUser() != null ? e.getUser().getIdUser() : null)
                .userName(e.getUser() != null ? e.getUser().getFirstName() + " " + e.getUser().getLastName() : null)
                .details(details)
                .build();
    }
}