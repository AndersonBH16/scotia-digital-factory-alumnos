package com.scotiabank.digitalfactory.alumnos.application.usecase;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.ListarAlumnosActivosUseCase;
import com.scotiabank.digitalfactory.alumnos.domain.port.out.AlumnoRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListarAlumnosActivosUseCaseImpl implements ListarAlumnosActivosUseCase {

    private final AlumnoRepositoryPort alumnoRepositoryPort;

    public ListarAlumnosActivosUseCaseImpl(AlumnoRepositoryPort alumnoRepositoryPort) {
        this.alumnoRepositoryPort = alumnoRepositoryPort;
    }

    @Override
    public Flux<Alumno> listarActivos() {
        return null;
    }
}
