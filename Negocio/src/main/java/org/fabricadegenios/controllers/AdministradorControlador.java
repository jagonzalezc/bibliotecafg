package org.fabricadegenios.controllers;

import org.fabricadegenios.dto.*;
import org.fabricadegenios.dto.admin.RegistroAutorDTO;
import org.fabricadegenios.dto.admin.RegistroLibroDTO;
import org.fabricadegenios.dto.admin.RegistroReservaDTO;
import org.fabricadegenios.dto.admin.RegistroUsuarioDTO;
import org.fabricadegenios.servicios.AdministradorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdministradorControlador {

    @Autowired
    private AdministradorServicio administradorServicio;

    // Usuarios
    @PostMapping("/usuarios")
    public ResponseEntity<Long> registrarUsuario(@Valid @RequestBody RegistroUsuarioDTO registroUsuarioDTO) throws Exception {
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);
        return new ResponseEntity<>(usuarioId, HttpStatus.CREATED);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) throws Exception {
        UsuarioDTO usuarioDTO = administradorServicio.obtenerUsuario(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody RegistroUsuarioDTO registroUsuarioDTO) throws Exception {
        UsuarioDTO usuarioActualizado = administradorServicio.actualizarUsuario(id, registroUsuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) throws Exception {
        administradorServicio.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuarios(Pageable pageable) {
        System.out.println("pasa");
        Page<UsuarioDTO> usuarios = administradorServicio.paginarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/listaUsuarios")
    public ResponseEntity<List<UsuarioDTO>> listaUsuarios() {
        System.out.println("pasa");
        List<UsuarioDTO> usuarios = administradorServicio.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuarios/cedula/{cedula}")
    public ResponseEntity<UsuarioDTO> obtenerUsuariosPorCedula(@PathVariable String cedula) throws Exception {
        UsuarioDTO usuarioDTO = administradorServicio.obtenerUsuariosPorCedula(cedula);
        return ResponseEntity.ok(usuarioDTO);
    }

    // Autores
    @PostMapping("/autores")
    public ResponseEntity<Long> registrarAutor(@RequestBody RegistroAutorDTO registroAutorDTO) throws Exception {
        Long autorId = administradorServicio.registrarAutor(registroAutorDTO);
        return new ResponseEntity<>(autorId, HttpStatus.CREATED);
    }

    @GetMapping("/autores/{id}")
    public ResponseEntity<AutorDTO> obtenerAutor(@PathVariable Long id) throws Exception {
        AutorDTO autorDTO = administradorServicio.obtenerAutor(id);
        return ResponseEntity.ok(autorDTO);
    }

    @PutMapping("/autores/{id}")
    public ResponseEntity<AutorDTO> actualizarAutor(@PathVariable Long id, @RequestBody RegistroAutorDTO registroAutorDTO) throws Exception {
        AutorDTO autorActualizado = administradorServicio.actualizarAutor(id, registroAutorDTO);
        return ResponseEntity.ok(autorActualizado);
    }

    @DeleteMapping("/autores/{id}")
    public ResponseEntity<Void> eliminarAutor(@PathVariable Long id) throws Exception {
        administradorServicio.eliminarAutor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/autores")
    public ResponseEntity<List<AutorDTO>> listarAutores() {
        List<AutorDTO> autores = administradorServicio.listarAutores();
        return ResponseEntity.ok(autores);
    }

    // Libros
    @PostMapping("/libros")
    public ResponseEntity<Long> registrarLibro(@RequestBody RegistroLibroDTO registroLibroDTO) throws Exception {
        Long libroId = administradorServicio.registrarLibro(registroLibroDTO);
        return new ResponseEntity<>(libroId, HttpStatus.CREATED);
    }

    @GetMapping("/libros/{isbn}")
    public ResponseEntity<LibroDTO> obtenerLibro(@PathVariable Long isbn) throws Exception {
        LibroDTO libroDTO = administradorServicio.obtenerLibro(isbn);
        return ResponseEntity.ok(libroDTO);
    }

    @PutMapping("/libros/{isbn}")
    public ResponseEntity<LibroDTO> actualizarLibro(@PathVariable Long isbn, @RequestBody RegistroLibroDTO registroLibroDTO) throws Exception {
        LibroDTO libroActualizado = administradorServicio.actualizarLibro(isbn, registroLibroDTO);
        return ResponseEntity.ok(libroActualizado);
    }

    @PutMapping("/libros/{isbn}/autores")
    public ResponseEntity<LibroDTO> asignarAutoresLibro(@PathVariable Long isbn, @RequestBody List<Long> autores) throws Exception {
        // Llama al servicio para asignar los autores al libro con el ISBN proporcionado
        LibroDTO libroActualizado = administradorServicio.asignarAutoresAlLibro(isbn, autores);
        // Devuelve la respuesta con el libro actualizado
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/libros/{isbn}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long isbn) throws Exception {
        administradorServicio.eliminarLibro(isbn);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/libros")
    public ResponseEntity<List<LibroDTO>> listarLibros() {
        List<LibroDTO> libros = administradorServicio.listarLibros();
        return ResponseEntity.ok(libros);
    }

    // Reservas
    @PostMapping("/reservas")
    public ResponseEntity<Long> registrarReserva(@RequestBody RegistroReservaDTO registroReservaDTO) throws Exception {
        Long reservaId = administradorServicio.registrarReserva(registroReservaDTO);
        return new ResponseEntity<>(reservaId, HttpStatus.CREATED);
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<ReservaDTO> obtenerReserva(@PathVariable Long id) throws Exception {
        ReservaDTO reservaDTO = administradorServicio.obtenerReserva(id);
        return ResponseEntity.ok(reservaDTO);
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Void> actualizarReserva(@PathVariable Long id, @RequestBody RegistroReservaDTO registroReservaDTO) throws Exception {
        administradorServicio.actualizarReserva(id, registroReservaDTO);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para asignar libros a una reserva existente
    @PutMapping("/reservas/{reservaId}/libros")
    public ResponseEntity<ReservaDTO> asignarLibrosAReserva(@PathVariable Long reservaId, @RequestBody List<Long> libroIds) throws Exception {
        // Llama al servicio para asignar los libros a la reserva con el ID proporcionado
        administradorServicio.asignarLibrosAReserva(reservaId, libroIds);

        // Obtener la reserva actualizada (si necesitas devolverla como respuesta)
        ReservaDTO reservaActualizada = administradorServicio.obtenerReserva(reservaId);

        // Devuelve la respuesta con la reserva actualizada
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) throws Exception {
        administradorServicio.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reservas = administradorServicio.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/reservas/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDTO>> buscarReservasPorUsuario(@PathVariable Long usuarioId) {
        List<ReservaDTO> reservas = administradorServicio.buscarReservasPorUsuario(usuarioId);
        return ResponseEntity.ok(reservas);
    }
}

