package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.model.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AutorServicio {

    AutorDTO registrarAutor(AutorDTO autorDTO) throws Exception;

    AutorDTO obtenerAutor(Long id) throws Exception;

    AutorDTO actualizarAutor(AutorDTO autorDTO) throws Exception;

    void eliminarAutor(Long id) throws Exception;

    List<AutorDTO> listarAutores();

    Page<AutorDTO> paginarAutores(Pageable pageable);

    Page<AutorDTO> buscarAutoresPorNombre(String nombre, Pageable pageable);



    List<AutorDTO> listarAutoresPorIds(List<Long> autorIds);
}
