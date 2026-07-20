package com.scotiabank.digitalfactory.alumnos.domain.exception;

public class AlumnoDuplicadoException extends RuntimeException{
    private final String id;

    public AlumnoDuplicadoException(String id) {
        super("Ya existe un alumno registrado con id: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
