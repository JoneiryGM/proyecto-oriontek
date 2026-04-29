package com.oriontek.apiOriontek.service;

import com.oriontek.apiOriontek.config.BusinessException;
import com.oriontek.apiOriontek.config.ResourceNotFoundException;
import com.oriontek.apiOriontek.domain.ClienteDomain;
import com.oriontek.apiOriontek.dto.request.ClienteRequest;
import com.oriontek.apiOriontek.dto.response.ClienteResponse;
import com.oriontek.apiOriontek.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDomain cliente;
    private ClienteRequest clienteRequest;

    @BeforeEach
    void setUp() {
        // Objeto de dominio simulado (lo que devolvería el Repo)
        cliente = ClienteDomain.builder()
                .id(1L)
                .nombre("Joneiry")
                .apellido("Guzman")
                .email("joneiry@example.com")
                .telefono("809-555-1234")
                .activo(true)
                .direcciones(List.of())
                .build();

        // Request simulado (lo que viene del Controller)
        clienteRequest = new ClienteRequest(
                "Joneiry",
                "Guzman",
                "joneiry@example.com",
                "809-555-1234",
                List.of()
        );
    }

    @Test
    @DisplayName("Debe encontrar un cliente por ID y convertirlo a DTO")
    void findById_Success() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ClienteResponse response = clienteService.findById(1L);

        assertNotNull(response);
        assertEquals(cliente.getNombre(), response.nombre());
        assertEquals(cliente.getEmail(), response.email());
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el ID no existe")
    void findById_NotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.findById(99L));
    }

    @Test
    @DisplayName("Debe crear un cliente correctamente")
    void create_Success() {
        when(clienteRepository.existsByEmail(clienteRequest.email())).thenReturn(false);
        // El service usará ClienteDomain.from(request) internamente
        when(clienteRepository.save(any(ClienteDomain.class))).thenReturn(cliente);

        ClienteResponse response = clienteService.create(clienteRequest);

        assertNotNull(response);
        assertEquals("joneiry@example.com", response.email());
        verify(clienteRepository).save(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Debe fallar si el email ya está en uso al crear")
    void create_Fail_EmailDuplicated() {
        when(clienteRepository.existsByEmail(clienteRequest.email())).thenReturn(true);

        assertThrows(BusinessException.class, () -> clienteService.create(clienteRequest));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar un cliente existente")
    void update_Success() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmailAndIdNot(clienteRequest.email(), 1L)).thenReturn(false);
        when(clienteRepository.save(any(ClienteDomain.class))).thenReturn(cliente);

        ClienteResponse response = clienteService.update(1L, clienteRequest);

        assertNotNull(response);
        verify(clienteRepository).save(any(ClienteDomain.class));
    }

    @Test
    @DisplayName("Debe eliminar un cliente si existe")
    void delete_Success() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        assertDoesNotThrow(() -> clienteService.delete(1L));
        verify(clienteRepository).delete(cliente);
    }
}