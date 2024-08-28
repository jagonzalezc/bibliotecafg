package org.fabricadegenios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Libro implements Serializable {
    @Id
    private String isbn;
    private String nombre;
    private Integer unidades;
    private Integer anio;

    @Enumerated(EnumType.STRING)
    private GeneroLibro genero;

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
}
