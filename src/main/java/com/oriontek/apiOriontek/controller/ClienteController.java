package com.oriontek.apiOriontek.controller;

import com.oriontek.apiOriontek.dto.request.ClienteRequest;
import com.oriontek.apiOriontek.dto.response.ApiResponse;
import com.oriontek.apiOriontek.dto.response.ClienteResponse;
import com.oriontek.apiOriontek.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Gestión de clientes OrionTek")
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Listar clientes con filtros y paginación")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ClienteResponse>>> findAll(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean activo,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.ok("Clientes obtenidos", clienteService.findAll(nombre, email, activo, pageable)));
    }

    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Cliente encontrado", clienteService.findById(id)));
    }

    @Operation(summary = "Crear nuevo cliente")
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> create(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Cliente creado exitosamente", clienteService.create(request)));
    }

    @Operation(summary = "Actualizar cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> update(
            @PathVariable Long id, @Valid @RequestBody ClienteRequest request) {

        return ResponseEntity.ok(ApiResponse.ok("Cliente actualizado", clienteService.update(id, request)));
    }

    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Cliente eliminado", null));
    }
}
