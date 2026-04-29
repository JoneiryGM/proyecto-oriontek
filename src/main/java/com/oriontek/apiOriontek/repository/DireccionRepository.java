package com.oriontek.apiOriontek.repository;

import com.oriontek.apiOriontek.domain.DireccionDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<DireccionDomain, Long> {

    List<DireccionDomain> findByClienteDomain_Id(Long clienteId);

    Optional<DireccionDomain> findByIdAndClienteDomain_Id(Long id, Long clienteId);

    boolean existsByClienteDomainIdAndPrincipalTrue(Long clienteId);

    long countByClienteDomain_Id(Long clienteId);
}