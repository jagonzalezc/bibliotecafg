package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Autor;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.repositorios.LibroRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LibroServicioImpl implements LibroServicio {

    private final LibroRepo libroRepo;
    private final AutorServicio autorServicio;

    @Autowired
    public LibroServicioImpl(LibroRepo libroRepo, AutorServicio autorServicio) {
        this.libroRepo = libroRepo;
        this.autorServicio = autorServicio;
    }

    public Libro registrarLibro(Libro libro, List<Long> autorIds) throws Exception {
        // Buscar autores por sus IDs
        List<Autor> autores = autorServicio.listarAutoresPorIds(autorIds);
        libro.setAutores(autores);

        // Guardar el libro en la base de datos
        return libroRepo.save(libro);
    }

    @Override
    public Libro obtenerLibro(String isbn) throws Exception {
        Optional<Libro> buscado = libroRepo.findById(isbn);
        if (buscado.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }
        return buscado.get();
    }

    @Override
    public Libro actualizarLibro(Libro libro) throws Exception {
        Optional<Libro> buscado = libroRepo.findById(libro.getIsbn());
        if (buscado.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }
        return libroRepo.save(libro);
    }

    @Override
    public void eliminarLibro(String isbn) throws Exception {
        Optional<Libro> buscado = libroRepo.findById(isbn);
        if (buscado.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }
        libroRepo.delete(buscado.get());
    }

    @Override
    public List<Libro> listarLibros() {
        return libroRepo.findAll();
    }

    @Override
    public Page<Libro> paginarLibros(Pageable pageable) {
        return libroRepo.findAll(pageable);
    }

    @Override
    public Page<Libro> buscarLibrosPorAutor(String nombreAutor, Pageable pageable) {
        return libroRepo.findByAutorNombre(nombreAutor, pageable);
    }

    @Override
    public List<Libro> buscarLibrosPorGenero(String genero) {
        return libroRepo.findAllByGenero(genero);
    }

    @Override
    public List<Libro> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy) {
        return libroRepo.findAvailableBooksByGenre(genero, hoy);
    }
}

