package org.fabricadegenios.dto;

import org.fabricadegenios.model.GeneroLibro;

import java.util.List;

public record LibroDTO(
        String isbn,
        String nombre,
        String editorial,
        int anio,
        boolean disponible,
        GeneroLibro genero,
        List<Long> autorIds
) {}

