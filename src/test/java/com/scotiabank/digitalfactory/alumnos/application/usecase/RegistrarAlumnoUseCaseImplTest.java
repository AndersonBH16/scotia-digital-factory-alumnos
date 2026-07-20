package com.scotiabank.digitalfactory.alumnos.application.usecase;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoDuplicadoException;
import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.out.AlumnoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarAlumnoUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort alumnoRepositoryPort;

    private RegistrarAlumnoUseCaseImpl registrarAlumnoUseCase;

    private final Alumno alumno = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);

    @Test
    void registraUnAlumnoCorrectamente() {
        registrarAlumnoUseCase = new RegistrarAlumnoUseCaseImpl(alumnoRepositoryPort);
        when(alumnoRepositoryPort.save(any(Alumno.class))).thenReturn(Mono.just(alumno));

        StepVerifier.create(registrarAlumnoUseCase.registrar(alumno))
                .verifyComplete();

        verify(alumnoRepositoryPort).save(alumno);
    }

    @Test
    void propagaElErrorDeDuplicadoSinAlterarlo() {
        registrarAlumnoUseCase = new RegistrarAlumnoUseCaseImpl(alumnoRepositoryPort);
        when(alumnoRepositoryPort.save(any(Alumno.class)))
                .thenReturn(Mono.error(new AlumnoDuplicadoException("A001")));

        StepVerifier.create(registrarAlumnoUseCase.registrar(alumno))
                .expectErrorMatches(error ->
                        error instanceof AlumnoDuplicadoException
                                && error.getMessage().contains("A001"))
                .verify();
    }
}