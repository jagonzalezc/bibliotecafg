package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Reserva implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaReserva;
    @Column(nullable = false)
    private LocalDate fechaDevolucion;
    private Integer anio;

    @Enumerated(EnumType.STRING)
    private GeneroLibro genero;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;

    @ManyToMany
    private List<Libro> libros;


    public Reserva(LocalDateTime fechaReserva, LocalDate fechaDevolucion, Integer anio, GeneroLibro genero) {
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.anio = anio;
        this.genero = genero;
    }


    public Reserva(LocalDateTime fechaReserva, LocalDate fechaDevolucion, Integer anio, GeneroLibro genero, Usuario usuario, List<Libro> libros) {
        this.fechaReserva = fechaReserva;
        this.fechaDevolucion = fechaDevolucion;
        this.anio = anio;
        this.genero = genero;
        this.usuario = usuario;
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "codigo=" + codigo +
                ", fechaReserva=" + fechaReserva +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }

}

