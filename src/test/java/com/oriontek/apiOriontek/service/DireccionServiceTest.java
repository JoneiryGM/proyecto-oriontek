package com.oriontek.apiOriontek.service;

import com.oriontek.apiOriontek.config.BusinessException;
import com.oriontek.apiOriontek.config.ResourceNotFoundException;
import com.oriontek.apiOriontek.domain.ClienteDomain;
import com.oriontek.apiOriontek.domain.DireccionDomain;
import com.oriontek.apiOriontek.dto.request.DireccionRequest;
import com.oriontek.apiOriontek.dto.response.DireccionResponse;
import com.oriontek.apiOriontek.repository.ClienteRepository;
import com.oriontek.apiOriontek.repository.DireccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private DireccionService direccionService;

    private ClienteDomain cliente;
    private DireccionDomain direccion;
    private DireccionRequest direccionRequest;

    @BeforeEach
    void setUp() {
        cliente = ClienteDomain.builder().id(1L).nombre("Joneiry").build();

        direccion = DireccionDomain.builder()
                .id(10L)
                .calle("Av. Winston Churchill")
                .ciudad("Santo Domingo")
                .provincia("Distrito Nacional")
                .principal(true)
                .clienteDomain(cliente)
                .build();

        // Record de DireccionRequest
        direccionRequest = new DireccionRequest(
                "Av. Winston Churchill",
                "Santo Domingo",
                "Distrito Nacional",
                "10101",
                "República Dominicana",
                true
        );
    }

    @Test
    @DisplayName("Debe crear una dirección y asignarla al cliente")
    void create_Success() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(direccionRepository.save(any(DireccionDomain.class))).thenReturn(direccion);

        // Act
        DireccionResponse response = direccionService.create(1L, direccionRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Av. Winston Churchill", response.calle());
        verify(clienteRepository).findById(1L);
        verify(direccionRepository).save(any(DireccionDomain.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear si el cliente no existe")
    void create_ClienteNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> direccionService.create(1L, direccionRequest));
    }

    @Test
    @DisplayName("Debe fallar al eliminar si es la única dirección principal")
    void delete_Fail_OnlyDirection() {
        // Arrange
        when(direccionRepository.findByIdAndClienteDomain_Id(10L, 1L)).thenReturn(Optional.of(direccion));
        when(direccionRepository.countByClienteDomain_Id(1L)).thenReturn(1L); // Es la única

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> direccionService.delete(1L, 10L));

        assertTrue(exception.getMessage().contains("única dirección"));
        verify(direccionRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Debe desmarcar otras direcciones si la nueva es principal")
    void create_SetNewPrincipal() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        // Simulamos que ya existe una principal
        DireccionDomain viejaPrincipal = DireccionDomain.builder().id(11L).principal(true).build();
        when(direccionRepository.findByClienteDomain_Id(1L)).thenReturn(java.util.List.of(viejaPrincipal));
        when(direccionRepository.save(any(DireccionDomain.class))).thenReturn(direccion);

        // Act
        direccionService.create(1L, direccionRequest);

        // Assert
        // Verificamos que se llamó a save para la dirección vieja (para ponerla en false)
        // y para la nueva (en true)
        verify(direccionRepository, atLeast(2)).save(any(DireccionDomain.class));
    }
}