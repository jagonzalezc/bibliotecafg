package org.fabricadegenios.dto;

import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Rol;

import java.util.List;

public record UsuarioDTO(
        Long id,
        String cedula,
        String nombre,
        String email,
        String password,
        GeneroPersona genero,
        Rol rol
) {}



