package com.farmagro.backend.rest;

import com.farmagro.backend.dto.CustomerDTO;
import com.farmagro.backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDTO>> getAllActive() {
        return ResponseEntity.ok(customerService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    // Obtener el perfil de cliente por el id del usuario asociado
    // Útil para que el cliente vea su propio perfil tras iniciar sesión
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CustomerDTO> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(customerService.findByUserId(userId));
    }

    // Solo actualiza street y ubigeo — los datos personales van por PUT /users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<CustomerDTO> restore(@PathVariable Integer id) {
        return ResponseEntity.ok(customerService.restore(id));
    }
}
