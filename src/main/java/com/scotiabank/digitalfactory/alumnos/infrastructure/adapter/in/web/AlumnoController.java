package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web;

import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.ListarAlumnosActivosUseCase;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.RegistrarAlumnoUseCase;
import com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto.AlumnoRequest;
import com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.dto.AlumnoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
    private final RegistrarAlumnoUseCase registrarAlumnoUseCase;
    private final ListarAlumnosActivosUseCase listarAlumnosActivosUseCase;

    public AlumnoController(RegistrarAlumnoUseCase registrarAlumnoUseCase, ListarAlumnosActivosUseCase listarAlumnosActivosUseCase) {
        this.registrarAlumnoUseCase = registrarAlumnoUseCase;
        this.listarAlumnosActivosUseCase = listarAlumnosActivosUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<Void>> registrar(@Valid @RequestBody AlumnoRequest request){
        return Mono.fromCallable(() -> aAlumno(request))
                .flatMap(registrarAlumnoUseCase::registrar)
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).<Void>build());
    }

    @GetMapping("/activos")
    public Flux<AlumnoResponse> listarActivos() {
        return listarAlumnosActivosUseCase.listarActivos()
                .map(AlumnoResponse::desde);
    }

    private Alumno aAlumno(AlumnoRequest request){
        return new Alumno(
                request.id(),
                request.nombre(),
                request.apellido(),
                EstadoAlumno.desdeTexto(request.estado()),
                request.edad()
        );
    }
}
