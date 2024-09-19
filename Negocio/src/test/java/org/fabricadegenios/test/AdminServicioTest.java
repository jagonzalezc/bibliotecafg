package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.dto.ReservaDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.dto.admin.RegistroAutorDTO;
import org.fabricadegenios.dto.admin.RegistroLibroDTO;
import org.fabricadegenios.dto.admin.RegistroReservaDTO;
import org.fabricadegenios.dto.admin.RegistroUsuarioDTO;
import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.servicios.AdministradorServicio;
import org.fabricadegenios.servicios.UsuarioServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class AdminServicioTest {


    @Autowired
    private AdministradorServicio administradorServicio;

    @Test
    public void registrarUsuarioTest() {
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("111", "Maria Lopez", "maria@gmail.com", "clave123", "FEMENINO", "USER");

        try {
            // Imprimimos información del DTO antes de guardar
            System.out.println("Intentando registrar el usuario: " + registroUsuarioDTO);

            // Guardamos el usuario usando DTO
            Long nuevo = administradorServicio.registrarUsuario(registroUsuarioDTO);

            // Imprimimos el ID del nuevo usuario
            System.out.println("Usuario registrado con ID: " + nuevo);

            // Comprobamos que el ID no sea cero (usuario registrado correctamente)
            Assertions.assertNotEquals(0, nuevo);

            // Imprimimos confirmación de éxito
            System.out.println("Test completado exitosamente: Usuario registrado.");
        } catch (Exception e) {
            // Imprimimos el error en caso de excepción
            e.printStackTrace();
            Assertions.fail("Ocurrió una excepción durante el registro del usuario: " + e.getMessage());
        }
    }


    @Test
    public void obtenerUsuarioTest() {
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO( "222", "Carlos", "carlos@mail.com", "clave456", "MASCULINO", "USER");
        try {
            Long guardado = administradorServicio.registrarUsuario(registroUsuarioDTO);
            // Verificamos si el usuario fue registrado
            Assertions.assertNotEquals(0, guardado);
            Long nuevoUsuarioId = guardado; // Convertimos el valor devuelto a Long, si es necesario

            UsuarioDTO obtenido = administradorServicio.obtenerUsuario(nuevoUsuarioId);
            // Verificamos que el usuario no sea nulo y que el ID coincida
            Assertions.assertNotNull(obtenido);
            Assertions.assertEquals(obtenido.id(), nuevoUsuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user retrieval: " + e.getMessage());
        }
    }

    @Test
    public void actualizarUsuarioTest() {
        // Creación de un DTO de usuario con datos iniciales
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("333", "Ana", "ana@mail.com", "clave789", "FEMENINO", "USER");

        try {
            // Registra un nuevo usuario
            Long guardado = administradorServicio.registrarUsuario(registroUsuarioDTO);

            // Actualiza los datos del usuario previamente guardado
            RegistroUsuarioDTO dtoActualizado = new RegistroUsuarioDTO("333", "Ana Updated", "ana@mail.com", "clave789", "FEMENINO", "USER");

            // Llama al método de actualizar usuario
            UsuarioDTO actualizado = administradorServicio.actualizarUsuario(guardado, dtoActualizado);

            // Comprueba que el nombre fue actualizado correctamente
            Assertions.assertEquals("Ana Updated", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user update: " + e.getMessage());
        }
    }

    @Test
    public void eliminarUsuarioTest() {
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");

        try {
            // Registrar usuario y obtener el ID generado
            Long guardado = administradorServicio.registrarUsuario(registroUsuarioDTO);

            // Verificamos que el ID del usuario registrado no sea nulo o 0
            assertNotNull(guardado, "El ID del usuario guardado no debería ser nulo");
            assertNotEquals(0L, guardado, "El ID del usuario guardado no debería ser 0");

            // Eliminar usuario registrado
            administradorServicio.eliminarUsuario(guardado);

            // Intentar obtener el usuario eliminado por ID
            try {
                UsuarioDTO usuarioDTO = administradorServicio.obtenerUsuario(guardado);
                fail("Se esperaba una excepción al intentar obtener un usuario eliminado");

            } catch (Exception e) {
                // Verificar que el mensaje de la excepción coincida
                assertEquals("El usuario con el código proporcionado no existe.", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la eliminación del usuario");
        }
    }


    @Test
    public void listarUsuariosTest() {
        RegistroUsuarioDTO registroUsuarioDTO1 = new RegistroUsuarioDTO( "555", "Maria", "maria@mail.com", "clave111", "FEMENINO", "USER");
        RegistroUsuarioDTO registroUsuarioDTO2 = new RegistroUsuarioDTO( "666", "Pedro", "pedro@mail.com", "clave222", "MASCULINO", "USER");

        try {
            administradorServicio.registrarUsuario(registroUsuarioDTO1);
            administradorServicio.registrarUsuario(registroUsuarioDTO2);
            // Listamos todos los usuarios
            var usuarios = administradorServicio.listarUsuarios();
            Assertions.assertFalse(usuarios.isEmpty());
            Assertions.assertTrue(usuarios.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user listing: " + e.getMessage());
        }
    }

    @Test
    public void obtenerUsuarioPorCedulaTest() {
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO( "777", "Sofia", "sofia@mail.com", "clave333", "FEMENINO", "USER");

        try {
            Long guardado = administradorServicio.registrarUsuario(registroUsuarioDTO);
            UsuarioDTO obtenido = administradorServicio.obtenerUsuario(guardado);
            UsuarioDTO obtenido2 = administradorServicio.obtenerUsuariosPorCedula(obtenido.cedula());
            Assertions.assertNotNull(obtenido2);
            Assertions.assertEquals(obtenido.cedula(), obtenido2.cedula());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user retrieval by cedula: " + e.getMessage());
        }
    }

    @Test
    public void buscarUsuarioPorNombreTest() {
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO( "888", "Gonzalo", "gonzalo@mail.com", "clave444", "MASCULINO", "USER");

        try {
            administradorServicio.registrarUsuario(registroUsuarioDTO);
            var usuarios = administradorServicio.findAllByNombreContainsIgnoreCase("gonzalo");
            Assertions.assertFalse(usuarios.isEmpty());
            Assertions.assertTrue(usuarios.stream().anyMatch(user -> user.nombre().equalsIgnoreCase("Gonzalo")));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user search by name: " + e.getMessage());
        }
    }

    @Test
    public void paginarUsuariosTest() throws Exception {
        try {
            RegistroUsuarioDTO dto1 = new RegistroUsuarioDTO( "123", "Ana", "ana@mail.com", "clave123", "FEMENINO", "USER");
            RegistroUsuarioDTO dto2 = new RegistroUsuarioDTO( "456", "Luis", "luis@mail.com", "clave456", "MASCULINO", "USER");
            RegistroUsuarioDTO dto3 = new RegistroUsuarioDTO( "789", "Gonzalo", "gonzalo@mail.com", "clave789", "MASCULINO", "USER");

            administradorServicio.registrarUsuario(dto1);
            administradorServicio.registrarUsuario(dto2);
            administradorServicio.registrarUsuario(dto3);

            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));
            Page<UsuarioDTO> lista = administradorServicio.paginarUsuarios(pagina);

            Assertions.assertNotNull(lista);
            Assertions.assertTrue(lista.getTotalElements() > 0);
            Assertions.assertTrue(lista.getSize() <= 2);
            Assertions.assertEquals(2, lista.getNumberOfElements());

            System.out.println("Página de usuarios ordenada por nombre:");
            lista.getContent().forEach(user -> System.out.println(user.nombre()));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user pagination test: " + e.getMessage());
        }
    }

    @Test
    public void registrarAutorTest() {
        RegistroAutorDTO registroAutorDTO = new RegistroAutorDTO("Gabriel Garcia Marquez", 1927);

        try {
            Long nuevo = administradorServicio.registrarAutor(registroAutorDTO);
            // Comprobamos que si haya quedado
            Assertions.assertNotEquals(0,nuevo);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user registration: " + e.getMessage());
        }
    }

    @Test
    public void obtenerAutorTest() {
        RegistroAutorDTO registroAutorDTO = new RegistroAutorDTO("Jorge Luis Borges", 1899);

        try {
            Long guardado = administradorServicio.registrarAutor(registroAutorDTO);
            AutorDTO obtenido = administradorServicio.obtenerAutor(guardado);
            assertNotNull(obtenido);
            assertEquals(guardado, obtenido.id());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author retrieval");
        }
    }

    @Test
    public void actualizarAutorTest() {
        RegistroAutorDTO registroAutorDTO = new RegistroAutorDTO("Julio Cortázar", 1914);

        try {
            Long guardado = administradorServicio.registrarAutor(registroAutorDTO);
            RegistroAutorDTO actualizadoDTO = new RegistroAutorDTO("Julio Cortázar Updated", 1914);
            AutorDTO actualizado = administradorServicio.actualizarAutor(guardado, actualizadoDTO);
            //comprobar que actuaalizó nombre
            assertEquals("Julio Cortázar Updated", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author update");
        }
    }

    @Test
    public void eliminarAutorTest() throws Exception {
        RegistroAutorDTO registroAutorDTO = new RegistroAutorDTO("Isabel Allende", 1942);

        try {
            Long guardado = administradorServicio.registrarAutor(registroAutorDTO);
            administradorServicio.eliminarAutor(guardado);

            try {
                administradorServicio.obtenerAutor(guardado);
                fail("Se esperaba una excepción al intentar obtener un autor eliminado");
            } catch (Exception e) {
                assertEquals("El autor con el código proporcionado no existe.", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la eliminación del autor");
        }
    }

    @Test
    public void listarAutoresTest() {
        RegistroAutorDTO autorDTO1 = new RegistroAutorDTO( "Mario Vargas Llosa", 1936);
        RegistroAutorDTO autorDTO2 = new RegistroAutorDTO( "Pablo Neruda", 1904);

        try {
            administradorServicio.registrarAutor(autorDTO1);
            administradorServicio.registrarAutor(autorDTO2);
            List<AutorDTO> autores = administradorServicio.listarAutores();
            assertFalse(autores.isEmpty());
            assertTrue(autores.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author listing");
        }
    }

    @Test
    public void buscarAutorPorNombreTest() {
        RegistroAutorDTO autorDTO = new RegistroAutorDTO( "Gabriel Garcia Marquez", 1927);

        try {
            administradorServicio.registrarAutor(autorDTO);
            Pageable pageable = PageRequest.of(0, 10);
            Page<AutorDTO> autores = administradorServicio.buscarAutoresPorNombre("Gabriel", pageable);
            assertFalse(autores.getContent().isEmpty());
            assertTrue(autores.getContent().stream()
                    .anyMatch(a -> a.nombre().equalsIgnoreCase("Gabriel Garcia Marquez")));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author search by name");
        }
    }

    @Test
    public void paginarAutoresTest() {
        try {
            RegistroAutorDTO autorDTO1 = new RegistroAutorDTO( "Ana Maria Matute", 1925);
            RegistroAutorDTO autorDTO2 = new RegistroAutorDTO( "Octavio Paz", 1914);
            RegistroAutorDTO autorDTO3 = new RegistroAutorDTO( "Roberto Bolaño", 1953);
            administradorServicio.registrarAutor(autorDTO1);
            administradorServicio.registrarAutor(autorDTO2);
            administradorServicio.registrarAutor(autorDTO3);

            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));
            Page<AutorDTO> lista = administradorServicio.paginarAutores(pagina);

            assertNotNull(lista);
            assertTrue(lista.getTotalElements() > 0);
            assertTrue(lista.getSize() <= 2);
            assertEquals(2, lista.getNumberOfElements());

            System.out.println("Página de autores ordenada por nombre:");
            lista.getContent().forEach(a -> System.out.println(a.nombre()));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author pagination test");
        }
    }

    @Test
    public void registrarLibroTest() throws Exception {
        // Crear libro sin autores
        RegistroLibroDTO registroLibroDTO = new RegistroLibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());

        try {
            Long libroId = administradorServicio.registrarLibro(registroLibroDTO);
            // Verificar que el ID del libro no sea nulo
            assertNotNull(libroId);
            // Obtener el libro recién registrado para verificar su contenido
            LibroDTO libroDTO = administradorServicio.obtenerLibro(libroId);
            // Verificar que no se hayan asignado autores al libro
            assertTrue(libroDTO.autorIds().isEmpty(), "El libro no debería tener autores asignados en este paso");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book registration");
        }
    }

    @Test
    public void asignarAutoresAlLibroTest() throws Exception {
        // Crear y registrar autores para la prueba
        RegistroAutorDTO registroAutorDTO1 = new RegistroAutorDTO("Gabriel García Márquez", 1927);
        RegistroAutorDTO registroAutorDTO2 = new RegistroAutorDTO("Mario Vargas Llosa", 1936);

        // Registrar autores y obtener sus IDs
        Long autorRegistrado1 = administradorServicio.registrarAutor(registroAutorDTO1);
        Long autorRegistrado2 = administradorServicio.registrarAutor(registroAutorDTO2);

        // Crear y registrar un libro para la prueba
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());
        Long codigoLibroGuardado = administradorServicio.registrarLibro(libroDTO);

        // Asignar los autores al libro usando el servicio
        LibroDTO libroConAutores = administradorServicio.asignarAutoresAlLibro(codigoLibroGuardado, Arrays.asList(autorRegistrado1, autorRegistrado2));

        // Verificar que el libro no es nulo y que los autores se han asignado correctamente
        assertNotNull(libroConAutores);
        assertEquals(2, libroConAutores.autorIds().size());
        assertTrue(libroConAutores.autorIds().contains(autorRegistrado1));
        assertTrue(libroConAutores.autorIds().contains(autorRegistrado2));
    }


    @Test
    public void obtenerLibroTest() throws Exception {
        RegistroLibroDTO registroLibroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            Long guardado = administradorServicio.registrarLibro(registroLibroDTO);
            LibroDTO obtenido = administradorServicio.obtenerLibro(guardado);
            assertNotNull(obtenido);
            assertEquals(guardado, obtenido.codigo());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book retrieval");
        }
    }

    @Test
    public void actualizarLibroTest() throws Exception {
        // Crear libro sin autores
        RegistroLibroDTO registroLibroDTO = new RegistroLibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());
        RegistroLibroDTO registroLibroDTO2 = new RegistroLibroDTO("1234567890", "Cien años de soledad Updated", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());

        try {
            Long libroId = administradorServicio.registrarLibro(registroLibroDTO);
            // Verificar que el ID del libro no sea nulo
            assertNotNull(libroId);
            // Obtener el libro recién registrado para verificar su contenido
            LibroDTO libroDTO = administradorServicio.obtenerLibro(libroId);
            LibroDTO actualizado = administradorServicio.actualizarLibro(libroId, registroLibroDTO2);
            assertEquals("Cien años de soledad Updated", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book registration");
        }
    }
    //LibroDTO actualizado = administradorServicio.actualizarLibro(guardado);
    //assertEquals("Cien años de soledad (Updated)", actualizado.nombre());
    @Test
    public void eliminarLibroTest() throws Exception {
        // Crear libro sin autores
        RegistroLibroDTO registroLibroDTO = new RegistroLibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());

        try {
            Long libroId = administradorServicio.registrarLibro(registroLibroDTO);
            // Verificar que el ID del libro no sea nulo
            assertNotNull(libroId);
            // Eliminamos el libro
            administradorServicio.eliminarLibro(libroId);

            // Intentamos obtener el libro después de eliminarlo, debería lanzar una excepción
            Exception exception = assertThrows(Exception.class, () -> {
                administradorServicio.obtenerLibro(libroId);
            });
            assertEquals("El libro con el código proporcionado no existe.", exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la eliminación del libro");
        }
    }

    @Test
    public void listarLibrosTest() throws Exception {
        RegistroLibroDTO libroDTO1 = new RegistroLibroDTO("2233445566", "La sombra del viento", "Editorial GHI", 2001, true, "FICCION", Arrays.asList());
        RegistroLibroDTO libroDTO2 = new RegistroLibroDTO("3344556677", "El nombre de la rosa", "Editorial JKL", 1980, true, "FICCION", Arrays.asList());

        try {
            administradorServicio.registrarLibro(libroDTO1);
            administradorServicio.registrarLibro(libroDTO2);
            List<LibroDTO> libros = administradorServicio.listarLibros();
            assertFalse(libros.isEmpty());
            assertTrue(libros.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book listing");
        }
    }

    @Test
    public void buscarLibrosPorGeneroTest() throws Exception {
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("4455667788", "Rayuela", "Editorial MNO", 1963, true, "FICCION", Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            administradorServicio.registrarLibro(libroDTO);

            // Buscar libros que contienen "FICCION" en el género
            List<LibroDTO> libros = administradorServicio.buscarLibrosPorGenero("FICCION");
            assertFalse(libros.isEmpty());
            assertTrue(libros.stream().anyMatch(l -> l.nombre().equalsIgnoreCase("Rayuela")));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book search by genre");
        }
    }

    @Test
    public void buscarLibrosDisponiblesPorGeneroTest() throws Exception {
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("5566778899", "Los detectives salvajes", "Editorial PQR", 1998, true, "FICCION", Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            administradorServicio.registrarLibro(libroDTO);

            // Buscar libros disponibles que contienen "FICCION" en el género
            List<LibroDTO> librosDisponibles = administradorServicio.buscarLibrosDisponiblesPorGenero("FICCION", LocalDate.now());
            assertFalse(librosDisponibles.isEmpty());
            assertTrue(librosDisponibles.stream().anyMatch(l -> l.nombre().equalsIgnoreCase("Los detectives salvajes")));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during available book search by genre");
        }
    }

    @Test
    public void paginarLibrosTest() throws Exception {
        // Crear libros para la prueba
        RegistroLibroDTO libroDTO1 = new RegistroLibroDTO("6677889900", "La casa de los espíritus", "Editorial STU", 1982, true, "FICCION", Arrays.asList());
        RegistroLibroDTO libroDTO2 = new RegistroLibroDTO("7788990011", "El túnel", "Editorial VWX", 1998, true, "FICCION", Arrays.asList());
        RegistroLibroDTO libroDTO3 = new RegistroLibroDTO("8899001122", "El lobo estepario", "Editorial YZA", 1927, true, "FICCION", Arrays.asList());
        administradorServicio.registrarLibro(libroDTO1);
        administradorServicio.registrarLibro(libroDTO2);
        administradorServicio.registrarLibro(libroDTO3);

        // Configuramos el paginador con ordenamiento por nombre
        Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

        // Obtenemos la página de libros
        Page<LibroDTO> lista = administradorServicio.paginarLibros(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        assertNotNull(lista);
        assertTrue(lista.getTotalElements() > 0); // Verifica que haya elementos en total
        assertTrue(lista.getSize() <= 2); // Verifica que el tamaño de la página no exceda el tamaño máximo
        assertEquals(2, lista.getNumberOfElements()); // Verifica el número de elementos en la página actual

        // Imprimimos la página de libros ordenada por nombre
        System.out.println("Página de libros ordenada por nombre:");
        lista.getContent().forEach(libro -> System.out.println(libro.nombre()));
    }

    //PRUEBAS DE RESERVA
    @Test
    public void registrarReservaTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar un libro
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId = administradorServicio.registrarLibro(libroDTO);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(

                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        try {
            Long reservaId = administradorServicio.registrarReserva(reservaDTO);
            // Verificar que el ID de la reserva no sea nulo
            assertNotNull(reservaId);

            // Obtener la reserva recién registrada
            ReservaDTO reservaObtenida = administradorServicio.obtenerReserva(reservaId);
            assertNotNull(reservaObtenida);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during reservation registration");
        }
    }

    @Test
    public void asignarLibrosAReservaTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar libros
        RegistroLibroDTO libroDTO1 = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId1 = administradorServicio.registrarLibro(libroDTO1);

        RegistroLibroDTO libroDTO2 = new RegistroLibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, "FICCION", Arrays.asList());
        Long libroId2 = administradorServicio.registrarLibro(libroDTO2);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId1)
        );

        Long reservaId = administradorServicio.registrarReserva(reservaDTO);

        // Asignar un nuevo libro a la reserva
        administradorServicio.asignarLibrosAReserva(reservaId, Arrays.asList(libroId2));

        // Obtener la reserva actualizada
        ReservaDTO reservaObtenida = administradorServicio.obtenerReserva(reservaId);
        assertNotNull(reservaObtenida);
        assertTrue(reservaObtenida.libroIds().contains(libroId1));
        assertTrue(reservaObtenida.libroIds().contains(libroId2));
    }

    @Test
    public void obtenerReservaTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar un libro
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId = administradorServicio.registrarLibro(libroDTO);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        Long reservaId = administradorServicio.registrarReserva(reservaDTO);

        // Obtener la reserva
        ReservaDTO reservaObtenida = administradorServicio.obtenerReserva(reservaId);
        assertNotNull(reservaObtenida);
        assertEquals(reservaDTO.fechaReserva(), reservaObtenida.fechaReserva());
        assertEquals(reservaDTO.fechaDevolucion(), reservaObtenida.fechaDevolucion());
        assertEquals(reservaDTO.anio(), reservaObtenida.anio());
        assertEquals(reservaDTO.usuarioId(), reservaObtenida.usuarioId());
        assertEquals(reservaDTO.libroIds(), reservaObtenida.libroIds());
    }

    @Test
    public void actualizarReservaTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar un libro
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId = administradorServicio.registrarLibro(libroDTO);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        Long reservaId = administradorServicio.registrarReserva(reservaDTO);

        // Crear un nuevo DTO para actualización
        RegistroReservaDTO reservaActualizadaDTO = new RegistroReservaDTO(
                LocalDateTime.now().plusDays(1),
                LocalDate.now().plusDays(10),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        administradorServicio.actualizarReserva(reservaId, reservaActualizadaDTO);

        // Obtener la reserva actualizada
        ReservaDTO reservaObtenida = administradorServicio.obtenerReserva(reservaId);
        assertNotNull(reservaObtenida);
        assertEquals(reservaActualizadaDTO.fechaReserva(), reservaObtenida.fechaReserva());
        assertEquals(reservaActualizadaDTO.fechaDevolucion(), reservaObtenida.fechaDevolucion());
    }

    @Test
    public void eliminarReservaTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar un libro
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId = administradorServicio.registrarLibro(libroDTO);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        // Registrar la reserva
        Long reservaId = administradorServicio.registrarReserva(reservaDTO);
        assertNotNull(reservaId);

        // Verificar que la reserva se ha registrado
        ReservaDTO reservaObtenida = administradorServicio.obtenerReserva(reservaId);
        assertNotNull(reservaObtenida);

        // Eliminar la reserva
        administradorServicio.eliminarReserva(reservaId);

        // Verificar que la reserva ha sido eliminada
        Exception exception = assertThrows(RuntimeException.class, () -> {
            administradorServicio.obtenerReserva(reservaId);
        });
        assertEquals("La reserva con el código proporcionado no existe.", exception.getMessage());

        // Verificar que los libros se han marcado como disponibles
        LibroDTO libroObtenido = administradorServicio.obtenerLibro(libroId);
        assertTrue(libroObtenido.disponible());
    }
    @Test
    public void buscarReservasPorUsuarioTest() throws Exception {
        // Crear y registrar un usuario
        RegistroUsuarioDTO registroUsuarioDTO = new RegistroUsuarioDTO("444", "Luis", "luis@mail.com", "clave000", "MASCULINO", "USER");
        Long usuarioId = administradorServicio.registrarUsuario(registroUsuarioDTO);

        // Crear y registrar un libro
        RegistroLibroDTO libroDTO = new RegistroLibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, "FICCION", Arrays.asList());
        Long libroId = administradorServicio.registrarLibro(libroDTO);

        // Crear DTO para reserva
        RegistroReservaDTO reservaDTO = new RegistroReservaDTO(
                LocalDateTime.now(),
                LocalDate.now().plusDays(7),
                1975,
                usuarioId,
                Arrays.asList(libroId)
        );

        administradorServicio.registrarReserva(reservaDTO);

        // Buscar reservas por usuario
        List<ReservaDTO> reservas = administradorServicio.buscarReservasPorUsuario(usuarioId);
        assertNotNull(reservas);
        assertFalse(reservas.isEmpty());
        assertTrue(reservas.stream().anyMatch(r -> r.usuarioId().equals(usuarioId)));
    }


}

