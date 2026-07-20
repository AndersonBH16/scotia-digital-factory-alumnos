package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.out.persistance;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoDuplicadoException;
import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class InMemoryAlumnoRepositoryAdapterTest {

    private InMemoryAlumnoRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAlumnoRepositoryAdapter();
    }

    @Test
    void guardaUnAlumnoNuevoCorrectamente() {
        Alumno alumno = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);

        StepVerifier.create(repository.save(alumno))
                .expectNext(alumno)
                .verifyComplete();
    }

    @Test
    void rechazaUnIdDuplicado() {
        Alumno alumno = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);

        repository.save(alumno).block(); // primer guardado, se completa antes del segundo intento

        StepVerifier.create(repository.save(alumno))
                .expectErrorMatches(error ->
                        error instanceof AlumnoDuplicadoException
                                && error.getMessage().contains("A001"))
                .verify();
    }

    @Test
    void filtraCorrectamentePorEstado() {
        Alumno activo = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);
        Alumno inactivo = new Alumno("A002", "Luis", "Diaz", EstadoAlumno.INACTIVO, 30);

        repository.save(activo).block();
        repository.save(inactivo).block();

        StepVerifier.create(repository.findByEstado(EstadoAlumno.ACTIVO))
                .expectNext(activo)
                .verifyComplete();
    }
}