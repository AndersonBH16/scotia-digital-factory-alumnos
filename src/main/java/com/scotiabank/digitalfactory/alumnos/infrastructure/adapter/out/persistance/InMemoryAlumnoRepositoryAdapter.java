package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.out.persistance;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoDuplicadoException;
import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.out.AlumnoRepositoryPort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAlumnoRepositoryAdapter implements AlumnoRepositoryPort {
    private final Map<String, Alumno> almacenamiento = new ConcurrentHashMap<>();

    @Override
    public Mono<Alumno> save(Alumno alumno) {
        return Mono.defer(() -> {
           Alumno alumnoExistente = almacenamiento.putIfAbsent(alumno.id(), alumno);

           if(alumnoExistente != null){
               return Mono.error(new AlumnoDuplicadoException(alumno.id()));
           }

           return Mono.just(alumno);
        });
    }

    @Override
    public Flux<Alumno> findByEstado(EstadoAlumno estado) {
        return Flux.defer(() -> Flux.fromIterable(almacenamiento.values())).filter(alumno -> alumno.estado() == estado);
    }
}
