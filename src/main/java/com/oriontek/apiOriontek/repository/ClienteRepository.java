package com.oriontek.apiOriontek.repository;

import com.oriontek.apiOriontek.domain.ClienteDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteDomain, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<ClienteDomain> findByEmail(String email);


    @Query("""
        SELECT c FROM ClienteDomain c
        WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', CAST(:nombre AS string), '%')))
          AND (:email  IS NULL OR LOWER(c.email)  LIKE LOWER(CONCAT('%', CAST(:email  AS string), '%')))
          AND (:activo IS NULL OR c.activo = :activo)
        ORDER BY c.nombre ASC
        """)
    Page<ClienteDomain> findAllWithFilters(
            @Param("nombre") String nombre,
            @Param("email")  String email,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}