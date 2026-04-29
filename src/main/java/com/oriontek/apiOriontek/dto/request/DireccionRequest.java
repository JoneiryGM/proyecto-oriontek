package com.oriontek.apiOriontek.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DireccionRequest(

        @NotBlank(message = "La calle es obligatoria")
        @Size(max = 200, message = "La calle no puede superar 200 caracteres")
        String calle,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 100, message = "La ciudad no puede superar 100 caracteres")
        String ciudad,

        @NotBlank(message = "La provincia es obligatoria")
        @Size(max = 100, message = "La provincia no puede superar 100 caracteres")
        String provincia,

        @Size(max = 20, message = "El código postal no puede superar 20 caracteres")
        String codigoPostal,

        @Size(max = 100, message = "El país no puede superar 100 caracteres")
        String pais,

        Boolean principal
) {}