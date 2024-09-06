package org.fabricadegenios.dto;

import org.fabricadegenios.model.GeneroLibro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReservaDTO(
        Long codigo,
        LocalDateTime fechaReserva,
        LocalDate fechaDevolucion,
        int anio,
        Long usuarioId,
        List<String> libroIds
) { }

