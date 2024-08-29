package org.fabricadegenios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import lombok.*;

import javax.persistence.ElementCollection;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString

public class Persona implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    private String cedula;
    private String nombre;
    private String email;
    @ElementCollection
    private HashMap<String, String> telefonos;

    @Enumerated(EnumType.STRING)
    private GeneroPersona genero;


    // Constructor con algunos atributos
    public Persona(String cedula, String nombre, String email ) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
    }
}
