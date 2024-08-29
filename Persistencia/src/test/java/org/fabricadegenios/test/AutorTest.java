package org.fabricadegenios.test;

import org.fabricadegenios.model.Autor;
import org.fabricadegenios.repositorios.AutorRepo;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:data.sql") // Carga el archivo data.sql antes de ejecutar las pruebas
public class AutorTest {

    @Autowired
    private AutorRepo autorRepo;

    @Test
    public void registrarAutorTest() {
        Autor a = new Autor("Gabriel García Márquez", 1927);

        // Guardamos el registro
        Autor guardado = autorRepo.save(a);

        // Comprobamos que se haya guardado correctamente
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(guardado.getCodigo()); // Verificar que el ID fue generado

        // Imprimimos el autor guardado
        System.out.println("Autor guardado: " + guardado);
    }

    @Test
    public void eliminarAutorTest() {
        Autor a = new Autor("Gabriel García Márquez", 1927);

        // Primero lo guardamos
        Autor registrado = autorRepo.save(a);

        // Luego lo eliminamos
        autorRepo.delete(registrado);

        // Verificamos que se haya eliminado correctamente
        Autor buscado = autorRepo.findById(registrado.getCodigo()).orElse(null);
        Assertions.assertNull(buscado);

        // Imprimimos confirmación de eliminación
        System.out.println("Autor eliminado: " + registrado.getNombre());
    }

    @Test
    public void actualizarAutorTest() {
        Autor a = new Autor("Gabriel García Márquez", 1927);

        // Primero lo guardamos
        Autor registrado = autorRepo.save(a);

        // Modificamos el nombre
        registrado.setNombre("Gabriel G. Márquez");

        // Guardamos el registro modificado
        Autor actualizado = autorRepo.save(registrado);

        // Verificamos que se haya actualizado correctamente
        Autor buscado = autorRepo.findById(actualizado.getCodigo()).orElse(null);
        Assertions.assertNotNull(buscado);
        Assertions.assertEquals("Gabriel G. Márquez", buscado.getNombre());

        // Imprimimos el autor actualizado
        System.out.println("Autor actualizado: " + buscado);
    }

    @Test
    public void listarAutoresTest() {
        // Creamos dos autores
        Autor a1 = new Autor("Gabriel García Márquez", 1927);
        Autor a2 = new Autor("Isabel Allende", 1942);

        // Guardamos los autores
        autorRepo.save(a1);
        autorRepo.save(a2);

        // Obtenemos la lista de todos los autores
        List<Autor> lista = autorRepo.findAll();

        // Verificamos que la lista no esté vacía y contenga los autores creados
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.size() >= 2);

        // Imprimimos la lista de autores
        lista.forEach(a -> System.out.println("Autor en lista: " + a));
    }

    @Test
    public void filtrarPorNombreTest() {
        Autor a1 = new Autor("Gabriel García Márquez", 1927);
        Autor a2 = new Autor("Gabriel García Márquez", 1942);

        // Guardamos los autores
        autorRepo.save(a1);
        autorRepo.save(a2);

        // Filtramos por nombre
        Page<Autor> lista = autorRepo.findByNombreContaining("Gabriel García Márquez", PageRequest.of(0, 10));

        // Verificamos que se han recuperado los autores correctos
        Assertions.assertNotNull(lista);
        Assertions.assertFalse(lista.isEmpty());
        Assertions.assertTrue(lista.getContent().size() >= 2);

        // Imprimimos la lista de autores filtrados
        lista.getContent().forEach(a -> System.out.println("Autor filtrado por nombre: " + a));
    }



    @Test
    public void paginarListaPorNombreTest() {
        // Configuramos el paginador con ordenamiento por nombre
        Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

        // Creamos y guardamos algunos autores
        autorRepo.save(new Autor("Gabriel García Márquez", 1927));
        autorRepo.save(new Autor("Isabel Allende", 1942));
        autorRepo.save(new Autor("Jorge Luis Borges", 1899));
        autorRepo.save(new Autor("Mario Vargas Llosa", 1936));

        // Obtenemos la página de autores
        Page<Autor> lista = autorRepo.findAll(pagina);

        // Verificamos que la página no esté vacía y tenga el tamaño esperado
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.getSize() > 0);

        // Imprimimos la página de autores ordenada por nombre
        System.out.println("Página de autores ordenada por nombre:");
        lista.getContent().forEach(System.out::println);
    }


}
