package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.ListUsuariosResponseDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.dto.UsuarioResponseDTO;
import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {


    UsuarioDTO registrarUsuario(UsuarioDTO dto) throws Exception;

    UsuarioDTO obtenerUsuario(Long id) throws Exception;

    UsuarioDTO actualizarUsuario(UsuarioDTO dto) throws Exception;

    void eliminarUsuario(Long id) throws Exception;

    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO obtenerUsuariosPorCedula(String cedula) throws Exception;

    List<UsuarioDTO> findAllByNombreContainsIgnoreCase(String nombre);

    Optional<UsuarioDTO> findByEmailAndPassword(String email, String password);

    Optional<UsuarioDTO> findByCedulaAndPassword(String cedula, String password);

    Page<UsuarioDTO> paginarUsuarios(Pageable pageable);
}
