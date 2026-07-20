package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlumnoRequest(
        @NotBlank(message = "El campo 'id' es obligatorio")
        String id,

        @NotBlank(message = "El campo 'nombre' es obligatorio")
        String nombre,

        @NotBlank(message = "El campo 'apellido' es obligatorio")
        String apellido,

        @NotBlank(message = "El campo 'estado' es obligatorio")
        String estado,

        @NotNull(message = "El campo 'edad' es obligatorio")
        Integer edad
) {
}
