package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.model.Autor;
import org.fabricadegenios.servicios.AutorServicio;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class AutorServicioTest {

    @Autowired
    private AutorServicio autorServicio;

    @Test
    public void registrarAutorTest() {
        Autor a = new Autor("Gabriel Garcia Marquez", 1927);

        try {
            Autor guardado = autorServicio.registrarAutor(a);
            Assert.assertNotNull(guardado);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author registration");
        }
    }

    @Test
    public void obtenerAutorTest() {
        Autor a = new Autor("Jorge Luis Borges", 1899);

        try {
            Autor guardado = autorServicio.registrarAutor(a);
            Autor obtenido = autorServicio.obtenerAutor(guardado.getCodigo());
            Assert.assertNotNull(obtenido);
            Assert.assertEquals(guardado.getCodigo(), obtenido.getCodigo());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author retrieval");

        }
    }
    @Test
    public void actualizarAutorTest() {
        Autor a = new Autor("Julio Cortázar", 1914);

        try {
            Autor guardado = autorServicio.registrarAutor(a);
            guardado.setNombre("Julio Cortázar Updated");
            Autor actualizado = autorServicio.actualizarAutor(guardado);
            Assert.assertEquals("Julio Cortázar Updated", actualizado.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author update");
        }
    }

    @Test
    public void eliminarAutorTest() throws Exception {
        Autor a = new Autor("Isabel Allende", 1942);

        try {
            // Registramos el autor
            Autor guardado = autorServicio.registrarAutor(a);

            // Eliminamos el autor
            autorServicio.eliminarAutor(guardado);

            // Intentamos obtener el autor después de eliminarlo, debería lanzar una excepción
            try {
                autorServicio.obtenerAutor(guardado.getCodigo());
                Assert.fail("Se esperaba una excepción al intentar obtener un autor eliminado");
            } catch (Exception e) {
                // Verificamos que la excepción lanzada sea la esperada
                Assert.assertEquals("El autor con el código proporcionado no existe.", e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Ocurrió una excepción durante la eliminación del autor");
        }
    }




    @Test
    public void listarAutoresTest() {
        Autor a1 = new Autor("Mario Vargas Llosa",  1936);
        Autor a2 = new Autor("Pablo Neruda",  1904);

        try {
            autorServicio.registrarAutor(a1);
            autorServicio.registrarAutor(a2);
            var autores = autorServicio.listarAutores();
            Assert.assertFalse(autores.isEmpty());
            Assert.assertTrue(autores.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author listing");
        }
    }

    @Test
    public void buscarAutorPorNombreTest() {
        Autor a = new Autor("Gabriel Garcia Marquez", 1927);

        try {
            // Registrar el autor
            autorServicio.registrarAutor(a);

            // Configuramos un paginador sencillo (sin paginación real) para usar en la búsqueda
            Pageable pageable = PageRequest.of(0, 10);  // Página 0, tamaño 10

            // Buscar autores que contienen "Gabriel" en el nombre
            Page<Autor> autores = autorServicio.buscarAutoresPorNombre("Gabriel", pageable);

            // Verificamos que la búsqueda no esté vacía
            Assert.assertFalse(autores.getContent().isEmpty());

            // Verificamos que el autor correcto esté presente
            Assert.assertTrue(autores.getContent().stream()
                    .anyMatch(autor -> autor.getNombre().equalsIgnoreCase("Gabriel Garcia Marquez")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author search by name");
        }
    }


    @Test
    public void paginarAutoresTest() {
        // Crear autores para la prueba
        try {
            Autor a1 = new Autor("Ana Maria Matute",  1925);
            Autor a2 = new Autor("Octavio Paz",  1914);
            Autor a3 = new Autor("Roberto Bolaño",  1953);
            autorServicio.registrarAutor(a1);
            autorServicio.registrarAutor(a2);
            autorServicio.registrarAutor(a3);

            // Configuramos el paginador con ordenamiento por nombre
            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

            // Obtenemos la página de autores
            Page<Autor> lista = autorServicio.paginarAutores(pagina);

            // Verificamos que la página no esté vacía y tenga el tamaño esperado
            Assert.assertNotNull(lista);
            Assert.assertTrue(lista.getTotalElements() > 0); // Verifica que haya elementos en total
            Assert.assertTrue(lista.getSize() <= 2); // Verifica que el tamaño de la página no exceda el tamaño máximo
            Assert.assertEquals(2, lista.getNumberOfElements()); // Verifica el número de elementos en la página actual

            // Imprimimos la página de autores ordenada por nombre
            System.out.println("Página de autores ordenada por nombre:");
            lista.getContent().forEach(autor -> System.out.println(autor.getNombre()));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during author pagination test");
        }
    }
}