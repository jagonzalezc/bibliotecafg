package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    //obtener un usuario a partir de su cedula
    @Query("SELECT u from Usuario u WHERE u.cedula= :cedula")
    Usuario obtenerUsuariosPorCedula(String cedula);

    //obtener usuarios a partir de parte del nombre
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Usuario> findAllByNombreContainsIgnoreCase(@Param("nombre") String nombre);

    //obtener usuario a partir de su nombre
    Optional<Usuario> findByNombre(String nombre);

    //obtener usuario a partir de email y password
    Optional<Usuario> findByEmailAndPassword(String email, String password);

    Optional<Usuario> findByCedulaAndPassword(String cedula, String password);

    Page<Usuario> findAll(Pageable paginador);


    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findById(Long id);
}
