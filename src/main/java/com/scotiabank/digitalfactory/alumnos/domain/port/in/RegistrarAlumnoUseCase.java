package com.scotiabank.digitalfactory.alumnos.domain.port.in;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import reactor.core.publisher.Mono;

public interface RegistrarAlumnoUseCase {
    Mono<Void> registrar(Alumno alumno);
}
