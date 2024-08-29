package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private String nombre;
    private Integer anio;

    // Constructor con todos los atributos
    public Autor(String nombre, Integer anio) {
        this.nombre = nombre;
        this.anio = anio;
    }

}




