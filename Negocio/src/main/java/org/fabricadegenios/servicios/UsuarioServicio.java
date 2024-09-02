package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {
    Usuario registrarUsuario(Usuario u) throws Exception;
    Usuario obtenerUsuario (Long id) throws Exception;
    Usuario actualizarUsuario(Usuario u) throws Exception;
    void eliminarUsuario(Usuario u) throws Exception;
    List<Usuario> listarUsuarios();
    Usuario obtenerUsuariosPorCedula(String cedula) throws Exception;
    List<Usuario> findAllByNombreContainsIgnoreCase(@Param("nombre") String nombre);
    Optional<Usuario> findByEmailAndPassword(String email, String password);
    Optional<Usuario> findByCedulaAndPassword(String cedula, String password);
    // Método para paginar usuarios
    Page<Usuario> paginarUsuarios(Pageable pageable);
}
