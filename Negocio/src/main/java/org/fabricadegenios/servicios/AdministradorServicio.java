package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.*;
import org.fabricadegenios.dto.admin.RegistroAutorDTO;
import org.fabricadegenios.dto.admin.RegistroLibroDTO;
import org.fabricadegenios.dto.admin.RegistroReservaDTO;
import org.fabricadegenios.dto.admin.RegistroUsuarioDTO;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdministradorServicio {


    Long registrarUsuario(RegistroUsuarioDTO registroUsuarioDTO) throws Exception;

    UsuarioDTO obtenerUsuario(Long id) throws Exception;

    UsuarioDTO actualizarUsuario(Long codigo, RegistroUsuarioDTO registroUsuarioDTO) throws Exception;

    void eliminarUsuario(Long id) throws Exception;

    List<UsuarioDTO> listarUsuarios();

    UsuarioDTO obtenerUsuariosPorCedula(String cedula) throws Exception;

    List<UsuarioDTO> findAllByNombreContainsIgnoreCase(String nombre);

    Optional<UsuarioDTO> findByEmailAndPassword(String email, String password);

    Optional<UsuarioDTO> findByCedulaAndPassword(String cedula, String password);

    Page<UsuarioDTO> paginarUsuarios(Pageable pageable);


    Long registrarAutor(RegistroAutorDTO registroAutorDTO) throws Exception;

    AutorDTO obtenerAutor(Long id) throws Exception;


    AutorDTO actualizarAutor(Long codigo, RegistroAutorDTO registroAutorDTO) throws Exception;

    void eliminarAutor(Long id) throws Exception;

    List<AutorDTO> listarAutores();

    Page<AutorDTO> paginarAutores(Pageable pageable);

    Page<AutorDTO> buscarAutoresPorNombre(String nombre, Pageable pageable);

    List<AutorDTO> listarAutoresPorIds(List<Long> autorIds);


    Long registrarLibro(RegistroLibroDTO libroDTO) throws Exception;


    LibroDTO actualizarLibro(Long codigo, RegistroLibroDTO registroLibroDTO) throws Exception;

    void asignarAutoresAlLibro(Long isbn, List<Long> autorIds) throws Exception;

    LibroDTO obtenerLibro(Long codigo) throws Exception;

    void eliminarLibro(Long codigo) throws Exception;

    List<LibroDTO> listarLibros();

    Page<LibroDTO> paginarLibros(Pageable pageable);

    Page<LibroDTO> buscarLibrosPorAutor(String nombreAutor, Pageable pageable);

    List<LibroDTO> buscarLibrosPorGenero(String genero);

    List<LibroDTO> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy);

    //CRUDS DE RESERVAS
    Long registrarReserva(RegistroReservaDTO registroReservaDTO) throws Exception;

    void asignarLibrosAReserva(Long reservaId, List<Long> libroIds) throws Exception;

    ReservaDTO obtenerReserva(Long codigo);

    void actualizarReserva(Long reservaId, RegistroReservaDTO reservaActualizadaDTO) throws Exception;

    void eliminarReserva(Long codigo);

    List<ReservaDTO> listarReservas();

    List<ReservaDTO> buscarReservasPorUsuario(Long usuarioId);
}

