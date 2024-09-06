package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.repositorios.AutorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AutorServicioImpl implements AutorServicio {

    private final AutorRepo autorRepo;

    @Autowired
    public AutorServicioImpl(AutorRepo autorRepo) {
        this.autorRepo = autorRepo;
    }

    @Override
    public AutorDTO registrarAutor(AutorDTO autorDTO) throws Exception {
        Optional<Autor> buscado = autorRepo.findByNombre(autorDTO.nombre());
        if (buscado.isPresent()) {
            throw new Exception("El autor ya existe en el sistema.");
        }

        Autor autor = new Autor(autorDTO.nombre(), autorDTO.anio());
        Autor guardado = autorRepo.save(autor);
        return new AutorDTO(guardado.getCodigo(), guardado.getNombre(), guardado.getAnio());
    }

    @Override
    public AutorDTO obtenerAutor(Long id) throws Exception {
        Optional<Autor> buscado = autorRepo.findById(id);
        if (buscado.isEmpty()) {
            throw new Exception("El autor con el código proporcionado no existe.");
        }

        Autor autor = buscado.get();
        return new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio());
    }

    @Override
    public AutorDTO actualizarAutor(AutorDTO autorDTO) throws Exception {
        Optional<Autor> buscado = autorRepo.findById(autorDTO.id());
        if (buscado.isEmpty()) {
            throw new Exception("El autor con el código proporcionado no existe.");
        }

        Autor autor = buscado.get();
        autor.setNombre(autorDTO.nombre());
        autor.setAnio(autorDTO.anio());
        Autor actualizado = autorRepo.save(autor);
        return new AutorDTO(actualizado.getCodigo(), actualizado.getNombre(), actualizado.getAnio());
    }

    @Override
    public void eliminarAutor(Long id) throws Exception {
        Optional<Autor> buscado = autorRepo.findById(id);
        if (buscado.isEmpty()) {
            throw new Exception("El autor con el código proporcionado no existe.");
        }
        autorRepo.delete(buscado.get());
    }

    @Override
    public List<AutorDTO> listarAutores() {
        return autorRepo.findAll().stream()
                .map(autor -> new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AutorDTO> paginarAutores(Pageable pageable) {
        return autorRepo.findAll(pageable).map(autor -> new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio()));
    }

    @Override
    public Page<AutorDTO> buscarAutoresPorNombre(String nombre, Pageable pageable) {
        return autorRepo.findByNombreContaining(nombre, pageable)
                .map(autor -> new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio()));
    }

    @Override
    public List<AutorDTO> listarAutoresPorIds(List<Long> autorIds) {
        List<Autor> autores = autorRepo.findAllById(autorIds);
        return autores.stream()
                .map(autor -> new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio()))
                .collect(Collectors.toList());
    }
}
