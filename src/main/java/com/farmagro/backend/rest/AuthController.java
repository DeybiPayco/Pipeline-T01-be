package com.farmagro.backend.rest;

import com.farmagro.backend.dto.CustomerDTO;
import com.farmagro.backend.dto.RegisterClientDTO;
import com.farmagro.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    /**
     * POST /auth/register
     *
     * Registro público desde la web.
     * Crea en una sola transacción:
     *   1. USERS  con role = 'CLIENT'
     *   2. CUSTOMERS con street + ubigeo
     *
     * Responde con el CustomerDTO completo (incluye datos del usuario asociado).
     */
    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> register(@Valid @RequestBody RegisterClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerClient(dto));
    }
}
