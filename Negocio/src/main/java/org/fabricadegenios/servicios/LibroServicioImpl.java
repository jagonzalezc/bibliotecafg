package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.repositorios.LibroRepo;
import org.fabricadegenios.servicios.AutorServicio;
import org.fabricadegenios.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroServicioImpl implements LibroServicio {

    private final LibroRepo libroRepo;
    private final AutorServicio autorServicio;

    @Autowired
    public LibroServicioImpl(LibroRepo libroRepo, AutorServicio autorServicio) {
        this.libroRepo = libroRepo;
        this.autorServicio = autorServicio;
    }

    @Override
    public LibroDTO registrarLibro(LibroDTO libroDTO) throws Exception {
        // Crear libro sin autores
        Libro libro = new Libro(
                libroDTO.isbn(),
                libroDTO.nombre(),
                libroDTO.editorial(),
                libroDTO.anio(),
                libroDTO.disponible(),
                libroDTO.genero()
        );
        // Guardar el libro en la base de datos
        Libro libroGuardado = libroRepo.save(libro);

        // Devolver DTO del libro guardado
        return convertirADTO(libroGuardado);
    }

    @Override
    public LibroDTO actualizarLibro(LibroDTO libroDTO) throws Exception {
        Optional<Libro> buscado = libroRepo.findById(libroDTO.isbn());
        if (buscado.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }

        // Actualizar libro existente
        Libro libro = buscado.get();
        libro.setNombre(libroDTO.nombre());
        libro.setEditorial(libroDTO.editorial());
        libro.setAnio(libroDTO.anio());
        libro.setDisponible(libroDTO.disponible());
        libro.setGenero(libroDTO.genero());

        // Guardar cambios en la base de datos
        Libro libroActualizado = libroRepo.save(libro);

        // Devolver DTO del libro actualizado
        return convertirADTO(libroActualizado);
    }


    @Override
    public void asignarAutoresAlLibro(String isbn, List<Long> autorIds) throws Exception {
        Optional<Libro> libroOptional = libroRepo.findById(isbn);
        if (libroOptional.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }
        Libro libro = libroOptional.get();

        // Buscar autores por sus IDs y convertir a DTOs
        List<AutorDTO> autoresDTO = autorServicio.listarAutoresPorIds(autorIds);

        // Convertir a objetos Autor
        List<Autor> autores = autoresDTO.stream()
                .map(dto -> new Autor( dto.nombre(), dto.anio())) // Asegúrate de que el constructor coincida con los atributos
                .collect(Collectors.toList());

        libro.setAutores(autores);

        // Guardar libro con los autores actualizados
        libroRepo.save(libro);
    }

    @Override
    public LibroDTO obtenerLibro(String isbn) throws Exception {
        Optional<Libro> buscado = libroRepo.findById(isbn);
        if (buscado.isEmpty()) {
            throw new Exception("El libro con el ISBN proporcionado no existe.");
        }

        Libro libro = buscado.get();
        return convertirADTO(libro);
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
    public List<LibroDTO> listarLibros() {
        List<Libro> libros = libroRepo.findAll();
        return libros.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<LibroDTO> paginarLibros(Pageable pageable) {
        Page<Libro> libros = libroRepo.findAll(pageable);
        return libros.map(this::convertirADTO);
    }

    @Override
    public Page<LibroDTO> buscarLibrosPorAutor(String nombreAutor, Pageable pageable) {
        Page<Libro> libros = libroRepo.findByAutorNombre(nombreAutor, pageable);
        return libros.map(this::convertirADTO);
    }

    @Override
    public List<LibroDTO> buscarLibrosPorGenero(String genero) {
        List<Libro> libros = libroRepo.findAllByGenero(genero);
        return libros.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroDTO> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy) {
        List<Libro> libros = libroRepo.findAvailableBooksByGenre(genero, hoy);
        return libros.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public LibroDTO convertirADTO(Libro libro) {
        if (libro == null) {
            return null;
        }
        // Convertimos el libro en DTO, obteniendo solo los IDs de los autores
        List<Long> autorIds = libro.getAutores() != null ?
                libro.getAutores().stream()
                        .map(Autor::getCodigo) // Solo obtenemos los IDs de los autores
                        .collect(Collectors.toList()) : Collections.emptyList(); // Devuelve una lista vacía si es null

        return new LibroDTO(
                libro.getIsbn(),
                libro.getNombre(),
                libro.getEditorial(),
                libro.getAnio(),
                libro.getDisponible(),
                libro.getGenero(),
                autorIds // Lista de IDs de autores
        );
    }


    public Libro convertirADominio(LibroDTO libroDTO) {
        Libro libro = new Libro();
        libro.setIsbn(libroDTO.isbn());
        libro.setNombre(libroDTO.nombre());
        libro.setEditorial(libroDTO.editorial());
        libro.setAnio(libroDTO.anio());
        libro.setDisponible(libroDTO.disponible());
        libro.setGenero(libroDTO.genero());

        // Asumimos que el servicio de autores ya está gestionando los IDs de autores en algún punto del flujo
        // pero en este método no deberíamos asignar autores directamente si solo trabajamos con DTO.

        return libro;
    }


}

