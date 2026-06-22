package com.farmagro.backend.service.impl;

import com.farmagro.backend.dto.CustomerDTO;
import com.farmagro.backend.dto.RegisterClientDTO;
import com.farmagro.backend.dto.UserDTO;
import com.farmagro.backend.exception.BusinessException;
import com.farmagro.backend.exception.ResourceNotFoundException;
import com.farmagro.backend.model.Customer;
import com.farmagro.backend.model.Ubigeo;
import com.farmagro.backend.model.User;
import com.farmagro.backend.repository.CustomerRepository;
import com.farmagro.backend.repository.UbigeoRepository;
import com.farmagro.backend.repository.UserRepository;
import com.farmagro.backend.service.UserService;
import com.farmagro.backend.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final UbigeoRepository ubigeoRepository;
    private final CustomerServiceImpl customerServiceImpl;

    // ── Consultas ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAllActive() {
        return userRepository.findByStatus(true).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Integer id) {
        return toDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", id)));
    }

    // ── Crear usuario interno (ADMIN, VEND, ALMAC, DELIV) ────────────────────

    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        // Los clientes se crean con registerClient(), no con este método
        if ("CLIENT".equals(dto.getRole()))
            throw new BusinessException("Para registrar un cliente usa el endpoint /auth/register");

        validateUniqueEmail(dto.getEmail(), null);
        validateUniqueDoc(dto.getDocType(), dto.getDocNumber(), null);

        User entity = User.builder()
                .docType(dto.getDocType())
                .docNumber(dto.getDocNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .passwordHash(dto.getPasswordHash())
                .role(dto.getRole())
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        return toDTO(userRepository.save(entity));
    }

    // ── Registro público desde la web (CLIENT) ────────────────────────────────

    /**
     * Crea en una sola transacción:
     *   1. USERS  con role = 'CLIENT' y la contraseña proporcionada
     *   2. CUSTOMERS con street + ubigeo
     *
     * La respuesta es un CustomerDTO con todos los datos del perfil.
     */
    @Override
    @Transactional
    public CustomerDTO registerClient(RegisterClientDTO dto) {
        validateUniqueEmail(dto.getEmail(), null);
        validateUniqueDoc(dto.getDocType(), dto.getDocNumber(), null);

        Ubigeo ubigeo = ubigeoRepository.findById(dto.getUbigeoId())
                .orElseThrow(() -> new ResourceNotFoundException("el ubigeo", dto.getUbigeoId()));

        // 1. Crear el usuario con role CLIENT
        User user = User.builder()
                .docType(dto.getDocType())
                .docNumber(dto.getDocNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword())   // el FE envía la contraseña en texto plano por ahora
                .role("CLIENT")
                .status(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // 2. Crear el perfil de cliente con los datos de ubicación
        Customer customer = Customer.builder()
                .street(dto.getStreet())
                .status(true)
                .createdAt(LocalDateTime.now())
                .user(savedUser)
                .ubigeo(ubigeo)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return customerServiceImpl.toDTO(savedCustomer);
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserDTO update(Integer id, UserDTO dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", id));

        validateUniqueEmail(dto.getEmail(), id);
        validateUniqueDoc(dto.getDocType(), dto.getDocNumber(), id);

        entity.setDocType(dto.getDocType());
        entity.setDocNumber(dto.getDocNumber());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        if (dto.getPasswordHash() != null && !dto.getPasswordHash().isBlank())
            entity.setPasswordHash(dto.getPasswordHash());
        entity.setRole(dto.getRole());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        return toDTO(userRepository.save(entity));
    }

    // ── Baja lógica / restaurar ───────────────────────────────────────────────

    @Override
    @Transactional
    public void delete(Integer id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", id));

        if (Boolean.FALSE.equals(entity.getStatus()))
            throw new BusinessException("El usuario ya se encuentra inactivo");

        entity.setStatus(false);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setRestoredAt(null);
        userRepository.save(entity);
    }

    @Override
    @Transactional
    public UserDTO restore(Integer id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el usuario", id));

        if (Boolean.TRUE.equals(entity.getStatus()))
            throw new BusinessException("El usuario ya está activo");

        entity.setStatus(true);
        entity.setRestoredAt(LocalDateTime.now());
        // deleted_at se conserva para mantener el historial de auditoría
        return toDTO(userRepository.save(entity));
    }

    // ── Validaciones reutilizables ────────────────────────────────────────────

    private void validateUniqueEmail(String email, Integer excludeId) {
        if (excludeId == null) {
            if (userRepository.existsByEmailIgnoreCase(email))
                throw new BusinessException("Ya existe un usuario con el email: " + email);
        } else {
            if (userRepository.existsByEmailIgnoreCaseAndIdUserNot(email, excludeId))
                throw new BusinessException("Ya existe un usuario con el email: " + email);
        }
    }

    private void validateUniqueDoc(String docType, String docNumber, Integer excludeId) {
        if (excludeId == null) {
            if (userRepository.existsByDocTypeAndDocNumber(docType, docNumber))
                throw new BusinessException("Ya existe un usuario con el documento: "
                        + docType + " " + docNumber);
        } else {
            if (userRepository.existsByDocTypeAndDocNumberAndIdUserNot(docType, docNumber, excludeId))
                throw new BusinessException("Ya existe un usuario con el documento: "
                        + docType + " " + docNumber);
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private UserDTO toDTO(User e) {
        return UserDTO.builder()
                .idUser(e.getIdUser())
                .docType(e.getDocType())
                .docNumber(e.getDocNumber())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .phone(e.getPhone())
                .email(e.getEmail())
                .passwordHash(e.getPasswordHash())
                .role(e.getRole())
                .status(e.getStatus())
                .lastLogin(e.getLastLogin())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .deletedAt(e.getDeletedAt())
                .restoredAt(e.getRestoredAt())
                .build();
    }
}
