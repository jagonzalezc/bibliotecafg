package org.fabricadegenios.dto;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}
