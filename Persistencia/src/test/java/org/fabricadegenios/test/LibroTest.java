package org.fabricadegenios.test;

import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.repositorios.AutorRepo;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:data.sql") // Carga el archivo data.sql antes de ejecutar las pruebas
public class LibroTest {

    @Autowired
    private LibroRepo libroRepo;
    @Autowired
    private AutorRepo autorRepo;

    @Test
    public void registrarLibroTest() {
        Libro libro = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);

        // Guardamos el registro
        Libro guardado = libroRepo.save(libro);

        // Comprobamos que se haya guardado correctamente
        assertNotNull(guardado);
        Assertions.assertEquals("123-456-789", guardado.getIsbn()); // Verificar que el ISBN es correcto
        System.out.println("Libro guardado: " + guardado);
    }

    @Test
    public void eliminarLibroTest() {
        Libro libro = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);

        // Primero lo guardamos
        Libro registrado = libroRepo.save(libro);

        // Luego lo eliminamos
        libroRepo.delete(registrado);

        // Verificamos que se haya eliminado correctamente
        Libro buscado = libroRepo.findById("123-456-789").orElse(null);
        Assertions.assertNull(buscado);
        System.out.println("Libro eliminado correctamente.");
    }

    @Test
    public void actualizarLibroTest() {
        Libro libro = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);

        // Primero lo guardamos
        Libro registrado = libroRepo.save(libro);

        // Modificamos el nombre
        registrado.setNombre("Don Quijote de la Mancha");

        // Guardamos el registro modificado
        Libro actualizado = libroRepo.save(registrado);

        // Verificamos que se haya actualizado correctamente
        Libro buscado = libroRepo.findById("123-456-789").orElse(null);
        assertNotNull(buscado);
        Assertions.assertEquals("Don Quijote de la Mancha", buscado.getNombre());
        System.out.println("Libro actualizado: " + buscado);
    }

    @Test
    public void listarLibrosTest() {
        // Creamos y guardamos dos libros
        Libro libro1 = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", "Oveja negra", 1967, true, GeneroLibro.LITERATURA);
        libroRepo.save(libro1);
        libroRepo.save(libro2);

        // Obtenemos la lista de todos los libros
        List<Libro> lista = libroRepo.findAll();

        // Verificamos que la lista no esté vacía y tenga al menos 2 libros
        assertNotNull(lista);
        Assertions.assertTrue(lista.size() >= 2);

        // Imprimimos la lista de libros
        lista.forEach(System.out::println);
    }

    @Test
    public void paginarLibrosTest() {
        // Configuramos el paginador con ordenamiento por ISBN
        Pageable pagina = PageRequest.of(0, 2, Sort.by("isbn"));

        // Creamos y guardamos libros para paginar
        Libro libro1 = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", "Oveja negra", 1967, true, GeneroLibro.LITERATURA);
        Libro libro3 = new Libro("111-222-333", "Crónica de una Muerte Anunciada", "Oveja negra", 1981, true, GeneroLibro.FICCION);
        libroRepo.save(libro1);
        libroRepo.save(libro2);
        libroRepo.save(libro3);

        // Obtenemos la página de libros
        Page<Libro> lista = libroRepo.findAll(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        assertNotNull(lista);
        Assertions.assertTrue(lista.getContent().size() <= 2); // Verificamos que no exceda el tamaño de la página

        // Imprimimos la página de libros ordenada por ISBN
        System.out.println("Página de libros ordenada por ISBN:");
        lista.getContent().forEach(System.out::println);
    }

    @Test
    public void filtrarPorGeneroTest() {
        // Creamos y guardamos libros
        Libro libro1 = new Libro("123-456-789", "El Quijote", "Planeta", 1605, true, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", "Oveja negra", 1967, true, GeneroLibro.LITERATURA);
        Libro libro3 = new Libro("111-222-333", "Crónica de una Muerte Anunciada", "Oveja negra", 1981, true, GeneroLibro.FICCION);
        libroRepo.save(libro1);
        libroRepo.save(libro2);
        libroRepo.save(libro3);

        // Filtramos por género LITERATURA
        List<Libro> literaturaLibros = libroRepo.findAllByGenero(String.valueOf(GeneroLibro.LITERATURA));

        // Verificamos que la lista no esté vacía y contenga los libros de género LITERATURA
        assertNotNull(literaturaLibros);
        Assertions.assertTrue(literaturaLibros.size() > 0);
        literaturaLibros.forEach(System.out::println);
    }

    @Test
    public void paginarLibrosPorAutorTest() {
        // Crear y guardar autor
        Autor autor = new Autor();
        autor.setNombre("Gabriel García Márquez");
        autorRepo.save(autor);

        // Crear y guardar libros
        Libro libro1 = new Libro("978-3-16-148410-0", "Cien años de soledad", "Oveja negra", 1967, true, GeneroLibro.LITERATURA);
        libro1.setAutores(List.of(autor));
        libroRepo.save(libro1);

        Libro libro2 = new Libro("978-0-06-088328-7", "El amor en los tiempos del cólera", "Oveja negra", 1985, true, GeneroLibro.LITERATURA);
        libro2.setAutores(List.of(autor));
        libroRepo.save(libro2);

        Libro libro3 = new Libro("978-1-56619-909-4", "Crónica de una muerte anunciada", "Oveja negra", 1981, true, GeneroLibro.LITERATURA);
        libro3.setAutores(List.of(autor));
        libroRepo.save(libro3);

        // Configuramos el paginador con ordenamiento por nombre de libro
        Pageable pagina = PageRequest.of(0, 2, Sort.by("anio"));

        // Obtenemos la página de libros por autor
        Page<Libro> lista = libroRepo.findByAutorNombre("Gabriel García Márquez", pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        assertNotNull(lista);
        Assertions.assertTrue(lista.getSize() > 0);

        // Imprimimos la página de libros ordenada por anio
        System.out.println("Página de libros ordenada por anio:");
        lista.getContent().forEach(System.out::println);
    }

    @Test
    public void testAvailableBooksByGenre() {
        // Definir la fecha actual
        LocalDate hoy = LocalDate.now();

        // Encontrar libros disponibles por género
        List<Libro> librosDisponibles = libroRepo.findAvailableBooksByGenre(String.valueOf(GeneroLibro.LITERATURA), hoy);

        // Verificar que los resultados no sean nulos
        assertNotNull(librosDisponibles);
        // Imprimir los libros disponibles
        librosDisponibles.forEach(System.out::println);
    }
}


