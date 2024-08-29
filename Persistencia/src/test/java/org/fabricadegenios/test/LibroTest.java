package org.fabricadegenios.test;

import org.fabricadegenios.model.GeneroLibro;
import org.fabricadegenios.model.Libro;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:data.sql") // Carga el archivo data.sql antes de ejecutar las pruebas
public class LibroTest {

    @Autowired
    private LibroRepo libroRepo;

    @Test
    public void registrarLibroTest() {
        Libro libro = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);

        // Guardamos el registro
        Libro guardado = libroRepo.save(libro);

        // Comprobamos que se haya guardado correctamente
        Assertions.assertNotNull(guardado);
        Assertions.assertEquals("123-456-789", guardado.getIsbn()); // Verificar que el ISBN es correcto
        System.out.println("Libro guardado: " + guardado);
    }

    @Test
    public void eliminarLibroTest() {
        Libro libro = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);

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
        Libro libro = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);

        // Primero lo guardamos
        Libro registrado = libroRepo.save(libro);

        // Modificamos el nombre
        registrado.setNombre("Don Quijote de la Mancha");

        // Guardamos el registro modificado
        Libro actualizado = libroRepo.save(registrado);

        // Verificamos que se haya actualizado correctamente
        Libro buscado = libroRepo.findById("123-456-789").orElse(null);
        Assertions.assertNotNull(buscado);
        Assertions.assertEquals("Don Quijote de la Mancha", buscado.getNombre());
        System.out.println("Libro actualizado: " + buscado);
    }

    @Test
    public void listarLibrosTest() {
        // Creamos y guardamos dos libros
        Libro libro1 = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", 5, 1967, GeneroLibro.LITERATURA);
        libroRepo.save(libro1);
        libroRepo.save(libro2);

        // Obtenemos la lista de todos los libros
        List<Libro> lista = libroRepo.findAll();

        // Verificamos que la lista no esté vacía y tenga al menos 2 libros
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.size() >= 2);

        // Imprimimos la lista de libros
        lista.forEach(System.out::println);
    }

    @Test
    public void paginarLibrosTest() {
        // Configuramos el paginador con ordenamiento por ISBN
        Pageable pagina = PageRequest.of(0, 2, Sort.by("isbn"));

        // Creamos y guardamos libros para paginar
        Libro libro1 = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", 5, 1967, GeneroLibro.LITERATURA);
        Libro libro3 = new Libro("111-222-333", "Crónica de una Muerte Anunciada", 8, 1981, GeneroLibro.FICCION);
        libroRepo.save(libro1);
        libroRepo.save(libro2);
        libroRepo.save(libro3);

        // Obtenemos la página de libros
        Page<Libro> lista = libroRepo.findAll(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.getContent().size() <= 2); // Verificamos que no exceda el tamaño de la página

        // Imprimimos la página de libros ordenada por ISBN
        System.out.println("Página de libros ordenada por ISBN:");
        lista.getContent().forEach(System.out::println);
    }

    @Test
    public void filtrarPorGeneroTest() {
        // Creamos y guardamos libros
        Libro libro1 = new Libro("123-456-789", "El Quijote", 10, 1605, GeneroLibro.LITERATURA);
        Libro libro2 = new Libro("987-654-321", "Cien Años de Soledad", 5, 1967, GeneroLibro.LITERATURA);
        Libro libro3 = new Libro("111-222-333", "Crónica de una Muerte Anunciada", 8, 1981, GeneroLibro.FICCION);
        libroRepo.save(libro1);
        libroRepo.save(libro2);
        libroRepo.save(libro3);

        // Filtramos por género LITERATURA
        List<Libro> literaturaLibros = libroRepo.findAllByGenero(GeneroLibro.LITERATURA);

        // Verificamos que la lista no esté vacía y contenga los libros de género LITERATURA
        Assertions.assertNotNull(literaturaLibros);
        Assertions.assertTrue(literaturaLibros.size() > 0);
        literaturaLibros.forEach(System.out::println);
    }
}


