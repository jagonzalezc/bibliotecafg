package org.fabricadegenios.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Rol;
import org.hibernate.validator.constraints.Length;

public record RegistroUsuarioDTO(
        @NotBlank
        @Length(max = 10)
        String cedula,
        @NotBlank
        @Length(max = 200)
        String nombre,
        @NotBlank
        @Email
        @Length(max = 80)
        String email,
        @NotBlank
        String password,
        @NotBlank
        String genero,
        @NotBlank
        String rol
) {}



