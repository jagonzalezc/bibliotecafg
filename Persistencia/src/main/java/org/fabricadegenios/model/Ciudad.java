package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import javax.persistence.ElementCollection;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString

public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;
    private String nombre;

    // Constructor con todos los atributos
    public Ciudad(String nombre) {
        this.nombre = nombre;
    }
}

