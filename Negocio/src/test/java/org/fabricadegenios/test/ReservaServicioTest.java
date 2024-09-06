package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.dto.ReservaDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.model.Reserva;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.LibroRepo;
import org.fabricadegenios.repositorios.ReservaRepo;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.fabricadegenios.servicios.LibroServicio;
import org.fabricadegenios.servicios.ReservaServicio;
import org.fabricadegenios.servicios.UsuarioServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class ReservaServicioTest {

    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private LibroRepo libroRepo;
    @Autowired
    private ReservaRepo reservaRepo;

    @Test
    @Transactional
    public void registrarReserva() throws Exception {
        // Crear y guardar un usuario
        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "1234567890", "Juan Pérez", "juanperez@mail.com", "clave123", GeneroPersona.MASCULINO);
        Usuario usuario = new Usuario(usuarioDTO.cedula(), usuarioDTO.email(), usuarioDTO.genero(), usuarioDTO.nombre(), usuarioDTO.password());
        usuarioRepo.save(usuario);

        // Crear y guardar libros
        LibroDTO libroDTO1 = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libroDTO2 = new LibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION, Arrays.asList());

        Libro libro1 = new Libro(libroDTO1.isbn(), libroDTO1.nombre(), libroDTO1.editorial(), libroDTO1.anio(), libroDTO1.disponible(), libroDTO1.genero());
        Libro libro2 = new Libro(libroDTO2.isbn(), libroDTO2.nombre(), libroDTO2.editorial(), libroDTO2.anio(), libroDTO2.disponible(), libroDTO2.genero());

        libroRepo.save(libro1);
        libroRepo.save(libro2);

        // Crear y guardar la reserva
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaDevolucion(LocalDate.now().plusDays(10));
        reserva.setAnio(LocalDate.now().getYear());
        reserva.setUsuario(usuario);
        reserva.setLibros(Arrays.asList(libro1, libro2));

        reservaRepo.save(reserva);

        // Verificar que la reserva se haya guardado correctamente
        Reserva reservaGuardada = reservaRepo.findById(reserva.getCodigo())
                .orElseThrow(() -> new Exception("Reserva no encontrada"));

        assertEquals(usuario.getCodigo(), reservaGuardada.getUsuario().getCodigo());
        assertEquals(2, reservaGuardada.getLibros().size());
        assertTrue(reservaGuardada.getLibros().stream().anyMatch(libro -> libro.getIsbn().equals(libro1.getIsbn())));
        assertTrue(reservaGuardada.getLibros().stream().anyMatch(libro -> libro.getIsbn().equals(libro2.getIsbn())));
    }



    @Test
    public void registrarReservaConLibrosNoDisponiblesTest() throws Exception {
        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "1234567890", "Juan Pérez", "juanperez@mail.com", "clave123", GeneroPersona.MASCULINO);
        usuarioDTO = usuarioServicio.registrarUsuario(usuarioDTO);

        LibroDTO libro1 = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, false, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libro2 = new LibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION, Arrays.asList());

        libroServicio.registrarLibro(libro1);
        libroServicio.registrarLibro(libro2);

        ReservaDTO reservaDTO = new ReservaDTO(
                null,
                LocalDateTime.now(),
                LocalDate.now().plusDays(10),
                LocalDate.now().getYear(),
                usuarioDTO.id(),
                Arrays.asList(libro1.isbn(), libro2.isbn())
        );

        try {
            reservaServicio.registrarReserva(reservaDTO);
            fail("Se esperaba una excepción al intentar registrar una reserva con libros no disponibles.");
        } catch (Exception e) {
            assertEquals("Uno o más libros ya no están disponibles.", e.getMessage());
        }
    }

    @Test
    public void obtenerReservaTest() throws Exception {
        // Crear y registrar un usuario
        UsuarioDTO usuarioDTO = new UsuarioDTO(null, "1234567890", "Juan Pérez", "juanperez@mail.com", "clave123", GeneroPersona.MASCULINO);
        usuarioDTO = usuarioServicio.registrarUsuario(usuarioDTO);

        // Crear y registrar libros
        LibroDTO libro1 = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libro2 = new LibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION, Arrays.asList());

        libroServicio.registrarLibro(libro1);
        libroServicio.registrarLibro(libro2);

        // Crear y guardar una reserva directamente
        Usuario usuario = usuarioRepo.findById(usuarioDTO.id())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Libro> libros = libroRepo.findAllByIsbnIn(Arrays.asList(libro1.isbn(), libro2.isbn()));
        for (Libro libro : libros) {
            if (!libro.getDisponible()) {
                throw new RuntimeException("Uno o más libros no están disponibles.");
            }
            libro.setDisponible(false);
            libroRepo.save(libro);
        }

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaDevolucion(LocalDate.now().plusDays(10));
        reserva.setAnio(LocalDate.now().getYear());
        reserva.setUsuario(usuario);
        reserva.setLibros(libros);
        Reserva reservaGuardada = reservaRepo.save(reserva);

        // Verificar que la reserva se pueda obtener
        ReservaDTO obtenida = reservaServicio.obtenerReserva(reservaGuardada.getCodigo());
        assertNotNull(obtenida);
        assertEquals(reservaGuardada.getCodigo(), obtenida.codigo());
    }


    @Test
    @Transactional
    public void actualizarReservaTest() throws Exception {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("123456789");
        usuario.setEmail("usuario@example.com");
        usuario.setGenero(GeneroPersona.MASCULINO);
        usuario.setNombre("Juan Pérez");
        usuario.setPassword("password");
        usuarioRepo.save(usuario);

        // Crear y guardar libros para la reserva
        Libro libro1 = new Libro();
        libro1.setIsbn("1234567890");
        libro1.setNombre("Libro Ejemplo 1");
        libro1.setEditorial("Editorial Ejemplo");
        libro1.setAnio(2024);
        libro1.setGenero(GeneroLibro.FICCION);
        libro1.setDisponible(true);
        libroRepo.save(libro1);

        Libro libro2 = new Libro();
        libro2.setIsbn("0987654321");
        libro2.setNombre("Libro Ejemplo 2");
        libro2.setEditorial("Editorial Ejemplo");
        libro2.setAnio(2024);
        libro2.setGenero(GeneroLibro.FICCION);
        libro2.setDisponible(true);
        libroRepo.save(libro2);

        // Crear y guardar una reserva manualmente
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaDevolucion(LocalDate.now().plusDays(7));
        reserva.setAnio(2024);
        reserva.setUsuario(usuario);
        reserva.setLibros(new ArrayList<>(Arrays.asList(libro1, libro2)));
        Reserva reservaGuardada = reservaRepo.save(reserva);

        // Modificar la reserva
        reservaGuardada.setFechaDevolucion(LocalDate.now().plusDays(14));

        // Crear una nueva lista de libros para la reserva actualizada
        List<Libro> librosActualizados = new ArrayList<>(reservaGuardada.getLibros());
        reservaGuardada.setLibros(librosActualizados);

        Reserva reservaActualizada = reservaRepo.save(reservaGuardada);

        // Obtener la reserva actualizada
        Reserva reservaRecuperada = reservaRepo.findById(reservaGuardada.getCodigo())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Verificar que la reserva se haya actualizado correctamente
        assertEquals(reservaGuardada.getCodigo(), reservaRecuperada.getCodigo());
        assertEquals(LocalDate.now().plusDays(14), reservaRecuperada.getFechaDevolucion());
        assertEquals(2, reservaRecuperada.getLibros().size());
    }





    @Test
    @Transactional
    public void eliminarReservaTest() throws Exception {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("123456789");
        usuario.setEmail("usuario@example.com");
        usuario.setGenero(GeneroPersona.MASCULINO);
        usuario.setNombre("Juan Pérez");
        usuario.setPassword("password");
        usuarioRepo.save(usuario);

        // Crear y guardar libros para la reserva
        Libro libro1 = new Libro();
        libro1.setIsbn("1234567890");
        libro1.setNombre("Libro Ejemplo 1");
        libro1.setEditorial("Editorial Ejemplo");
        libro1.setAnio(2024);
        libro1.setGenero(GeneroLibro.FICCION);
        libro1.setDisponible(true);
        libroRepo.save(libro1);

        Libro libro2 = new Libro();
        libro2.setIsbn("0987654321");
        libro2.setNombre("Libro Ejemplo 2");
        libro2.setEditorial("Editorial Ejemplo");
        libro2.setAnio(2024);
        libro2.setGenero(GeneroLibro.FICCION);
        libro2.setDisponible(true);
        libroRepo.save(libro2);

        // Crear y guardar una reserva manualmente
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setFechaDevolucion(LocalDate.now().plusDays(10));
        reserva.setAnio(2024);
        reserva.setUsuario(usuario);
        reserva.setLibros(Arrays.asList(libro1, libro2));
        Reserva reservaGuardada = reservaRepo.save(reserva);

        // Eliminar la reserva
        reservaServicio.eliminarReserva(reservaGuardada.getCodigo());

        // Verificar que la reserva ha sido eliminada
        try {
            reservaRepo.findById(reservaGuardada.getCodigo())
                    .orElseThrow(() -> new Exception("Reserva no encontrada después de eliminar"));
            fail("Se esperaba una excepción al intentar encontrar una reserva eliminada.");
        } catch (Exception e) {
            assertEquals("Reserva no encontrada después de eliminar", e.getMessage());
        }
    }

    @Test
    @Transactional
    public void listarReservasPorUsuarioTest() throws Exception {
        // Crear y guardar un usuario
        Usuario usuario = new Usuario();
        usuario.setCedula("123456789");
        usuario.setEmail("usuario@example.com");
        usuario.setGenero(GeneroPersona.MASCULINO);
        usuario.setNombre("Juan Pérez");
        usuario.setPassword("password");
        usuarioRepo.save(usuario);

        // Crear y guardar libros para la reserva
        Libro libro1 = new Libro();
        libro1.setIsbn("1234567890");
        libro1.setNombre("Libro Ejemplo 1");
        libro1.setEditorial("Editorial Ejemplo");
        libro1.setAnio(2024);
        libro1.setGenero(GeneroLibro.FICCION);
        libro1.setDisponible(true);
        libroRepo.save(libro1);

        Libro libro2 = new Libro();
        libro2.setIsbn("0987654321");
        libro2.setNombre("Libro Ejemplo 2");
        libro2.setEditorial("Editorial Ejemplo");
        libro2.setAnio(2024);
        libro2.setGenero(GeneroLibro.FICCION);
        libro2.setDisponible(true);
        libroRepo.save(libro2);

        // Crear y guardar reservas manualmente
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva(LocalDateTime.now());
        reserva1.setFechaDevolucion(LocalDate.now().plusDays(10));
        reserva1.setAnio(LocalDate.now().getYear());
        reserva1.setUsuario(usuario);
        reserva1.setLibros(Arrays.asList(libro1, libro2));
        reservaRepo.save(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva(LocalDateTime.now().plusDays(1));
        reserva2.setFechaDevolucion(LocalDate.now().plusDays(12));
        reserva2.setAnio(LocalDate.now().getYear());
        reserva2.setUsuario(usuario);
        reserva2.setLibros(Collections.singletonList(libro2));
        reservaRepo.save(reserva2);

        // Buscar reservas por usuario
        List<ReservaDTO> reservas = reservaServicio.buscarReservasPorUsuario(usuario.getCodigo());

        // Obtener IDs de libros esperados
        List<String> libroIdsEsperados = Arrays.asList(libro1.getIsbn(), libro2.getIsbn());

        // Verificar que se recuperen las reservas correctas
        assertEquals(2, reservas.size());

        // Verificar que al menos una reserva contenga los IDs esperados
        assertTrue(reservas.stream().anyMatch(r -> libroIdsEsperados.containsAll(r.libroIds())));
    }

}
