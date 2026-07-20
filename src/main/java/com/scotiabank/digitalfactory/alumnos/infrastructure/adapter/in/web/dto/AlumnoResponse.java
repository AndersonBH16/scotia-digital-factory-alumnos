package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;

public record AlumnoResponse(
    String id,
    String nombre,
    String apellido,
    String estado,
    int edad
) {
    public static AlumnoResponse desde(Alumno alumno){
        return new AlumnoResponse(
                alumno.id(),
                alumno.nombre(),
                alumno.apellido(),
                alumno.estado().name(),
                alumno.edad()
        );
    }
}
