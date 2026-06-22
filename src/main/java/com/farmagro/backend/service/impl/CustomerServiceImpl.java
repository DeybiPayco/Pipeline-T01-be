package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.CustomerDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Customer;
import com.farmagro.backend.model.Ubigeo;
import com.farmagro.backend.repository.CustomerRepository;
import com.farmagro.backend.repository.UbigeoRepository;
import com.farmagro.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UbigeoRepository ubigeoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAllActive() {
        return customerRepository.findAllActiveWithDetails(true)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO findById(Integer id) {
        return toDTO(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO findByUserId(Integer userId) {
        return toDTO(customerRepository.findByUserIdUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("el perfil de cliente para el usuario", userId)));
    }

    /**
     * Actualiza solo los datos propios del cliente: street y ubigeo.
     * Los datos personales (nombre, doc, email, etc.) se actualizan
     * a través de UserService.update().
     */
    @Override
    @Transactional
    public CustomerDTO update(Integer id, CustomerDTO dto) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", id));

        Ubigeo ubigeo = ubigeoRepository.findById(dto.getUbigeoId())
                .orElseThrow(() -> new ResourceNotFoundException("el ubigeo", dto.getUbigeoId()));

        entity.setStreet(dto.getStreet());
        entity.setUbigeo(ubigeo);
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(customerRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", id));

        if (Boolean.FALSE.equals(entity.getStatus()))
            throw new BusinessException("El cliente ya se encuentra inactivo");

        entity.setStatus(false);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setRestoredAt(null);
        customerRepository.save(entity);
    }

    @Override
    @Transactional
    public CustomerDTO restore(Integer id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el cliente", id));

        if (Boolean.TRUE.equals(entity.getStatus()))
            throw new BusinessException("El cliente ya se encuentra activo");

        entity.setStatus(true);
        entity.setRestoredAt(LocalDateTime.now());
        entity.setDeletedAt(null);
        return toDTO(customerRepository.save(entity));
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    public CustomerDTO toDTO(Customer e) {
        String ubigeoDisplay = null;
        if (e.getUbigeo() != null) {
            Ubigeo u = e.getUbigeo();
            ubigeoDisplay = u.getDepartament() + " - " + u.getProvince() + " - " + u.getDistrict();
        }
        return CustomerDTO.builder()
                .idCustomer(e.getIdCustomer())
                .street(e.getStreet())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .ubigeoId(e.getUbigeo() != null ? e.getUbigeo().getIdUbigeo() : null)
                .ubigeoDisplay(ubigeoDisplay)
                // Datos del usuario asociado (solo lectura)
                .userId(e.getUser() != null ? e.getUser().getIdUser() : null)
                .docType(e.getUser() != null ? e.getUser().getDocType() : null)
                .docNumber(e.getUser() != null ? e.getUser().getDocNumber() : null)
                .firstName(e.getUser() != null ? e.getUser().getFirstName() : null)
                .lastName(e.getUser() != null ? e.getUser().getLastName() : null)
                .phone(e.getUser() != null ? e.getUser().getPhone() : null)
                .email(e.getUser() != null ? e.getUser().getEmail() : null)
                .build();
    }
}
