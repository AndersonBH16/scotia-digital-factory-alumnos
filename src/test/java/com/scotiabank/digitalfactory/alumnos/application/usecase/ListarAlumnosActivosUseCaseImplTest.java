package com.scotiabank.digitalfactory.alumnos.application.usecase;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.out.AlumnoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarAlumnosActivosUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort alumnoRepositoryPort;

    private ListarAlumnosActivosUseCaseImpl listarAlumnosActivosUseCase;

    @Test
    void listaSoloLosAlumnosActivosDelegandoAlRepositorio() {
        listarAlumnosActivosUseCase = new ListarAlumnosActivosUseCaseImpl(alumnoRepositoryPort);

        Alumno activo1 = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);
        Alumno activo2 = new Alumno("A002", "Luis", "Diaz", EstadoAlumno.ACTIVO, 25);

        when(alumnoRepositoryPort.findByEstado(EstadoAlumno.ACTIVO))
                .thenReturn(Flux.just(activo1, activo2));

        StepVerifier.create(listarAlumnosActivosUseCase.listarActivos())
                .expectNext(activo1)
                .expectNext(activo2)
                .verifyComplete();

        verify(alumnoRepositoryPort).findByEstado(EstadoAlumno.ACTIVO);
    }

    @Test
    void retornaFluxVacioCuandoNoHayActivos() {
        listarAlumnosActivosUseCase = new ListarAlumnosActivosUseCaseImpl(alumnoRepositoryPort);

        when(alumnoRepositoryPort.findByEstado(EstadoAlumno.ACTIVO))
                .thenReturn(Flux.empty());

        StepVerifier.create(listarAlumnosActivosUseCase.listarActivos())
                .verifyComplete();
    }
}