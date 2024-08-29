package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepo extends JpaRepository<Libro, String> {
    // Puedes añadir métodos personalizados si lo necesitas
}

