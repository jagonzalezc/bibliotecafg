package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.servicios.AutorServicio;
import org.fabricadegenios.servicios.LibroServicio;
import org.junit.Assert;
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

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class LibroServicioTest {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;

    @Test
    public void registrarLibroTest() throws Exception {
        // Crear y registrar autores para la prueba
        Autor autor1 = new Autor("Gabriel García Márquez", 1927);
        Autor autor2 = new Autor("Mario Vargas Llosa", 1936);
        autor1 = autorServicio.registrarAutor(autor1);
        autor2 = autorServicio.registrarAutor(autor2);

        List<Long> autorIds = Arrays.asList(autor1.getCodigo(), autor2.getCodigo());
        Libro libro = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);

        try {
            Libro guardado = libroServicio.registrarLibro(libro, autorIds);
            Assert.assertNotNull(guardado);
            Assert.assertEquals(autorIds.size(), guardado.getAutores().size());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book registration");
        }
    }

    @Test
    public void obtenerLibroTest() {
        Libro libro = new Libro("0987654321", "El otoño del patriarca", "Editorial ABC", 1975, true, GeneroLibro.FICCION);

        try {
            // Registrar el libro con una lista vacía de autores
            Libro guardado = libroServicio.registrarLibro(libro, Arrays.asList());
            Libro obtenido = libroServicio.obtenerLibro(guardado.getIsbn());
            Assert.assertNotNull(obtenido);
            Assert.assertEquals(guardado.getIsbn(), obtenido.getIsbn());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book retrieval");
        }
    }


    @Test
    public void actualizarLibroTest() {
        Libro libro = new Libro("1234567890", "Cien años de soledad", "Editorial XYZ", 1967, true, GeneroLibro.FICCION);

        try {
            // Registrar el libro con una lista vacía de autores
            Libro guardado = libroServicio.registrarLibro(libro, Arrays.asList());
            guardado.setNombre("Cien años de soledad (Updated)");
            Libro actualizado = libroServicio.actualizarLibro(guardado);
            Assert.assertEquals("Cien años de soledad (Updated)", actualizado.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book update");
        }
    }

    @Test
    public void eliminarLibroTest() throws Exception {
        Libro libro = new Libro("1122334455", "Crónica de una muerte anunciada", "Editorial DEF", 1981, true, GeneroLibro.FICCION);

        try {
            // Registrar el libro con una lista vacía de autores
            Libro guardado = libroServicio.registrarLibro(libro, Arrays.asList());

            // Eliminamos el libro
            libroServicio.eliminarLibro(guardado.getIsbn());

            // Intentamos obtener el libro después de eliminarlo, debería lanzar una excepción
            try {
                libroServicio.obtenerLibro(guardado.getIsbn());
                Assert.fail("Se esperaba una excepción al intentar obtener un libro eliminado");
            } catch (Exception e) {
                // Verificamos que la excepción lanzada sea la esperada
                Assert.assertEquals("El libro con el ISBN proporcionado no existe.", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Ocurrió una excepción durante la eliminación del libro");
        }
    }

    @Test
    public void listarLibrosTest() {
        Libro libro1 = new Libro("2233445566", "La sombra del viento", "Editorial GHI", 2001, true, GeneroLibro.FICCION);
        Libro libro2 = new Libro("3344556677", "El nombre de la rosa", "Editorial JKL", 1980, true, GeneroLibro.FICCION);

        try {
            libroServicio.registrarLibro(libro1, Arrays.asList());
            libroServicio.registrarLibro(libro2, Arrays.asList());
            List<Libro> libros = libroServicio.listarLibros();
            Assert.assertFalse(libros.isEmpty());
            Assert.assertTrue(libros.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book listing");
        }
    }


    @Test
    public void buscarLibrosPorGeneroTest() {
        Libro libro = new Libro("4455667788", "Rayuela", "Editorial MNO", 1963, true, GeneroLibro.FICCION);

        try {
            // Registrar el libro con una lista vacía de autores
            libroServicio.registrarLibro(libro, Arrays.asList());

            // Buscar libros que contienen "FICCION" en el género
            List<Libro> libros = libroServicio.buscarLibrosPorGenero("FICCION");
            Assert.assertFalse(libros.isEmpty());
            Assert.assertTrue(libros.stream().anyMatch(l -> l.getNombre().equalsIgnoreCase("Rayuela")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book search by genre");
        }
    }


    @Test
    public void buscarLibrosDisponiblesPorGeneroTest() {
        Libro libro = new Libro("5566778899", "Los detectives salvajes", "Editorial PQR", 1998, true, GeneroLibro.FICCION);

        try {
            // Registrar el libro con una lista vacía de autores
            libroServicio.registrarLibro(libro, Arrays.asList());

            // Buscar libros disponibles que contienen "FICCION" en el género
            List<Libro> librosDisponibles = libroServicio.buscarLibrosDisponiblesPorGenero("FICCION", LocalDate.now());
            Assert.assertFalse(librosDisponibles.isEmpty());
            Assert.assertTrue(librosDisponibles.stream().anyMatch(l -> l.getNombre().equalsIgnoreCase("Los detectives salvajes")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during available book search by genre");
        }
    }


    @Test
    public void paginarLibrosTest() {
        // Crear libros para la prueba
        try {
            Libro libro1 = new Libro("6677889900", "La casa de los espíritus", "Editorial STU", 1982, true, GeneroLibro.FICCION);
            Libro libro2 = new Libro("7788990011", "El túnel", "Editorial VWX", 1998, true, GeneroLibro.FICCION);
            Libro libro3 = new Libro("8899001122", "El lobo estepario", "Editorial YZA", 1927, true, GeneroLibro.FICCION);
            libroServicio.registrarLibro(libro1, Arrays.asList());
            libroServicio.registrarLibro(libro2, Arrays.asList());
            libroServicio.registrarLibro(libro3, Arrays.asList());

            // Configuramos el paginador con ordenamiento por nombre
            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

            // Obtenemos la página de libros
            Page<Libro> lista = libroServicio.paginarLibros(pagina);

            // Verificamos que la página no esté vacía y tenga el tamaño esperado
            Assert.assertNotNull(lista);
            Assert.assertTrue(lista.getTotalElements() > 0); // Verifica que haya elementos en total
            Assert.assertTrue(lista.getSize() <= 2); // Verifica que el tamaño de la página no exceda el tamaño máximo
            Assert.assertEquals(2, lista.getNumberOfElements()); // Verifica el número de elementos en la página actual

            // Imprimimos la página de libros ordenada por nombre
            System.out.println("Página de libros ordenada por nombre:");
            lista.getContent().forEach(libro -> System.out.println(libro.getNombre()));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during book pagination test");
        }
    }
}


