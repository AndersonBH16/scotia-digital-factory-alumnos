package com.scotiabank.digitalfactory.alumnos.domain.exception;

import java.util.List;

public class AlumnoInvalidoException extends RuntimeException {

    private final List<String> errores;

    public AlumnoInvalidoException(List<String> errores) {
        super("Alumno inválido: " + String.join("; ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}