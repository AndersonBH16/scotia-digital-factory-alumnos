package com.scotiabank.digitalfactory.alumnos.domain.model;

import com.scotiabank.digitalfactory.alumnos.domain.exception.AlumnoInvalidoException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public record Alumno(String id, String nombre, String apellido, EstadoAlumno estado, int edad) {
    private static final int EDAD_MINIMA = 1;
    private static final int EDAD_MAXIMA = 120;
    private static final int LONGITUD_MAXIMA_NOMBRE = 50;

    private static final Pattern PATRON_NOMBRE = Pattern.compile("^[\\p{L}][\\p{L}'\\- ]{0,49}$");

    public Alumno {
        List<String> errores = new ArrayList<>();

        if (id == null || id.isBlank()) {
            errores.add("El campo 'id' es obligatorio");
        }

        validarNombre(nombre, "nombre", errores);
        validarNombre(apellido, "apellido", errores);

        if (estado == null) {
            errores.add("El campo 'estado' es obligatorio");
        }

        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            errores.add("El campo 'edad' debe estar entre " + EDAD_MINIMA + " y " + EDAD_MAXIMA);
        }

        if (!errores.isEmpty()) {
            throw new AlumnoInvalidoException(errores);
        }
    }

    private static void validarNombre(String valor, String campo, List<String> errores) {
        if (valor == null || valor.isBlank()) {
            errores.add("El campo '" + campo + "' es obligatorio");
            return;
        }
        if (valor.length() > LONGITUD_MAXIMA_NOMBRE) {
            errores.add("El campo '" + campo + "' no debe superar " + LONGITUD_MAXIMA_NOMBRE + " caracteres");
            return;
        }
        if (!PATRON_NOMBRE.matcher(valor).matches()) {
            errores.add("El campo '" + campo + "' solo debe contener letras, espacios, apóstrofes o guiones");
        }
    }

    public boolean estaActivo() {
        return this.estado == EstadoAlumno.ACTIVO;
    }
}
