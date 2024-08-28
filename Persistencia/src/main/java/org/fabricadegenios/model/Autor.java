package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Autor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String nombre;
    private Integer anio;


    // Constructor por defecto
    public Autor() {
        super();
    }

    // Constructor con todos los atributos
    public Autor(String nombre, Integer anio) {
        this.nombre = nombre;
        this.anio = anio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(codigo, autor.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}




