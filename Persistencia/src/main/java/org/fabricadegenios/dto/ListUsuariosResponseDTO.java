package org.fabricadegenios.dto;

import java.util.List;

public record ListUsuariosResponseDTO(
        boolean error,
        List<UsuarioDTO> usuarios
) {}
