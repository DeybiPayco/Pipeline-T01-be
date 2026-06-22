package com.farmagro.backend.repository;

import com.farmagro.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByStatus(Boolean status);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByDocTypeAndDocNumber(String docType, String docNumber);

    // Para validar unicidad de documento en update (excluye el propio registro)
    boolean existsByDocTypeAndDocNumberAndIdUserNot(String docType, String docNumber, Integer idUser);

    // Para validar unicidad de email en update (excluye el propio registro)
    boolean existsByEmailIgnoreCaseAndIdUserNot(String email, Integer idUser);
}
