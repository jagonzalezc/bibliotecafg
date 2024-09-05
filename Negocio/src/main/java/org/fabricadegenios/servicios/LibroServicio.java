package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LibroServicio {

    Libro registrarLibro(Libro libro, List<Long> autorIds) throws Exception;

    Libro obtenerLibro(String isbn) throws Exception;

    Libro actualizarLibro(Libro libro) throws Exception;

    void eliminarLibro(String isbn) throws Exception;

    List<Libro> listarLibros();

    Page<Libro> paginarLibros(Pageable pageable);

    Page<Libro> buscarLibrosPorAutor(String nombreAutor, Pageable pageable);

    List<Libro> buscarLibrosPorGenero(String genero);

    List<Libro> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy);
}
