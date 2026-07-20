package com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoDuplicadoException;
import com.scotiabank.digitalfactory.alumnos.domain.model.Alumno;
import com.scotiabank.digitalfactory.alumnos.domain.model.EstadoAlumno;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.ListarAlumnosActivosUseCase;
import com.scotiabank.digitalfactory.alumnos.domain.port.in.RegistrarAlumnoUseCase;
import com.scotiabank.digitalfactory.alumnos.infrastructure.adapter.in.web.handler.GlobalExceptionHandler;
import com.scotiabank.digitalfactory.alumnos.infrastructure.config.ApiKeyAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(
        controllers = AlumnoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = ApiKeyAuthenticationFilter.class),
        excludeAutoConfiguration = {
                ReactiveSecurityAutoConfiguration.class,
                ReactiveUserDetailsServiceAutoConfiguration.class
        }
)
@Import(GlobalExceptionHandler.class)
class AlumnoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegistrarAlumnoUseCase registrarAlumnoUseCase;

    @MockitoBean
    private ListarAlumnosActivosUseCase listarAlumnosActivosUseCase;

    private static final String JSON_VALIDO = """
            {
                "id": "A001",
                "nombre": "Ana",
                "apellido": "Torres",
                "estado": "activo",
                "edad": 20
            }
            """;

    @Test
    void registraUnAlumnoValidoYRetorna201() {
        when(registrarAlumnoUseCase.registrar(any(Alumno.class))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON_VALIDO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();

        verify(registrarAlumnoUseCase).registrar(any(Alumno.class));
    }

    @Test
    void retorna409CuandoElAlumnoEstaDuplicado() {
        when(registrarAlumnoUseCase.registrar(any(Alumno.class)))
                .thenReturn(Mono.error(new AlumnoDuplicadoException("A001")));

        webTestClient.post()
                .uri("/api/v1/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(JSON_VALIDO)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Alumno duplicado");
    }

    @Test
    void retorna400CuandoElNombreEstaVacio() {
        String jsonInvalido = """
                {
                    "id": "A001",
                    "nombre": "",
                    "apellido": "Torres",
                    "estado": "activo",
                    "edad": 20
                }
                """;

        webTestClient.post()
                .uri("/api/v1/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonInvalido)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void listaLosAlumnosActivos() {
        Alumno alumno = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);
        when(listarAlumnosActivosUseCase.listarActivos()).thenReturn(Flux.just(alumno));

        webTestClient.get()
                .uri("/api/v1/alumnos/activos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Object.class)
                .hasSize(1);
    }
}