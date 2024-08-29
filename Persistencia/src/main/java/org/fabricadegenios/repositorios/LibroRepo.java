package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepo extends JpaRepository<Libro, String> {

    List<Libro> findAllByGenero(GeneroLibro genero);
}

