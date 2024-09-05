package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AutorServicio {
    Autor registrarAutor(Autor autor) throws Exception;

    Autor obtenerAutor(Long id) throws Exception;

    Autor actualizarAutor(Autor autor) throws Exception;

    void eliminarAutor(Autor autor) throws Exception;

    List<Autor> listarAutores();

    Page<Autor> paginarAutores(Pageable pageable);

    Page<Autor> buscarAutoresPorNombre(String nombre, Pageable pageable);

    List<Autor> listarAutoresPorIds(List<Long> ids);
}
