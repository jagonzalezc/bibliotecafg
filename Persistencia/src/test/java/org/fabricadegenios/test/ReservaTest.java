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

        // Crear libros
        Libro libro1 = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", "Editorial Sudamericana", 1967, true, GeneroLibro.LITERATURA);
        Libro libro3 = new Libro("456-789-123", "Don Quijote", "Editorial Cervantes", 1605, true, GeneroLibro.FICCION);

        // Guardar los libros
        Libro libroGuardado1 = libroRepo.save(libro1);
        Libro libroGuardado2 = libroRepo.save(libro2);
        Libro libroGuardado3 = libroRepo.save(libro3);

        // Crear una reserva con los libros
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> libros = List.of(libroGuardado1, libroGuardado2, libroGuardado3);
        reserva.setLibros(libros);

        // Guardar la reserva
        Reserva guardada = reservaRepo.save(reserva);

        // Actualizar la disponibilidad de los libros a false
        libros.forEach(libro -> {
            libro.setDisponible(false);
            libroRepo.save(libro);
        });

        // Verificar que la reserva se haya guardado correctamente
        Assertions.assertNotNull(guardada);
        Assertions.assertNotNull(guardada.getCodigo());
        Assertions.assertEquals(3, guardada.getLibros().size());

        // Verificar que la disponibilidad de los libros ha cambiado a false
        libros.forEach(libro -> {
            Libro libroVerificado = libroRepo.findById(libro.getIsbn()).orElse(null);
            Assertions.assertNotNull(libroVerificado);
            Assertions.assertFalse(libroVerificado.getDisponible());
        });

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

        // Crear un libro con disponibilidad en true
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024);
        reserva.setUsuario(usuarioGuardado);
        List<Libro> librosReserva = new ArrayList<>();
        librosReserva.add(libroGuardado);
        reserva.setLibros(librosReserva);
        Reserva guardada = reservaRepo.save(reserva);

        // Modificar el estado de los libros a no disponible
        libroGuardado.setDisponible(false);
        libroRepo.save(libroGuardado);

        // Eliminar la reserva
        reservaRepo.delete(guardada);

        // Recuperar y actualizar el estado de los libros a disponible
        for (Libro libroEnReserva : librosReserva) {
            libroEnReserva.setDisponible(true);
            libroRepo.save(libroEnReserva);
        }

        // Verificar que se haya eliminado correctamente
        Reserva buscada = reservaRepo.findById(guardada.getCodigo()).orElse(null);
        Assertions.assertNull(buscada);

        // Verificar que el libro haya recuperado su estado de disponibilidad
        Libro libroRecuperado = libroRepo.findById(libroGuardado.getIsbn()).orElse(null);
        Assertions.assertNotNull(libroRecuperado);
        Assertions.assertTrue(libroRecuperado.getDisponible());

        System.out.println("Reserva eliminada y libros recuperados.");
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

        // Crear un libro con disponibilidad en true
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024);
        reserva.setUsuario(usuarioGuardado);

        // Asociar libros a la reserva
        List<Libro> librosReserva = new ArrayList<>();
        librosReserva.add(libroGuardado);
        reserva.setLibros(librosReserva);

        // Guardar la reserva y cambiar la disponibilidad de los libros
        Reserva reservaGuardada = reservaRepo.save(reserva);

        // Cambiar la disponibilidad de los libros a false después de guardar la reserva
        for (Libro libroReserva : reservaGuardada.getLibros()) {
            libroReserva.setDisponible(false);
            libroRepo.save(libroReserva);
        }

        // Actualizar la fecha de devolución
        reservaGuardada.setFechaDevolucion(LocalDate.now().plusDays(14));
        Reserva reservaActualizada = reservaRepo.save(reservaGuardada);

        // Verificar que la fecha de devolución se haya actualizado correctamente
        Reserva reservaBuscada = reservaRepo.findById(reservaActualizada.getCodigo()).orElse(null);
        Assertions.assertNotNull(reservaBuscada);
        Assertions.assertEquals(LocalDate.now().plusDays(14), reservaBuscada.getFechaDevolucion());
        System.out.println("Reserva actualizada: " + reservaBuscada);
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

        // Crear un libro con disponibilidad en true
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear una reserva
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024);
        reserva.setUsuario(usuarioGuardado);

        // Asociar libros a la reserva
        List<Libro> librosReserva = new ArrayList<>();
        librosReserva.add(libroGuardado);
        reserva.setLibros(librosReserva);

        // Guardar la reserva
        Reserva reservaGuardada = reservaRepo.save(reserva);

        // Cambiar la disponibilidad de los libros a false después de guardar la reserva
        for (Libro libroReserva : reservaGuardada.getLibros()) {
            libroReserva.setDisponible(false);
            libroRepo.save(libroReserva);
        }

        // Listar todas las reservas
        List<Reserva> reservas = reservaRepo.findAll();

        // Verificar que la lista no esté vacía
        Assertions.assertNotNull(reservas);
        Assertions.assertTrue(reservas.size() > 0);

        // Verificar que la reserva guardada esté en la lista
        Assertions.assertTrue(reservas.stream().anyMatch(r -> r.getCodigo().equals(reservaGuardada.getCodigo())));

        reservas.forEach(System.out::println);
    }


    @Test
    //Es equivalente a devolver los libros
    public void finalizarReservaTest() {
        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("333");
        usuario.setNombre("María");
        usuario.setEmail("maria@mail.com");
        usuario.setPassword("password123");
        usuario.setGenero(GeneroPersona.FEMENINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear dos libros con disponible en true
        Libro libro1 = new Libro("987-654-321", "Cien Años de Soledad", "Editorial Sudamericana", 1967, true, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("654-321-987", "Rayuela", "Editorial Siglo XXI", 1963, true, GeneroLibro.LITERATURA);
        Libro libroGuardado1 = libroRepo.save(libro1);
        Libro libroGuardado2 = libroRepo.save(libro2);

        // Crear una reserva con dos libros
        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), 2024);
        reserva.setUsuario(usuarioGuardado);
        reserva.setLibros(new ArrayList<>(List.of(libroGuardado1, libroGuardado2)));

        // Guardar la reserva y actualizar el estado disponible de los libros a false
        Reserva guardada = reservaRepo.save(reserva);

        libroGuardado1.setDisponible(false);
        libroGuardado2.setDisponible(false);
        libroRepo.saveAll(List.of(libroGuardado1, libroGuardado2));

        // Simulación: Se finaliza la reserva (actualizar fechaDevolucion y poner disponible en true)
        guardada.setFechaDevolucion(LocalDate.now());
        reservaRepo.save(guardada);

        libroGuardado1.setDisponible(true);
        libroGuardado2.setDisponible(true);
        libroRepo.saveAll(List.of(libroGuardado1, libroGuardado2));

        // Verificar que la reserva se haya finalizado correctamente
        Reserva buscada = reservaRepo.findById(guardada.getCodigo()).orElse(null);
        Assertions.assertNotNull(buscada);
        Assertions.assertEquals(LocalDate.now(), buscada.getFechaDevolucion());

        // Verificar que los libros estén disponibles nuevamente
        Assertions.assertTrue(libroRepo.findById(libroGuardado1.getIsbn()).get().getDisponible());
        Assertions.assertTrue(libroRepo.findById(libroGuardado2.getIsbn()).get().getDisponible());

        System.out.println("Reserva finalizada: " + buscada);
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

        // Crear un libro con disponibilidad en true
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear reservas
        Reserva reserva1 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(7), 2024);
        reserva1.setUsuario(usuarioGuardado);
        reserva1.setLibros(List.of(libroGuardado));
        reservaRepo.save(reserva1);

        Reserva reserva2 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(14), 2024);
        reserva2.setUsuario(usuarioGuardado);
        reserva2.setLibros(List.of(libroGuardado));
        reservaRepo.save(reserva2);

        // Cambiar la disponibilidad de los libros a false después de guardar las reservas
        for (Libro libroReserva : List.of(libroGuardado)) {
            libroReserva.setDisponible(false);
            libroRepo.save(libroReserva);
        }

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

        // Crear y guardar libro con disponibilidad en true
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear y guardar reservas con todos los datos necesarios
        Reserva reserva1 = new Reserva(
                LocalDateTime.now().minusDays(10),
                LocalDate.now().plusDays(7),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva1);

        Reserva reserva2 = new Reserva(
                LocalDateTime.now().minusDays(5),
                LocalDate.now().plusDays(14),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva2);

        Reserva reserva3 = new Reserva(
                LocalDateTime.now(),
                LocalDate.now().plusDays(21),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva3);

        // Cambiar la disponibilidad de los libros a false después de guardar las reservas
        for (Libro libroReserva : List.of(libroGuardado)) {
            libroReserva.setDisponible(false);
            libroRepo.save(libroReserva);
        }

        // Obtenemos la página de reservas
        Page<Reserva> lista = reservaRepo.findAll(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.getSize() > 0);

        // Imprimimos la página de reservas ordenada por fecha de reserva
        System.out.println("Página de reservas ordenada por fecha de reserva:");
        lista.getContent().forEach(System.out::println);
    }

    @Test
    public void paginarReservasActivasTest() {
        // Configuramos el paginador con ordenamiento por fecha de reserva
        Pageable pagina = PageRequest.of(0, 2, Sort.by("fechaReserva"));

        // Crear y guardar usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("333");
        usuario.setNombre("Luis");
        usuario.setEmail("luis@mail.com");
        usuario.setPassword("clave456");
        usuario.setGenero(GeneroPersona.MASCULINO);
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Crear y guardar libro
        Libro libro = new Libro("987-654-321", "Cien Años de Soledad", "Oveja negra", 1967, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear y guardar reservas activas y no activas
        reservaRepo.save(new Reserva(
                LocalDateTime.now().minusDays(10),
                LocalDate.now().minusDays(1), // Reserva no activa (fecha de devolución pasada)
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        reservaRepo.save(new Reserva(
                LocalDateTime.now().minusDays(5),
                LocalDate.now().plusDays(14), // Reserva activa
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        reservaRepo.save(new Reserva(
                LocalDateTime.now(),
                LocalDate.now().plusDays(21), // Reserva activa
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        ));

        // Obtenemos la página de reservas activas
        Page<Reserva> reservasActivas = reservaRepo.obtenerReservasActivas(usuarioGuardado.getCedula(), pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(reservasActivas);
        Assertions.assertEquals(2, reservasActivas.getContent().size());

        // Imprimimos la página de reservas activas ordenada por fecha de reserva
        System.out.println("Página de reservas activas ordenada por fecha de reserva:");
        reservasActivas.getContent().forEach(System.out::println);
    }

    @Test
    public void paginarReservasActivasPorUsuarioTest() {
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
        Libro libro = new Libro("123-456-789", "El Principito", "Planeta", 1943, true, GeneroLibro.LITERATURA);
        Libro libroGuardado = libroRepo.save(libro);

        // Crear y guardar reservas
        Reserva reserva1 = new Reserva(
                LocalDateTime.now().minusDays(10),
                LocalDate.now().plusDays(7),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva1);

        Reserva reserva2 = new Reserva(
                LocalDateTime.now().minusDays(5),
                LocalDate.now().plusDays(14),
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva2);

        Reserva reserva3 = new Reserva(
                LocalDateTime.now().minusDays(2),
                LocalDate.now().minusDays(1), // Reserva pasada
                2024,
                GeneroLibro.LITERATURA,
                usuarioGuardado,
                List.of(libroGuardado)
        );
        reservaRepo.save(reserva3);

        // Cambiar la disponibilidad de los libros a false después de guardar las reservas
        for (Libro libroReserva : List.of(libroGuardado)) {
            libroReserva.setDisponible(false);
            libroRepo.save(libroReserva);
        }

        // Filtrar y paginar reservas activas del usuario
        Page<Reserva> lista = reservaRepo.findByUsuarioAndFechaDevolucionAfter(usuarioGuardado, LocalDate.now(), pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.getSize() > 0);

        // Imprimimos la página de reservas ordenada por fecha de reserva
        System.out.println("Página de reservas activas ordenada por fecha de reserva:");
        lista.getContent().forEach(System.out::println);
    }

}

