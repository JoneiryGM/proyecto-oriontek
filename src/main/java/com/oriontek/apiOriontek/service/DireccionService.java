package com.oriontek.apiOriontek.service;


import com.oriontek.apiOriontek.config.BusinessException;
import com.oriontek.apiOriontek.config.ResourceNotFoundException;
import com.oriontek.apiOriontek.domain.ClienteDomain;
import com.oriontek.apiOriontek.domain.DireccionDomain;
import com.oriontek.apiOriontek.dto.request.DireccionRequest;
import com.oriontek.apiOriontek.dto.response.DireccionResponse;
import com.oriontek.apiOriontek.repository.ClienteRepository;
import com.oriontek.apiOriontek.repository.DireccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DireccionService{

    private final DireccionRepository direccionRepository;
    private final ClienteRepository clienteRepository;

    // instancia auxiliar solo para invocar TransformFrom
    private final DireccionDomain direccionFactory = new DireccionDomain();


    public List<DireccionResponse> findByClienteId(Long clienteId) {
        assertClienteExists(clienteId);
        return direccionRepository.findByClienteDomain_Id(clienteId)
                .stream().map(DireccionDomain::toDto).toList();
    }


    public DireccionResponse findById(Long clienteId, Long id) {
        return getOrThrow(clienteId, id).toDto();
    }


    @Transactional
    public DireccionResponse create(Long clienteId, DireccionRequest request) {
        ClienteDomain cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));

        DireccionDomain direccion = direccionFactory.from(request);

        if (Boolean.TRUE.equals(request.principal())) {
            desmarcarPrincipal(clienteId);
            direccion.setPrincipal(true);
        } else if (!direccionRepository.existsByClienteDomainIdAndPrincipalTrue(clienteId)) {
            direccion.setPrincipal(true);
        }

        cliente.addDireccion(direccion);
        DireccionDomain saved = direccionRepository.save(direccion);
        log.info("Dirección creada id={} para cliente id={}", saved.getId(), clienteId);
        return saved.toDto();
    }


    @Transactional
    public DireccionResponse update(Long clienteId, Long id, DireccionRequest request) {
        DireccionDomain direccion = getOrThrow(clienteId, id);

        if (Boolean.TRUE.equals(request.principal()) && !Boolean.TRUE.equals(direccion.getPrincipal())) {
            desmarcarPrincipal(clienteId);
        }

        direccion.updateFrom(request);
        return direccionRepository.save(direccion).toDto();
    }


    @Transactional
    public void delete(Long clienteId, Long id) {
        DireccionDomain direccion = getOrThrow(clienteId, id);

        boolean esPrincipal = Boolean.TRUE.equals(direccion.getPrincipal());
        long count = direccionRepository.countByClienteDomain_Id(clienteId);

        // Bloquear solo si es principal Y es la única que queda
        if (esPrincipal && count == 1) {
            throw new BusinessException(
                    "No se puede eliminar la única dirección del cliente.");
        }

        // Si es principal pero hay más, simplemente la eliminamos
        // (opcionalmente podrías marcar otra como principal automáticamente)
        direccionRepository.delete(direccion);
        log.info("Dirección eliminada id={}", id);
    }

    private DireccionDomain getOrThrow(Long clienteId, Long id) {
        return direccionRepository.findByIdAndClienteDomain_Id(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Dirección", id));
    }

    private void assertClienteExists(Long clienteId) {
        if (!clienteRepository.existsById(clienteId))
            throw new ResourceNotFoundException("Cliente", clienteId);
    }

    private void desmarcarPrincipal(Long clienteId) {
        direccionRepository.findByClienteDomain_Id(clienteId).stream()
                .filter(d -> Boolean.TRUE.equals(d.getPrincipal()))
                .forEach(d -> { d.setPrincipal(false); direccionRepository.save(d); });
    }
}