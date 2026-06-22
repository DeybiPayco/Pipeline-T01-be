package com.farmagro.backend.repository;

import com.farmagro.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByStatus(Boolean status);

    // Buscar el perfil de cliente por el id del usuario asociado (relación 1:1)
    Optional<Customer> findByUserIdUser(Integer idUser);

    // Verificar si ya existe un perfil de cliente para ese usuario
    boolean existsByUserIdUser(Integer idUser);

    @Query("SELECT c FROM Customer c JOIN FETCH c.user JOIN FETCH c.ubigeo WHERE c.status = :status")
    List<Customer> findAllActiveWithDetails(@Param("status") Boolean status);
}
