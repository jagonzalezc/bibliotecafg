package org.fabricadegenios.test;

import org.fabricadegenios.model.Autor;
import org.fabricadegenios.repositorios.AutorRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        System.out.println(guardado);
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
        System.out.println(buscado);
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
        lista.forEach(System.out::println);
    }
}

