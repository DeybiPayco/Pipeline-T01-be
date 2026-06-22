package com.farmagro.backend.service;

import com.farmagro.backend.dto.SupplierDTO;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de proveedores.
 * <p>
 * Define las operaciones de negocio disponibles para crear, consultar,
 * actualizar, eliminar y restaurar proveedores en el sistema FarmaGro.
 * </p>
 */
public interface SupplierService {

    /**
     * Obtiene la lista completa de proveedores (activos e inactivos).
     *
     * @return lista de todos los proveedores registrados.
     */
    List<SupplierDTO> findAll();

    /**
     * Obtiene únicamente los proveedores con estado activo.
     *
     * @return lista de proveedores activos.
     */
    List<SupplierDTO> findAllActive();

    /**
     * Busca un proveedor por su identificador único.
     *
     * @param id identificador del proveedor.
     * @return DTO con los datos del proveedor encontrado.
     * @throws com.farmagro.backend.exception.ResourceNotFoundException si no existe.
     */
    SupplierDTO findById(Integer id);

    /**
     * Registra un nuevo proveedor en el sistema.
     *
     * @param dto datos del proveedor a crear.
     * @return DTO con los datos del proveedor creado, incluyendo el id generado.
     */
    SupplierDTO create(SupplierDTO dto);

    /**
     * Actualiza los datos de un proveedor existente.
     *
     * @param id  identificador del proveedor a actualizar.
     * @param dto nuevos datos del proveedor.
     * @return DTO con los datos actualizados del proveedor.
     * @throws com.farmagro.backend.exception.ResourceNotFoundException si no existe.
     */
    SupplierDTO update(Integer id, SupplierDTO dto);

    /**
     * Realiza la eliminación lógica de un proveedor (cambia su estado a inactivo).
     *
     * @param id identificador del proveedor a eliminar.
     * @throws com.farmagro.backend.exception.ResourceNotFoundException si no existe.
     */
    void delete(Integer id);

    /**
     * Restaura un proveedor previamente eliminado de forma lógica.
     *
     * @param id identificador del proveedor a restaurar.
     * @return DTO con los datos del proveedor restaurado.
     * @throws com.farmagro.backend.exception.ResourceNotFoundException si no existe.
     */
    SupplierDTO restore(Integer id);
}
