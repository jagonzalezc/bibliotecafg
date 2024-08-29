package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepo extends JpaRepository<Autor, Long> {
}

