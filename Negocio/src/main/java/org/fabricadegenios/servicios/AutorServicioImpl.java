package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Autor;
import org.fabricadegenios.repositorios.AutorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorServicioImpl implements AutorServicio {

    private final AutorRepo autorRepo;

    @Autowired
    public AutorServicioImpl(AutorRepo autorRepo) {
        this.autorRepo = autorRepo;
    }

    @Override
    public Autor registrarAutor(Autor autor) throws Exception {
        Optional<Autor> buscado = autorRepo.findByNombre(autor.getNombre());
        if (buscado.isPresent()) {
            throw new Exception("El autor ya existe en el sistema.");
        }
        return autorRepo.save(autor);
    }

    @Override
    public Autor obtenerAutor(Long id) throws Exception {
        Optional<Autor> buscado = autorRepo.findById(id);
        if (buscado.isEmpty()) {
            throw new Exception("El autor con el código proporcionado no existe.");
        }
        return buscado.get();
    }

    @Override
    public Autor actualizarAutor(Autor autor) throws Exception {
        return autorRepo.save(autor);
    }

    @Override
    public void eliminarAutor(Autor autor) throws Exception {
        Optional<Autor> buscado = autorRepo.findById(autor.getCodigo());
        if (buscado.isEmpty()) {
            throw new Exception("El autor con el código proporcionado no existe.");
        }
        autorRepo.delete(autor);
    }

    @Override
    public List<Autor> listarAutores() {
        return autorRepo.findAll();
    }

    @Override
    public Page<Autor> paginarAutores(Pageable pageable) {
        return autorRepo.findAll(pageable);
    }

    @Override
    public Page<Autor> buscarAutoresPorNombre(String nombre, Pageable pageable) {
        return autorRepo.findByNombreContaining(nombre, pageable);
    }

    @Override
    public List<Autor> listarAutoresPorIds(List<Long> ids) {
        return autorRepo.findAllById(ids);
    }
}

