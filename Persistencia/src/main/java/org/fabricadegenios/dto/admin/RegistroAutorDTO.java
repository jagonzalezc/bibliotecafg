package org.fabricadegenios.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Rol;
import org.hibernate.validator.constraints.Length;

public record RegistroAutorDTO(

        @NotBlank
        @Length(max = 200)
        String nombre,

        @NotNull
        Integer anio
) {}




