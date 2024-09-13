package org.fabricadegenios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;

    @Column(length = 120, nullable = false, unique = true)
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 10)
    private String cedula;

    @Column(length = 200, nullable = false)
    private String nombre;

    @ElementCollection
    @CollectionTable(name = "usuario_telefonos", joinColumns = @JoinColumn(name = "usuario_codigo"))
    @MapKeyColumn(name = "tipo_telefono")
    @Column(name = "numero_telefono")
    private Map<String, String> telefonos;

    @Enumerated(EnumType.STRING)
    private GeneroPersona genero;


    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;

    // Rol del usuario: puede ser USER o ADMIN
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Rol rol;

    public Usuario(String cedula, String email, GeneroPersona generoPersona, String nombre, String password, Rol rol) {
        this.cedula = cedula;
        this.email = email;
        this.genero = generoPersona;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", genero=" + genero +
                ", rol=" + rol +
                '}';
    }
}

