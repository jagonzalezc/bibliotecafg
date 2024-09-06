package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.servicios.AutorServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = NegocioApplication.class)
@Transactional
public class AutorServicioTest {

    @Autowired
    private AutorServicio autorServicio;

    @Test
    public void registrarAutorTest() {
        AutorDTO autorDTO = new AutorDTO(null, "Gabriel Garcia Marquez", 1927);

        try {
            AutorDTO guardado = autorServicio.registrarAutor(autorDTO);
            assertNotNull(guardado);
            assertNotNull(guardado.id());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author registration");
        }
    }

    @Test
    public void obtenerAutorTest() {
        AutorDTO autorDTO = new AutorDTO(null, "Jorge Luis Borges", 1899);

        try {
            AutorDTO guardado = autorServicio.registrarAutor(autorDTO);
            AutorDTO obtenido = autorServicio.obtenerAutor(guardado.id());
            assertNotNull(obtenido);
            assertEquals(guardado.id(), obtenido.id());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author retrieval");
        }
    }

    @Test
    public void actualizarAutorTest() {
        AutorDTO autorDTO = new AutorDTO(null, "Julio Cortázar", 1914);

        try {
            AutorDTO guardado = autorServicio.registrarAutor(autorDTO);
            AutorDTO actualizadoDTO = new AutorDTO(guardado.id(), "Julio Cortázar Updated", 1914);
            AutorDTO actualizado = autorServicio.actualizarAutor(actualizadoDTO);
            assertEquals("Julio Cortázar Updated", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author update");
        }
    }

    @Test
    public void eliminarAutorTest() throws Exception {
        AutorDTO autorDTO = new AutorDTO(null, "Isabel Allende", 1942);

        try {
            AutorDTO guardado = autorServicio.registrarAutor(autorDTO);
            autorServicio.eliminarAutor(guardado.id());

            try {
                autorServicio.obtenerAutor(guardado.id());
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
        AutorDTO autorDTO1 = new AutorDTO(null, "Mario Vargas Llosa", 1936);
        AutorDTO autorDTO2 = new AutorDTO(null, "Pablo Neruda", 1904);

        try {
            autorServicio.registrarAutor(autorDTO1);
            autorServicio.registrarAutor(autorDTO2);
            List<AutorDTO> autores = autorServicio.listarAutores();
            assertFalse(autores.isEmpty());
            assertTrue(autores.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during author listing");
        }
    }

    @Test
    public void buscarAutorPorNombreTest() {
        AutorDTO autorDTO = new AutorDTO(null, "Gabriel Garcia Marquez", 1927);

        try {
            autorServicio.registrarAutor(autorDTO);
            Pageable pageable = PageRequest.of(0, 10);
            Page<AutorDTO> autores = autorServicio.buscarAutoresPorNombre("Gabriel", pageable);
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
            AutorDTO autorDTO1 = new AutorDTO(null, "Ana Maria Matute", 1925);
            AutorDTO autorDTO2 = new AutorDTO(null, "Octavio Paz", 1914);
            AutorDTO autorDTO3 = new AutorDTO(null, "Roberto Bolaño", 1953);
            autorServicio.registrarAutor(autorDTO1);
            autorServicio.registrarAutor(autorDTO2);
            autorServicio.registrarAutor(autorDTO3);

            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));
            Page<AutorDTO> lista = autorServicio.paginarAutores(pagina);

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
}
