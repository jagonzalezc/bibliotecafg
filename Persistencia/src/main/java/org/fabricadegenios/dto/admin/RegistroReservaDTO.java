package org.fabricadegenios.dto.admin;

import org.fabricadegenios.model.GeneroLibro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RegistroReservaDTO(

        LocalDateTime fechaReserva,
        LocalDate fechaDevolucion,
        Integer anio,
        Long usuarioId,
        List<Long> libroIds
) { }
