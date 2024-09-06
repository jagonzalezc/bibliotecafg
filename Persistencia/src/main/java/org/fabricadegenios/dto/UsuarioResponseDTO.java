package org.fabricadegenios.dto;

public record UsuarioResponseDTO(
        boolean error,
        String mensaje,
        UsuarioDTO usuario
) {}
