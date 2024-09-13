package org.fabricadegenios.dto.admin;

import jakarta.validation.constraints.NotBlank;
import org.fabricadegenios.model.GeneroLibro;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record RegistroLibroDTO(
        @NotBlank
        @Length(max = 50)
        String isbn,
        @NotBlank
        @Length(max = 100)
        String nombre,
        @NotBlank
        @Length(max = 100)
        String editorial,
        @NotBlank
        int anio,
        @NotBlank
        boolean disponible,
        @NotBlank
        String genero,
        @NotBlank
        List<Long> autorIds
) {}
