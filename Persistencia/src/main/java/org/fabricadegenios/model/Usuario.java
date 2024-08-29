package org.fabricadegenios.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long codigo;

    @Column(length = 120, nullable = false, unique = true)
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Ciudad ciudad;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;
}
