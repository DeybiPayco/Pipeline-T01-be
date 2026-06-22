package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.SaleDTO;
import com.farmagro.backend.dto.SaleDetailDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.*;
import com.farmagro.backend.repository.*;
import com.farmagro.backend.service.EmailService;
import com.farmagro.backend.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductPresentationRepository presentationRepository;
    private final InventoryMovementRepository movementRepository;
    private final EmailService emailService;

    @Override @Transactional(readOnly = true)
    public List<SaleDTO> findAll() {
        return saleRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<SaleDTO> findByStatus(String status) {
        return saleRepository.findByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<SaleDTO> findByCustomer(Integer customerId) {
        return saleRepository.findByCustomerIdCustomer(customerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public SaleDTO findById(Integer id) {
        return toDTO(saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la venta", id)));
    }

    @Override @Transactional
    public SaleDTO create(SaleDTO dto) {
        if (saleRepository.existsByInvoiceNumberIgnoreCase(dto.getInvoiceNumber()))
            throw new BusinessException("Ya existe una venta con el número de factura: " + dto.getInvoiceNumber());

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", dto.getCustomerId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", dto.getUserId()));

        // Construir detalles, validar stock y calcular totales
        List<SaleDetail> details = dto.getDetails().stream().map(d -> {
            ProductPresentation presentation = presentationRepository.findById(d.getPresentationId())
                    .orElseThrow(() -> new ResourceNotFoundException("la presentación", d.getPresentationId()));
            if (presentation.getCurrentStock() < d.getQuantity())
                throw new BusinessException("Stock insuficiente para: " + presentation.getLabel()
                        + ". Stock disponible: " + presentation.getCurrentStock());
            BigDecimal sub = d.getUnitPrice()
                    .multiply(BigDecimal.valueOf(d.getQuantity()))
                    .subtract(d.getItemDiscount());
            return SaleDetail.builder()
                    .quantity(d.getQuantity())
                    .unitPrice(d.getUnitPrice())
                    .itemDiscount(d.getItemDiscount())
                    .subtotal(sub)
                    .batchNumber(d.getBatchNumber())
                    .presentation(presentation)
                    .build();
        }).collect(Collectors.toList());

        BigDecimal subtotal = details.stream()
                .map(SaleDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscount = details.stream()
                .map(SaleDetail::getItemDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal taxAmount = subtotal.multiply(BigDecimal.valueOf(0.18));

        Sale sale = Sale.builder()
                .saleDate(dto.getSaleDate())
                .invoiceNumber(dto.getInvoiceNumber())
                .paymentMethod(dto.getPaymentMethod())
                .subtotal(subtotal.add(totalDiscount))
                .discount(totalDiscount)
                .taxAmount(taxAmount)
                .total(subtotal.add(taxAmount))
                .status("PENDIENTE")
                .notes(dto.getNotes())
                .salesChannel(dto.getSalesChannel())
                .customer(customer)
                .user(user)
                .details(details)
                .build();

        details.forEach(d -> d.setSale(sale));
        Sale saved = saleRepository.save(sale);

        // Descontar stock y registrar movimientos de inventario (SALIDA)
        List<ProductPresentation> lowStockItems = new ArrayList<>();
        saved.getDetails().forEach(d -> {
            ProductPresentation pres = d.getPresentation();
            int before = pres.getCurrentStock();
            int after = before - d.getQuantity();
            pres.setCurrentStock(after);
            presentationRepository.save(pres);

            movementRepository.save(InventoryMovement.builder()
                    .idReference(saved.getIdSale())
                    .movementType("SALIDA")
                    .quantity(d.getQuantity())
                    .stockBefore(before)
                    .stockAfter(after)
                    .reason("Venta " + saved.getInvoiceNumber())
                    .presentation(pres)
                    .user(user)
                    .build());

            // Verificar si el stock bajó al umbral mínimo
            if (after <= LOW_STOCK_THRESHOLD) {
                lowStockItems.add(pres);
            }
        });

        // Enviar alerta por correo si hay productos con stock bajo
        if (!lowStockItems.isEmpty()) {
            emailService.sendLowStockAlert(lowStockItems);
        }

        return toDTO(saved);
    }

    @Override @Transactional
    public SaleDTO update(Integer id, SaleDTO dto) {
        Sale entity = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la venta", id));
        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("No se puede editar una venta anulada");
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNotes(dto.getNotes());
        entity.setStatus(dto.getStatus());
        entity.setSalesChannel(dto.getSalesChannel());
        return toDTO(saleRepository.save(entity));
    }

    @Override @Transactional
    public void cancel(Integer id) {
        Sale entity = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la venta", id));
        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("La venta ya está anulada");
        entity.setStatus("ANULADO");
        saleRepository.save(entity);
    }

    private SaleDTO toDTO(Sale e) {
        List<SaleDetailDTO> details = e.getDetails() == null ? List.of() :
                e.getDetails().stream().map(d -> SaleDetailDTO.builder()
                        .idSaleDetail(d.getIdSaleDetail())
                        .quantity(d.getQuantity())
                        .unitPrice(d.getUnitPrice())
                        .itemDiscount(d.getItemDiscount())
                        .subtotal(d.getSubtotal())
                        .batchNumber(d.getBatchNumber())
                        .presentationId(d.getPresentation() != null ? d.getPresentation().getIdPresentation() : null)
                        .presentationLabel(d.getPresentation() != null ? d.getPresentation().getLabel() : null)
                        .build()).collect(Collectors.toList());

        return SaleDTO.builder()
                .idSale(e.getIdSale())
                .saleDate(e.getSaleDate())
                .invoiceNumber(e.getInvoiceNumber())
                .paymentMethod(e.getPaymentMethod())
                .subtotal(e.getSubtotal())
                .discount(e.getDiscount())
                .taxAmount(e.getTaxAmount())
                .total(e.getTotal())
                .status(e.getStatus())
                .notes(e.getNotes())
                .salesChannel(e.getSalesChannel())
                .customerId(e.getCustomer() != null ? e.getCustomer().getIdCustomer() : null)
                .customerName(e.getCustomer() != null && e.getCustomer().getUser() != null
                        ? e.getCustomer().getUser().getFirstName() + " " + e.getCustomer().getUser().getLastName()
                        : null)
                .userId(e.getUser() != null ? e.getUser().getIdUser() : null)
                .userName(e.getUser() != null ? e.getUser().getFirstName() + " " + e.getUser().getLastName() : null)
                .details(details)
                .build();
    }
}