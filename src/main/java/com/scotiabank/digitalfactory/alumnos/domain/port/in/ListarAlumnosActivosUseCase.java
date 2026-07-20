package com.scotiabank.digitalfactory.alumnos.domain.port.in;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import reactor.core.publisher.Flux;

public interface ListarAlumnosActivosUseCase {

    Flux<Alumno> listarActivos();

}
