package com.scotiabank.digitalfactory.alumnos.domain.model;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EstadoAlumnoTest {
    @ParameterizedTest
    @ValueSource(strings = {"activo", "ACTIVO", "Activo", "  activo  "})
    void aceptaVariasFormasDeEscribirActivo(String texto) {
        assertThat(EstadoAlumno.desdeTexto(texto)).isEqualTo(EstadoAlumno.ACTIVO);
    }

    @Test
    void aceptaInactivo() {
        assertThat(EstadoAlumno.desdeTexto("inactivo")).isEqualTo(EstadoAlumno.INACTIVO);
    }

    @Test
    void rechazaTextoVacio() {
        assertThatThrownBy(() -> EstadoAlumno.desdeTexto(""))
                .isInstanceOf(AlumnoInvalidoException.class);
    }

    @Test
    void rechazaTextoNoReconocido() {
        assertThatThrownBy(() -> EstadoAlumno.desdeTexto("suspendido"))
                .isInstanceOf(AlumnoInvalidoException.class)
                .hasMessageContaining("suspendido");
    }
}
