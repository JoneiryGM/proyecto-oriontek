package com.oriontek.apiOriontek.dto.response;

import java.time.LocalDateTime;

public record DireccionResponse(
        Long id,
        Long clienteId,
        String calle,
        String ciudad,
        String provincia,
        String codigoPostal,
        String pais,
        Boolean principal,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
