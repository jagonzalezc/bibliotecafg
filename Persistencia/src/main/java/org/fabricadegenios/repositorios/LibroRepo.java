package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LibroRepo extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE LOWER(l.genero) LIKE LOWER(CONCAT('%', :genero, '%'))")
    List<Libro> findAllByGenero(@Param("genero") String genero);

    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombreAutor, '%'))")
    Page<Libro> findByAutorNombre(String nombreAutor, Pageable pageable);


    @Query("SELECT l FROM Libro l WHERE l.disponible = true AND LOWER(l.genero) LIKE LOWER(CONCAT('%', :genero, '%')) AND NOT EXISTS (" +
            "SELECT r FROM Reserva r JOIN r.libros rl WHERE rl.isbn = l.isbn AND r.fechaDevolucion >= :hoy)")
    List<Libro> findAvailableBooksByGenre(@Param("genero") String genero, @Param("hoy") LocalDate hoy);

}

