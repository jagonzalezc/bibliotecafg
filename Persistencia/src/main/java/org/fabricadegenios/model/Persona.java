package org.fabricadegenios.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Persona implements Serializable {
    @Id
    private String cedula;
    private String nombre;
    private String email;
    @ElementCollection
    private List<String> telefonos;

    @Enumerated(EnumType.STRING)
    private GeneroPersona genero;

    // Constructor por defecto
    public Persona() {
        super();
    }

    // Constructor con todos los atributos
    public Persona(String cedula, String nombre, String email, List<String> telefonos, GeneroPersona genero) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.telefonos = telefonos;
        this.genero = genero;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(cedula, persona.cedula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cedula);
    }


}
