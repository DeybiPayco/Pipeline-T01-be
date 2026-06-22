package com.farmagro.backend.repository;

import com.farmagro.backend.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Supplier}.
 * <p>
 * Proporciona operaciones CRUD estándar heredadas de {@link JpaRepository}
 * y consultas personalizadas para filtrar y validar proveedores.
 * </p>
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    /**
     * Obtiene la lista de proveedores filtrados por su estado.
     *
     * @param status {@code true} para proveedores activos, {@code false} para eliminados.
     * @return lista de proveedores con el estado indicado.
     */
    List<Supplier> findByStatus(Boolean status);

    /**
     * Verifica si existe un proveedor con el email indicado (sin distinción de mayúsculas).
     *
     * @param email correo electrónico a verificar.
     * @return {@code true} si ya existe un proveedor con ese email.
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Verifica si existe un proveedor con el tipo y número de documento indicados.
     *
     * @param docType   tipo de documento (RUC, DNI o CE).
     * @param docNumber número de documento.
     * @return {@code true} si ya existe un proveedor con esa combinación.
     */
    boolean existsByDocTypeAndDocNumber(String docType, String docNumber);

    /**
     * Verifica si existe otro proveedor con el mismo email, excluyendo al proveedor con el id dado.
     * Utilizado para validaciones en actualizaciones.
     *
     * @param email      correo electrónico a verificar.
     * @param idSupplier id del proveedor que se está actualizando (excluido de la búsqueda).
     * @return {@code true} si otro proveedor ya usa ese email.
     */
    boolean existsByEmailIgnoreCaseAndIdSupplierNot(String email, Integer idSupplier);

    /**
     * Verifica si existe otro proveedor con el mismo tipo y número de documento, excluyendo
     * al proveedor con el id dado. Utilizado para validaciones en actualizaciones.
     *
     * @param docType    tipo de documento.
     * @param docNumber  número de documento.
     * @param idSupplier id del proveedor que se está actualizando (excluido de la búsqueda).
     * @return {@code true} si otro proveedor ya usa esa combinación de documento.
     */
    boolean existsByDocTypeAndDocNumberAndIdSupplierNot(String docType, String docNumber, Integer idSupplier);
}
