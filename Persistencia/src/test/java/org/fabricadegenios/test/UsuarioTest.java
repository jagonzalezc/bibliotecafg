package org.fabricadegenios.test;

import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void registrarUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
        u.setPassword("clave123");
        u.setGenero(GeneroPersona.FEMENINO);

        // Guardamos el registro
        Usuario guardado = usuarioRepo.save(u);

        // Comprobamos que se haya guardado correctamente
        Assertions.assertNotNull(guardado);
        Assertions.assertNotNull(guardado.getCodigo()); // Verificar que el ID fue generado

        // Imprimimos información relevante
        System.out.println("Usuario registrado: " + guardado);
    }

    @Test
    public void eliminarUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
        u.setPassword("clave123");
        u.setGenero(GeneroPersona.FEMENINO);

        // Primero lo guardamos
        Usuario registrado = usuarioRepo.save(u);

        // Imprimimos información relevante antes de eliminar
        System.out.println("Usuario a eliminar: " + registrado);

        // Luego lo eliminamos
        usuarioRepo.delete(registrado);

        // Verificamos que se haya eliminado correctamente
        Usuario buscado = usuarioRepo.findById(registrado.getCodigo().toString()).orElse(null);
        Assertions.assertNull(buscado);

        // Imprimimos información relevante después de eliminar
        System.out.println("Usuario eliminado, búsqueda resultante: " + buscado);
    }

    @Test
    public void actualizarUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("111");
        u.setNombre("Juanita");
        u.setEmail("juana@mail.com");
        u.setPassword("clave123");
        u.setGenero(GeneroPersona.FEMENINO);

        // Primero lo guardamos
        Usuario registrado = usuarioRepo.save(u);

        // Imprimimos información relevante antes de actualizar
        System.out.println("Usuario antes de actualizar: " + registrado);

        // Modificamos el nombre
        registrado.setNombre("Juanita Lopez");

        // Guardamos el registro modificado
        Usuario actualizado = usuarioRepo.save(registrado);

        // Verificamos que se haya actualizado correctamente
        Usuario buscado = usuarioRepo.findById(actualizado.getCodigo().toString()).orElse(null);
        Assertions.assertNotNull(buscado);
        Assertions.assertEquals("Juanita Lopez", buscado.getNombre());

        // Imprimimos información relevante después de actualizar
        System.out.println("Usuario después de actualizar: " + actualizado);
    }

    @Test
    public void listarUsuariosTest() {
        // Creamos y guardamos dos usuarios para listar
        Usuario u1 = new Usuario();
        u1.setCedula("111");
        u1.setNombre("Juanita");
        u1.setEmail("juana@mail.com");
        u1.setPassword("clave123");
        u1.setGenero(GeneroPersona.FEMENINO);
        usuarioRepo.save(u1);

        Usuario u2 = new Usuario();
        u2.setCedula("112");
        u2.setNombre("Carlos");
        u2.setEmail("carlos@mail.com");
        u2.setPassword("clave456");
        u2.setGenero(GeneroPersona.MASCULINO);
        usuarioRepo.save(u2);

        // Obtenemos la lista de todos los usuarios
        List<Usuario> lista = usuarioRepo.findAll();

        // Verificamos que la lista no esté vacía
        Assertions.assertNotNull(lista);
        Assertions.assertTrue(lista.size() > 0);

        // Imprimimos la lista de usuarios
        System.out.println("Lista de usuarios:");
        lista.forEach(System.out::println);
    }
}


