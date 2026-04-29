package com.oriontek.apiOriontek.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        Boolean activo,
        List<DireccionResponse> direcciones,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}