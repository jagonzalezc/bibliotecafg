package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.model.*;
import org.fabricadegenios.servicios.LibroServicio;
import org.fabricadegenios.servicios.ReservaServicio;
import org.fabricadegenios.servicios.UsuarioServicio;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class ReservaServicioTest {

    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Test
    public void registrarReservaTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));

        try {
            Reserva guardada = reservaServicio.registrarReserva(reserva);
            Assert.assertNotNull(guardada);
            Assert.assertEquals(reserva.getLibros().size(), guardada.getLibros().size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservation registration");
        }
    }

    @Test
    public void registrarReservaConLibrosNoDisponiblesTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, false, GeneroLibro.FICCION); // Libro no disponible
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));

        try {
            reservaServicio.registrarReserva(reserva);
            Assert.fail("Se esperaba una excepción al intentar registrar una reserva con libros no disponibles.");
        } catch (Exception e) {
            Assert.assertEquals("Uno o más libros ya no están disponibles.", e.getMessage());
        }
    }

    @Test
    public void obtenerReservaTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));
        reserva = reservaServicio.registrarReserva(reserva);

        try {
            Reserva obtenida = reservaServicio.obtenerReserva(reserva.getCodigo());
            Assert.assertNotNull(obtenida);
            Assert.assertEquals(reserva.getCodigo(), obtenida.getCodigo());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservation retrieval");
        }
    }

    @Test
    public void actualizarReservaTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));
        reserva = reservaServicio.registrarReserva(reserva);

        reserva.setFechaDevolucion(LocalDate.now().plusDays(15));

        try {
            Reserva actualizada = reservaServicio.actualizarReserva(reserva);
            Assert.assertEquals(LocalDate.now().plusDays(15), actualizada.getFechaDevolucion());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservation update");
        }
    }

    @Test
    public void eliminarReservaTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));
        reserva = reservaServicio.registrarReserva(reserva);

        try {
            reservaServicio.eliminarReserva(reserva.getCodigo());

            try {
                reservaServicio.obtenerReserva(reserva.getCodigo());
                Assert.fail("Se esperaba una excepción al intentar obtener una reserva eliminada");
            } catch (Exception e) {
                Assert.assertEquals("La reserva con el código proporcionado no existe.", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservation deletion");
        }
    }

    @Test
    public void listarReservasTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva1 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));
        Reserva reserva2 = new Reserva(LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(11), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));

        reservaServicio.registrarReserva(reserva1);
        reservaServicio.registrarReserva(reserva2);

        try {
            List<Reserva> reservas = reservaServicio.listarReservas();
            Assert.assertFalse(reservas.isEmpty());
            Assert.assertTrue(reservas.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservation listing");
        }
    }

    @Test
    public void buscarReservasPorUsuarioTest() throws Exception {
        Usuario usuario = new Usuario("1234567890", "juanperez@mail.com", GeneroPersona.MASCULINO, "Juan Pérez", "clave123");
        usuario = usuarioServicio.registrarUsuario(usuario);

        Libro libro1 = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        libroServicio.registrarLibro(libro1, Arrays.asList());
        libroServicio.registrarLibro(libro2, Arrays.asList());

        Reserva reserva1 = new Reserva(LocalDateTime.now(), LocalDate.now().plusDays(10), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));
        Reserva reserva2 = new Reserva(LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(11), LocalDate.now().getYear(), GeneroLibro.FICCION, usuario, Arrays.asList(libro1, libro2));

        reservaServicio.registrarReserva(reserva1);
        reservaServicio.registrarReserva(reserva2);

        try {
            List<Reserva> reservas = reservaServicio.buscarReservasPorUsuario(usuario.getCodigo());
            Assert.assertFalse(reservas.isEmpty());
            Assert.assertTrue(reservas.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during reservations by user search");
        }
    }
}


