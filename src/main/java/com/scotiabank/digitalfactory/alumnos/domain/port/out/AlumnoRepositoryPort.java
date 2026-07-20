package com.scotiabank.digitalfactory.alumnos.domain.port.out;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoRepositoryPort {
    Mono<Alumno> save(Alumno alumno);

    Flux<Alumno> findByEstado(EstadoAlumno estado);
}
