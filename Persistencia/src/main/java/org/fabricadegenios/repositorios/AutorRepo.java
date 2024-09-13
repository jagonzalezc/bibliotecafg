package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AutorRepo extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Autor> findByNombreContaining(@Param("nombre") String nombre, Pageable pageable);

    Page<Autor> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Método para paginar sin condiciones (lista completa de autores)
    @Override
    Page<Autor> findAll(Pageable pageable);

    Optional<Autor> findByNombre(String nombre);
}


