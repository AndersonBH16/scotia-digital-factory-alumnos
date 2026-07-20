package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.handler;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoDuplicadoException;
import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoInvalidoException;
import com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;

public class GlobalExceptionHandler {
    @ExceptionHandler(AlumnoInvalidoException.class)
    public Mono<ResponseEntity<ErrorResponse>> manejarAlumnoInvalido(AlumnoInvalidoException ex) {
        return Mono.just(ResponseEntity.badRequest()
                .body(ErrorResponse.de(HttpStatus.BAD_REQUEST.value(), "Alumno inválido", ex.getErrores())));
    }

    @ExceptionHandler(AlumnoDuplicadoException.class)
    public Mono<ResponseEntity<ErrorResponse>> manejarAlumnoDuplicado(AlumnoDuplicadoException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.de(HttpStatus.CONFLICT.value(), "Alumno duplicado", List.of(ex.getMessage()))));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> manejarErroresDeValidacion(WebExchangeBindException ex) {
        List<String> mensajes = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return Mono.just(ResponseEntity.badRequest()
                .body(ErrorResponse.de(HttpStatus.BAD_REQUEST.value(), "Solicitud inválida", mensajes)));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> manejarErrorGenerico(Exception ex) {
        return Mono.just(ResponseEntity.internalServerError()
                .body(ErrorResponse.de(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno",
                        List.of("Ocurrió un error inesperado"))));
    }
}
