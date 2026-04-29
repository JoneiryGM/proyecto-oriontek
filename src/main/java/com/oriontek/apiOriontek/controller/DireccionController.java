package com.oriontek.apiOriontek.controller;

import com.oriontek.apiOriontek.dto.request.DireccionRequest;
import com.oriontek.apiOriontek.dto.response.ApiResponse;
import com.oriontek.apiOriontek.dto.response.DireccionResponse;
import com.oriontek.apiOriontek.service.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Direcciones", description = "Gestión de direcciones por cliente")
@RestController
@RequestMapping("/clientes/{clienteId}/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @Operation(summary = "Listar todas las direcciones de un cliente")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DireccionResponse>>> findAll(@PathVariable Long clienteId) {
        return ResponseEntity.ok(
                ApiResponse.ok("Direcciones obtenidas", direccionService.findByClienteId(clienteId)));
    }

    @Operation(summary = "Obtener dirección por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DireccionResponse>> findById(
            @PathVariable Long clienteId, @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.ok("Dirección encontrada", direccionService.findById(clienteId, id)));
    }

    @Operation(summary = "Agregar dirección a un cliente")
    @PostMapping
    public ResponseEntity<ApiResponse<DireccionResponse>> create(
            @PathVariable Long clienteId, @Valid @RequestBody DireccionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Dirección creada", direccionService.create(clienteId, request)));
    }

    @Operation(summary = "Actualizar dirección")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DireccionResponse>> update(
            @PathVariable Long clienteId, @PathVariable Long id,
            @Valid @RequestBody DireccionRequest request) {

        return ResponseEntity.ok(
                ApiResponse.ok("Dirección actualizada", direccionService.update(clienteId, id, request)));
    }

    @Operation(summary = "Eliminar dirección")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long clienteId, @PathVariable Long id) {

        direccionService.delete(clienteId, id);
        return ResponseEntity.ok(ApiResponse.ok("Dirección eliminada", null));
    }
}