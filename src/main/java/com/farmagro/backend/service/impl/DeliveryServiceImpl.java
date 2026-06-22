package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.DeliveryDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Delivery;
import com.farmagro.backend.model.Sale;
import com.farmagro.backend.model.User;
import com.farmagro.backend.repository.DeliveryRepository;
import com.farmagro.backend.repository.SaleRepository;
import com.farmagro.backend.repository.UserRepository;
import com.farmagro.backend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> findByStatus(String status) {
        return deliveryRepository.findByStatus(status).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDTO> findBySale(Integer saleId) {
        return deliveryRepository.findBySaleIdSale(saleId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDTO findById(Integer id) {
        return toDTO(deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la entrega", id)));
    }

    @Override
    @Transactional
    public DeliveryDTO create(DeliveryDTO dto) {
        Sale sale = saleRepository.findById(dto.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("la venta", dto.getSaleId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", dto.getUserId()));

        Delivery entity = Delivery.builder()
                .deliveryType(dto.getDeliveryType())
                .reference(dto.getReference())
                .deliveryAddress(dto.getDeliveryAddress())
                // deliveryLat y deliveryLon eliminados
                .scheduledDate(dto.getScheduledDate())
                .actualDate(dto.getActualDate())
                .status("PENDIENTE")
                .notes(dto.getNotes())
                .sale(sale)
                .user(user)
                .build();

        return toDTO(deliveryRepository.save(entity));
    }

    @Override
    @Transactional
    public DeliveryDTO update(Integer id, DeliveryDTO dto) {
        Delivery entity = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la entrega", id));

        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("No se puede editar una entrega anulada");

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", dto.getUserId()));

        entity.setDeliveryType(dto.getDeliveryType());
        entity.setReference(dto.getReference());
        entity.setDeliveryAddress(dto.getDeliveryAddress());
        // deliveryLat y deliveryLon eliminados
        entity.setScheduledDate(dto.getScheduledDate());
        entity.setActualDate(dto.getActualDate());
        entity.setStatus(dto.getStatus());
        entity.setNotes(dto.getNotes());
        entity.setUser(user);

        return toDTO(deliveryRepository.save(entity));
    }

    @Override
    @Transactional
    public void cancel(Integer id) {
        Delivery entity = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("la entrega", id));

        if ("ANULADO".equals(entity.getStatus()))
            throw new BusinessException("La entrega ya está anulada");

        entity.setStatus("ANULADO");
        deliveryRepository.save(entity);
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private DeliveryDTO toDTO(Delivery e) {
        return DeliveryDTO.builder()
                .idDelivery(e.getIdDelivery())
                .deliveryType(e.getDeliveryType())
                .reference(e.getReference())
                .deliveryAddress(e.getDeliveryAddress())
                // deliveryLat y deliveryLon eliminados
                .scheduledDate(e.getScheduledDate())
                .actualDate(e.getActualDate())
                .status(e.getStatus())
                .notes(e.getNotes())
                .saleId(e.getSale() != null ? e.getSale().getIdSale() : null)
                .saleInvoice(e.getSale() != null ? e.getSale().getInvoiceNumber() : null)
                .userId(e.getUser() != null ? e.getUser().getIdUser() : null)
                .userName(e.getUser() != null
                        ? e.getUser().getFirstName() + " " + e.getUser().getLastName()
                        : null)
                .build();
    }
}
