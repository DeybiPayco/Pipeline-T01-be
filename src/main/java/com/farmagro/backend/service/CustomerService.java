package com.farmagro.backend.service;

import com.farmagro.backend.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findAll();
    List<CustomerDTO> findAllActive();
    CustomerDTO findById(Integer id);
    // Buscar el perfil de cliente por el id del usuario asociado
    CustomerDTO findByUserId(Integer userId);
    CustomerDTO update(Integer id, CustomerDTO dto);
    void delete(Integer id);
    CustomerDTO restore(Integer id);
}
