package com.scotiabank.digitalfactory.alumnos.application.usecase;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.RegistrarAlumnoUseCase;
import com.scotiabank.digitalfactory.alumnos.domain.port.out.AlumnoRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RegistrarAlumnoUseCaseImpl implements RegistrarAlumnoUseCase {
    private final AlumnoRepositoryPort alumnoRepositoryPort;

    public RegistrarAlumnoUseCaseImpl(AlumnoRepositoryPort alumnoRepositoryPort) {
        this.alumnoRepositoryPort = alumnoRepositoryPort;
    }

    @Override
    public Mono<Void> registrar(Alumno alumno) {
        return alumnoRepositoryPort.save(alumno).then();
    }
}
