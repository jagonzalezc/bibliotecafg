package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, String> {

    @Query("SELECT u from Usuario u WHERE u.cedula= :cedula")
    List<Usuario> obtenerUsuariosPorCedula(String cedula);

    List<Usuario> findAllByNombreContains(String nombre);


    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndPassword(String email, String password);

    Optional<Usuario> findByCedulaAndPassword(String cedula, String password);

    Page<Usuario> findAll(Pageable paginador);
}
