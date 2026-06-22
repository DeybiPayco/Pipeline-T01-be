package com.farmagro.backend.service;

import com.farmagro.backend.dto.CustomerDTO;
import com.farmagro.backend.dto.RegisterClientDTO;
import com.farmagro.backend.dto.UserDTO;
import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    List<UserDTO> findAllActive();
    UserDTO findById(Integer id);
    // Crear usuario interno (ADMIN, VEND, ALMAC, DELIV) — solo desde el panel admin
    UserDTO create(UserDTO dto);
    UserDTO update(Integer id, UserDTO dto);
    void delete(Integer id);
    UserDTO restore(Integer id);
    // Registro público desde la web — crea User (CLIENT) + Customer en una transacción
    CustomerDTO registerClient(RegisterClientDTO dto);
}
