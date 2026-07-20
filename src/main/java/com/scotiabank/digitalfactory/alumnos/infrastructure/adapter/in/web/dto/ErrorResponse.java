package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        List<String> mensajes
) {
    public static ErrorResponse de(int status, String error, List<String> mensajes) {
        return new ErrorResponse(Instant.now(), status, error, mensajes);
    }
}
