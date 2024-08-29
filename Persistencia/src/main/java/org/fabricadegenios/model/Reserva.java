package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Reserva implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;
    private LocalDateTime fechaReserva;
    private LocalDate fechaDevolucion;
    private Integer anio;

    @Enumerated(EnumType.STRING)
    private GeneroLibro genero;


    public Reserva(LocalDateTime fechaReserva, LocalDate fechaDevolucion, Integer anio, GeneroLibro genero) {
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.anio = anio;
        this.genero = genero;
    }

}

