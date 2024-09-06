package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LibroServicio {

    LibroDTO registrarLibro(LibroDTO libroDTO) throws Exception;

    LibroDTO actualizarLibro(LibroDTO libroDTO) throws Exception;

    void asignarAutoresAlLibro(String isbn, List<Long> autorIds) throws Exception;

    LibroDTO obtenerLibro(String isbn) throws Exception;

    void eliminarLibro(String isbn) throws Exception;

    List<LibroDTO> listarLibros();

    Page<LibroDTO> paginarLibros(Pageable pageable);

    Page<LibroDTO> buscarLibrosPorAutor(String nombreAutor, Pageable pageable);

    List<LibroDTO> buscarLibrosPorGenero(String genero);

    List<LibroDTO> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy);

    // Agrega la firma del método convertirADTO
    LibroDTO convertirADTO(Libro libro);

    Libro convertirADominio(LibroDTO libroDTO);
}
