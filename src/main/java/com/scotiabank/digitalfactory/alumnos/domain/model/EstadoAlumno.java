package com.scotiabank.digitalfactory.alumnos.domain.model;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoInvalidoException;

import java.util.List;

public enum EstadoAlumno {
    ACTIVO,
    INACTIVO;

    public static EstadoAlumno desdeTexto(String valor){
        if(valor == null || valor.isBlank()){
            throw new AlumnoInvalidoException(List.of("El campo 'estado' es obligatorio"));
        }

        try{
            return EstadoAlumno.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e){
            throw new AlumnoInvalidoException(
                    List.of("El campo 'estado' debe ser 'ACTIVO' o 'INACTIVO', se recibió: " + valor));
        }
    }
}
