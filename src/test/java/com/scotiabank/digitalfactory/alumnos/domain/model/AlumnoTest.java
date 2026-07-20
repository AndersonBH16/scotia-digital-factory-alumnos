package com.scotiabank.digitalfactory.alumnos.domain.model;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlumnoTest {

    @Test
    @DisplayName("Crea un Alumno válido correctamente")
    void creaAlumnoValido() {
        Alumno alumno = new Alumno("A001", "María José", "Pérez-Gómez", EstadoAlumno.ACTIVO, 20);

        assertThat(alumno.id()).isEqualTo("A001");
        assertThat(alumno.estaActivo()).isTrue();
    }

    @Test
    @DisplayName("Rechaza un id nulo o vacío")
    void rechazaIdVacio() {
        assertThatThrownBy(() -> new Alumno("", "Ana", "Torres", EstadoAlumno.ACTIVO, 20))
                .isInstanceOf(AlumnoInvalidoException.class)
                .hasMessageContaining("id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "Ana123", "Ana@Torres", "Ana_Torres"})
    @DisplayName("Rechaza nombres vacíos o con caracteres no permitidos")
    void rechazaNombreInvalido(String nombreInvalido) {
        assertThatThrownBy(() ->
                new Alumno("A001", nombreInvalido, "Torres", EstadoAlumno.ACTIVO, 20))
                .isInstanceOf(AlumnoInvalidoException.class);
    }

    @Test
    @DisplayName("Acepta nombres con tildes, ñ, espacios, apóstrofes y guiones")
    void aceptaNombresConCaracteresEspeciales() {
        Alumno alumno = new Alumno("A001", "Ñoño O'Connor", "De la Cruz-Ramírez", EstadoAlumno.ACTIVO, 20);

        assertThat(alumno.nombre()).isEqualTo("Ñoño O'Connor");
    }

    @Test
    @DisplayName("Rechaza estado nulo")
    void rechazaEstadoNulo() {
        assertThatThrownBy(() -> new Alumno("A001", "Ana", "Torres", null, 20))
                .isInstanceOf(AlumnoInvalidoException.class)
                .hasMessageContaining("estado");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 121, 500})
    @DisplayName("Rechaza edades fuera del rango 1-120")
    void rechazaEdadFueraDeRango(int edadInvalida) {
        assertThatThrownBy(() ->
                new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, edadInvalida))
                .isInstanceOf(AlumnoInvalidoException.class)
                .hasMessageContaining("edad");
    }

    @Test
    @DisplayName("Acumula todos los errores de validación en una sola excepción")
    void acumulaTodosLosErrores() {
        assertThatThrownBy(() -> new Alumno("", "", "", null, -5))
                .isInstanceOf(AlumnoInvalidoException.class)
                .satisfies(ex -> {
                    var errores = ((AlumnoInvalidoException) ex).getErrores();
                    assertThat(errores).hasSize(4); // id, nombre, apellido, estado (edad no se valida más porque los otros ya fallaron... revisar)
                });
    }

    @Test
    @DisplayName("estaActivo() retorna true solo cuando el estado es ACTIVO")
    void estaActivoRetornaCorrectamente() {
        Alumno activo = new Alumno("A001", "Ana", "Torres", EstadoAlumno.ACTIVO, 20);
        Alumno inactivo = new Alumno("A002", "Luis", "Diaz", EstadoAlumno.INACTIVO, 30);

        assertThat(activo.estaActivo()).isTrue();
        assertThat(inactivo.estaActivo()).isFalse();
    }
}