package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.servicios.AutorServicio;
import org.fabricadegenios.servicios.LibroServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class LibroServicioTest {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;

    @Test
    public void registrarLibroTest() throws Exception {
        // Crear libro sin autores
        LibroDTO libroDTO = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            LibroDTO guardado = libroServicio.registrarLibro(libroDTO);
            assertNotNull(guardado);
            assertTrue(guardado.autorIds().isEmpty()); // No se deben asignar autores en este paso
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book registration");
        }
    }

    @Test
    public void asignarAutoresAlLibroTest() throws Exception {
        // Crear y registrar autores para la prueba
        AutorDTO autorDTO1 = new AutorDTO(null, "Gabriel García Márquez", 1927);
        AutorDTO autorDTO2 = new AutorDTO(null, "Mario Vargas Llosa", 1936);
        AutorDTO autorRegistrado1 = autorServicio.registrarAutor(autorDTO1);
        AutorDTO autorRegistrado2 = autorServicio.registrarAutor(autorDTO2);

        LibroDTO libroDTO = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libroGuardado = libroServicio.registrarLibro(libroDTO);

        libroServicio.asignarAutoresAlLibro(libroGuardado.isbn(), Arrays.asList(autorRegistrado1.id(), autorRegistrado2.id()));

        LibroDTO libroConAutores = libroServicio.obtenerLibro(libroGuardado.isbn());
        assertNotNull(libroConAutores);
        assertEquals(2, libroConAutores.autorIds().size()); // Verificar que los autores se han asignado correctamente
    }


    @Test
    public void obtenerLibroTest() throws Exception {
        LibroDTO libroDTO = new LibroDTO("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            LibroDTO guardado = libroServicio.registrarLibro(libroDTO);
            LibroDTO obtenido = libroServicio.obtenerLibro(guardado.isbn());
            assertNotNull(obtenido);
            assertEquals(guardado.isbn(), obtenido.isbn());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book retrieval");
        }
    }

    @Test
    public void actualizarLibroTest() throws Exception {
        LibroDTO libroDTO = new LibroDTO("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            LibroDTO guardado = libroServicio.registrarLibro(libroDTO);
            guardado = new LibroDTO(
                    guardado.isbn(),
                    "Cien años de soledad (Updated)",
                    guardado.editorial(),
                    guardado.anio(),
                    guardado.disponible(),
                    guardado.genero(),
                    guardado.autorIds()
            );
            LibroDTO actualizado = libroServicio.actualizarLibro(guardado);
            assertEquals("Cien años de soledad (Updated)", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book update");
        }
    }

    @Test
    public void eliminarLibroTest() throws Exception {
        LibroDTO libroDTO = new LibroDTO("1122334455", "Crónica de una muerte anunciada", "Editorial DEF", 1981, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            LibroDTO guardado = libroServicio.registrarLibro(libroDTO);

            // Eliminamos el libro
            libroServicio.eliminarLibro(guardado.isbn());

            // Intentamos obtener el libro después de eliminarlo, debería lanzar una excepción
            Exception exception = assertThrows(Exception.class, () -> {
                libroServicio.obtenerLibro(guardado.isbn());
            });
            assertEquals("El libro con el ISBN proporcionado no existe.", exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ocurrió una excepción durante la eliminación del libro");
        }
    }

    @Test
    public void listarLibrosTest() throws Exception {
        LibroDTO libroDTO1 = new LibroDTO("2233445566", "La sombra del viento", "Editorial GHI", 2001, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libroDTO2 = new LibroDTO("3344556677", "El nombre de la rosa", "Editorial JKL", 1980, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            libroServicio.registrarLibro(libroDTO1);
            libroServicio.registrarLibro(libroDTO2);
            List<LibroDTO> libros = libroServicio.listarLibros();
            assertFalse(libros.isEmpty());
            assertTrue(libros.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book listing");
        }
    }

    @Test
    public void buscarLibrosPorGeneroTest() throws Exception {
        LibroDTO libroDTO = new LibroDTO("4455667788", "Rayuela", "Editorial MNO", 1963, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            libroServicio.registrarLibro(libroDTO);

            // Buscar libros que contienen "FICCION" en el género
            List<LibroDTO> libros = libroServicio.buscarLibrosPorGenero("FICCION");
            assertFalse(libros.isEmpty());
            assertTrue(libros.stream().anyMatch(l -> l.nombre().equalsIgnoreCase("Rayuela")));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during book search by genre");
        }
    }

    @Test
    public void buscarLibrosDisponiblesPorGeneroTest() throws Exception {
        LibroDTO libroDTO = new LibroDTO("5566778899", "Los detectives salvajes", "Editorial PQR", 1998, true, GeneroLibro.FICCION, Arrays.asList());

        try {
            // Registrar el libro con una lista vacía de autores
            libroServicio.registrarLibro(libroDTO);

            // Buscar libros disponibles que contienen "FICCION" en el género
            List<LibroDTO> librosDisponibles = libroServicio.buscarLibrosDisponiblesPorGenero("FICCION", LocalDate.now());
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
        LibroDTO libroDTO1 = new LibroDTO("6677889900", "La casa de los espíritus", "Editorial STU", 1982, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libroDTO2 = new LibroDTO("7788990011", "El túnel", "Editorial VWX", 1998, true, GeneroLibro.FICCION, Arrays.asList());
        LibroDTO libroDTO3 = new LibroDTO("8899001122", "El lobo estepario", "Editorial YZA", 1927, true, GeneroLibro.FICCION, Arrays.asList());
        libroServicio.registrarLibro(libroDTO1);
        libroServicio.registrarLibro(libroDTO2);
        libroServicio.registrarLibro(libroDTO3);

        // Configuramos el paginador con ordenamiento por nombre
        Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

        // Obtenemos la página de libros
        Page<LibroDTO> lista = libroServicio.paginarLibros(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        assertNotNull(lista);
        assertTrue(lista.getTotalElements() > 0); // Verifica que haya elementos en total
        assertTrue(lista.getSize() <= 2); // Verifica que el tamaño de la página no exceda el tamaño máximo
        assertEquals(2, lista.getNumberOfElements()); // Verifica el número de elementos en la página actual

        // Imprimimos la página de libros ordenada por nombre
        System.out.println("Página de libros ordenada por nombre:");
        lista.getContent().forEach(libro -> System.out.println(libro.nombre()));
    }
}

