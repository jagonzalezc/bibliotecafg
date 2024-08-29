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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:data.sql") // Carga el archivo data.sql antes de ejecutar las pruebas
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

    @Test
    public void filtrarPorUsuarioTest() {
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

        // Crear reservas
        Reserva reserva1 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024, GeneroLibro.LITERATURA);
        reserva1.setUsuario(usuarioGuardado);
        reserva1.setLibros(List.of(libroGuardado));
        reservaRepo.save(reserva1);

        Reserva reserva2 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(14), 2024, GeneroLibro.LITERATURA);
        reserva2.setUsuario(usuarioGuardado);
        reserva2.setLibros(List.of(libroGuardado));
        reservaRepo.save(reserva2);

        // Filtrar por usuario
        List<Reserva> reservas = reservaRepo.findByUsuario(usuarioGuardado);

        // Verificar que se han recuperado las reservas correctas
        Assertions.assertNotNull(reservas);
        Assertions.assertTrue(reservas.size() >= 2);
        reservas.forEach(r -> System.out.println("Reserva filtrada por usuario: " + r));
    }

    @Test
    public void paginarReservasTest() {
        // Configuramos el paginador con ordenamiento por fecha de reserva
        Pageable pagina = PageRequest.of(0, 2, Sort.by("fechaReserva"));

        // Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("222");
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@mail.com");
        usuario.setPassword("clave123");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar libro
        Libro libro = new Libro("123-456-789", "El Principito", 5, 1943, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear y guardar reservas con todos los datos necesarios
        reservaRepo.save(new Reserva(
                LocalDateTime.now().minusDays(10),
                LocalDate.now().plusDays(7),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        reservaRepo.save(new Reserva(
                LocalDateTime.now().minusDays(5),
                LocalDate.now().plusDays(14),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        reservaRepo.save(new Reserva(
                LocalDateTime.now(),
                LocalDate.now().plusDays(21),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        // Obtenemos la página de reservas
        Page<Reserva> lista = reservaRepo.findAll(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.getSize() > 0);

        // Imprimimos la página de reservas ordenada por fecha de reserva
        System.out.println("Página de reservas ordenada por fecha de reserva:");
        lista.getContent().forEach(System.out::println);
    }


}

