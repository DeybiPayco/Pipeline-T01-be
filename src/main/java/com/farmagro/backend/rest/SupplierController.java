package com.farmagro.backend.rest;

import com.farmagro.backend.dto.SupplierDTO;
import com.farmagro.backend.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de proveedores.
 * <p>
 * Expone los endpoints HTTP bajo la ruta base {@code /suppliers} y delega
 * la lógica de negocio al servicio {@link SupplierService}.
 * </p>
 */
@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {

    /** Servicio de negocio para operaciones sobre proveedores. */
    private final SupplierService supplierService;

    /**
     * Obtiene la lista completa de proveedores (activos e inactivos).
     *
     * @return {@code 200 OK} con la lista de todos los proveedores.
     */
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAll() {
        return ResponseEntity.ok(supplierService.findAll());
    }

    /**
     * Obtiene únicamente los proveedores con estado activo.
     *
     * @return {@code 200 OK} con la lista de proveedores activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<SupplierDTO>> getAllActive() {
        return ResponseEntity.ok(supplierService.findAllActive());
    }

    /**
     * Obtiene un proveedor por su identificador.
     *
     * @param id identificador del proveedor.
     * @return {@code 200 OK} con los datos del proveedor, o {@code 404} si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplierService.findById(id));
    }

    /**
     * Crea un nuevo proveedor en el sistema.
     *
     * @param dto datos del proveedor a registrar (validados con {@code @Valid}).
     * @return {@code 201 Created} con los datos del proveedor creado.
     */
    @PostMapping
    public ResponseEntity<SupplierDTO> create(@Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(dto));
    }

    /**
     * Actualiza los datos de un proveedor existente.
     *
     * @param id  identificador del proveedor a actualizar.
     * @param dto nuevos datos del proveedor (validados con {@code @Valid}).
     * @return {@code 200 OK} con los datos actualizados, o {@code 404} si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable Integer id, @Valid @RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.update(id, dto));
    }

    /**
     * Elimina lógicamente un proveedor (cambia su estado a inactivo).
     *
     * @param id identificador del proveedor a eliminar.
     * @return {@code 204 No Content} si se eliminó correctamente, o {@code 404} si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un proveedor previamente eliminado de forma lógica.
     *
     * @param id identificador del proveedor a restaurar.
     * @return {@code 200 OK} con los datos del proveedor restaurado, o {@code 404} si no existe.
     */
    @PatchMapping("/{id}/restore")
    public ResponseEntity<SupplierDTO> restore(@PathVariable Integer id) {
        return ResponseEntity.ok(supplierService.restore(id));
    }
}
