package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Reserva implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long codigo;
    private LocalDateTime fechaReserva;
    private LocalDate fechaDevolucion;
    private Integer anio;

    @Enumerated(EnumType.STRING)
    private GeneroLibro genero;

    // Constructor por defecto
    public Reserva() {
        super();
    }

    public Reserva(LocalDateTime fechaReserva, LocalDate fechaDevolucion, Integer anio, GeneroLibro genero) {
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.anio = anio;
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(codigo, reserva.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}

