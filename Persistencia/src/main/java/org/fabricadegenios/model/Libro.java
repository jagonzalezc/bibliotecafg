package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Libro implements Serializable {
    @Id
    @Column(length = 50)
    private String isbn;
    @Column(length = 100, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Integer unidades;
    private Integer anio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GeneroLibro genero;

    @ManyToMany(mappedBy = "libros")
    private List<Reserva> reservas;

    @ManyToMany
    private List<Autor> autores;

    // Constructor por defecto
    public Libro() {
        super();
    }

    // Constructor con todos los atributos
    public Libro(String isbn, String nombre, Integer unidades, Integer anio, GeneroLibro genero) {
        this.isbn = isbn;
        this.nombre = nombre;
        this.unidades = unidades;
        this.anio = anio;
        this.genero = genero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }
    @Override
    public String toString() {
        return "Libro{" +
                "isbn=" + isbn +
                ", nombre='" + nombre + '\'' +
                ", unidades='" + unidades + '\'' +
                ", anio='" + anio + '\'' +
                '}';
    }

}
