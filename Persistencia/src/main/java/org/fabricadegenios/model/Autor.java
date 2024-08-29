package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Autor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;
    @Column(length = 200, nullable = false)
    private String nombre;
    private Integer anio;

    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros;

    // Constructor con todos los atributos
    public Autor(String nombre, Integer anio) {
        this.nombre = nombre;
        this.anio = anio;
    }

}




