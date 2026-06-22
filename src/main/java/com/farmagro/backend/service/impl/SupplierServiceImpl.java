package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.SupplierDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Supplier;
import com.farmagro.backend.model.Ubigeo;
import com.farmagro.backend.repository.SupplierRepository;
import com.farmagro.backend.repository.UbigeoRepository;
import com.farmagro.backend.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final UbigeoRepository ubigeoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findAll() {
        return supplierRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findAllActive() {
        return supplierRepository.findByStatus(true).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierDTO findById(Integer id) {
        return toDTO(supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", id)));
    }

    @Override
    @Transactional
    public SupplierDTO create(SupplierDTO dto) {
        if (supplierRepository.existsByEmailIgnoreCase(dto.getEmail()))
            throw new BusinessException("Ya existe un proveedor con el email: " + dto.getEmail());
        if (supplierRepository.existsByDocTypeAndDocNumber(dto.getDocType(), dto.getDocNumber()))
            throw new BusinessException("Ya existe un proveedor con el documento: "
                    + dto.getDocType() + " " + dto.getDocNumber());

        Ubigeo ubigeo = ubigeoRepository.findById(dto.getUbigeoId())
                .orElseThrow(() -> new ResourceNotFoundException("el ubigeo", dto.getUbigeoId()));

        Supplier entity = Supplier.builder()
                .docType(dto.getDocType())
                .docNumber(dto.getDocNumber())
                .companyName(dto.getCompanyName())
                .contactPerson(dto.getContactPerson())
                .phoneContact(dto.getPhoneContact())
                .email(dto.getEmail())
                .street(dto.getStreet())
                .certifications(dto.getCertifications())
                .status(true)
                .ubigeo(ubigeo)
                .createdAt(LocalDateTime.now())
                .build();

        return toDTO(supplierRepository.save(entity));
    }

    @Override
    @Transactional
    public SupplierDTO update(Integer id, SupplierDTO dto) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", id));

        if (supplierRepository.existsByEmailIgnoreCaseAndIdSupplierNot(dto.getEmail(), id))
            throw new BusinessException("Ya existe un proveedor con el email: " + dto.getEmail());
        if (supplierRepository.existsByDocTypeAndDocNumberAndIdSupplierNot(dto.getDocType(), dto.getDocNumber(), id))
            throw new BusinessException("Ya existe un proveedor con el documento: "
                    + dto.getDocType() + " " + dto.getDocNumber());

        Ubigeo ubigeo = ubigeoRepository.findById(dto.getUbigeoId())
                .orElseThrow(() -> new ResourceNotFoundException("el ubigeo", dto.getUbigeoId()));

        entity.setDocType(dto.getDocType());
        entity.setDocNumber(dto.getDocNumber());
        entity.setCompanyName(dto.getCompanyName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setPhoneContact(dto.getPhoneContact());
        entity.setEmail(dto.getEmail());
        entity.setStreet(dto.getStreet());
        entity.setCertifications(dto.getCertifications());
        entity.setUbigeo(ubigeo);
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(supplierRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", id));
        if (Boolean.FALSE.equals(entity.getStatus()))
            throw new BusinessException("El proveedor ya está inactivo");
        entity.setStatus(false);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setRestoredAt(null);
        supplierRepository.save(entity);
    }

    @Override
    @Transactional
    public SupplierDTO restore(Integer id) {
        Supplier entity = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el proveedor", id));
        if (Boolean.TRUE.equals(entity.getStatus()))
            throw new BusinessException("El proveedor ya está activo");
        entity.setStatus(true);
        entity.setRestoredAt(LocalDateTime.now());
        entity.setDeletedAt(null);
        return toDTO(supplierRepository.save(entity));
    }

    private SupplierDTO toDTO(Supplier e) {
        String ubigeoDisplay = null;
        if (e.getUbigeo() != null) {
            Ubigeo u = e.getUbigeo();
            ubigeoDisplay = u.getDepartament() + " - " + u.getProvince() + " - " + u.getDistrict();
        }
        return SupplierDTO.builder()
                .idSupplier(e.getIdSupplier())
                .docType(e.getDocType())
                .docNumber(e.getDocNumber())
                .companyName(e.getCompanyName())
                .contactPerson(e.getContactPerson())
                .phoneContact(e.getPhoneContact())
                .email(e.getEmail())
                .street(e.getStreet())
                .certifications(e.getCertifications())
                .status(e.getStatus())
                .ubigeoId(e.getUbigeo() != null ? e.getUbigeo().getIdUbigeo() : null)
                .ubigeoDisplay(ubigeoDisplay)
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .build();
    }
}
