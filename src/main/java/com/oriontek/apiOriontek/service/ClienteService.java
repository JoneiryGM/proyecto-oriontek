package com.oriontek.apiOriontek.service;

import com.oriontek.apiOriontek.config.BusinessException;
import com.oriontek.apiOriontek.config.ResourceNotFoundException;
import com.oriontek.apiOriontek.domain.ClienteDomain;
import com.oriontek.apiOriontek.domain.DireccionDomain;
import com.oriontek.apiOriontek.dto.request.ClienteRequest;
import com.oriontek.apiOriontek.dto.response.ClienteResponse;
import com.oriontek.apiOriontek.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ClienteDomain clienteFactory = new ClienteDomain();
    private final DireccionDomain direccionFactory = new DireccionDomain();


    public Page<ClienteResponse> findAll(String nombre, String email, Boolean activo, Pageable pageable) {
        return clienteRepository
                .findAllWithFilters(nombre, email, activo, pageable)
                .map(ClienteDomain::toDto);
    }


    public ClienteResponse findById(Long id) {
        return getOrThrow(id).toDto();
    }


    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.email())) {
            throw new BusinessException("Ya existe un cliente con el email: " + request.email());
        }

        ClienteDomain cliente = clienteFactory.from(request);

        if (request.direcciones() != null && !request.direcciones().isEmpty()) {
            var dirs = request.direcciones();
            boolean tienePrincipal = dirs.stream()
                    .anyMatch(d -> Boolean.TRUE.equals(d.principal()));

            for (int i = 0; i < dirs.size(); i++) {
                DireccionDomain d = direccionFactory.from(dirs.get(i));
                if (!tienePrincipal && i == 0) d.setPrincipal(true);
                cliente.addDireccion(d);
            }
        }

        ClienteDomain saved = clienteRepository.save(cliente);
        log.info("Cliente creado id={}", saved.getId());
        return saved.toDto();
    }


    @Transactional
    public ClienteResponse update(Long id, ClienteRequest request) {
        ClienteDomain cliente = getOrThrow(id);

        if (clienteRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new BusinessException("El email " + request.email() + " ya está en uso");
        }

        cliente.updateFrom(request);
        return clienteRepository.save(cliente).toDto();
    }


    @Transactional
    public void delete(Long id) {
        clienteRepository.delete(getOrThrow(id));
        log.info("Cliente eliminado id={}", id);
    }

    private ClienteDomain getOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }
}