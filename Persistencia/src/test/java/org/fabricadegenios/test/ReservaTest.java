package org.fabricadegenios.test;

import org.fabricadegenios.model.*;
import org.fabricadegenios.repositorios.ReservaRepo;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.fabricadegenios.repositorios.LibroRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservaTest {

    @Autowired
    private ReservaRepo reservaRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private LibroRepo libroRepo;

    @Test
    public void registrarReservaTest() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("222");
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");
        usuario.setPassword("clave123");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear un libro
        Libro libro = new Libro("123-456-789", "El Principito", 5, 1943, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024, GeneroLibro.LITERATURA);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> libros = new ArrayList<>();
        libros.add(libroGuardado);
        reserva.setLibros(libros);

        // Guardar la reserva
        Reserva guardada = reservaRepo.save(reserva);

        // Verificar que se haya guardado correctamente
        Assertions.assertNotNull(guardada);
        Assertions.assertNotNull(guardada.getCodigo());
        Assertions.assertEquals(1, guardada.getLibros().size());
        System.out.println("Reserva registrada: " + guardada);
    }

    @Test
    public void eliminarReservaTest() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("222");
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");
        usuario.setPassword("clave123");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear un libro
        Libro libro = new Libro("123-456-789", "El Principito", 5, 1943, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024, GeneroLibro.LITERATURA);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> libros = new ArrayList<>();
        libros.add(libroGuardado);
        reserva.setLibros(libros);
        Reserva guardada = reservaRepo.save(reserva);

        // Eliminar la reserva
        reservaRepo.delete(guardada);

        // Verificar que se haya eliminado correctamente
        Reserva buscada = reservaRepo.findById(guardada.getCodigo()).orElse(null);
        Assertions.assertNull(buscada);
        System.out.println("Reserva eliminada.");
    }

    @Test
    public void actualizarReservaTest() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("222");
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");
        usuario.setPassword("clave123");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear un libro
        Libro libro = new Libro("123-456-789", "El Principito", 5, 1943, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024, GeneroLibro.LITERATURA);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> libros = new ArrayList<>();
        libros.add(libroGuardado);
        reserva.setLibros(libros);
        Reserva guardada = reservaRepo.save(reserva);

        // Actualizar la fecha de devolución
        guardada.setFechaDevolucion(LocalDate.now().plusDays(14));
        Reserva actualizada = reservaRepo.save(guardada);

        // Verificar que se haya actualizado correctamente
        Reserva buscada = reservaRepo.findById(actualizada.getCodigo()).orElse(null);
        Assertions.assertNotNull(buscada);
        Assertions.assertEquals(LocalDate.now().plusDays(14), buscada.getFechaDevolucion());
        System.out.println("Reserva actualizada: " + buscada);
    }

    @Test
    public void listarReservasTest() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("222");
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");
        usuario.setPassword("clave123");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear un libro
        Libro libro = new Libro("123-456-789", "El Principito", 5, 1943, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024, GeneroLibro.LITERATURA);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> libros = new ArrayList<>();
        libros.add(libroGuardado);
        reserva.setLibros(libros);
        reservaRepo.save(reserva);

        // Listar todas las reservas
        List<Reserva> reservas = reservaRepo.findAll();

        // Verificar que la lista no esté vacía
        Assertions.assertNotNull(reservas);
        Assertions.assertTrue(reservas.size() > 0);
        reservas.forEach(System.out::println);
    }
}

