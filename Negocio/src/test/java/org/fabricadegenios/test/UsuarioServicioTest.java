package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.servicios.UsuarioServicio;
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
public class UsuarioServicioTest {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Test
    public void registrarUsuarioTest() {
        Usuario u = new Usuario("111", "maria@gmail.com", GeneroPersona.FEMENINO, "MariaLopez", "clave123");

        try {
            // Guardamos el registro
            Usuario guardado = usuarioServicio.registrarUsuario(u);
            // Comprobamos que si haya quedado
            Assert.assertNotNull(guardado);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user registration");
        }
    }


    @Test
    public void obtenerUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("222");
        u.setNombre("Carlos");
        u.setEmail("carlos@mail.com");
        u.setPassword("clave456");
        u.setGenero(GeneroPersona.MASCULINO);

        try {
            Usuario guardado = usuarioServicio.registrarUsuario(u);
            Usuario obtenido = usuarioServicio.obtenerUsuario(guardado.getCodigo());
            Assert.assertNotNull(obtenido);
            Assert.assertEquals(guardado.getCodigo(), obtenido.getCodigo());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user retrieval");
        }
    }

    @Test
    public void actualizarUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("333");
        u.setNombre("Ana");
        u.setEmail("ana@mail.com");
        u.setPassword("clave789");
        u.setGenero(GeneroPersona.FEMENINO);

        try {
            Usuario guardado = usuarioServicio.registrarUsuario(u);
            guardado.setNombre("Ana Updated");
            Usuario actualizado = usuarioServicio.actualizarUsuario(guardado);
            Assert.assertEquals("Ana Updated", actualizado.getNombre());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user update");
        }
    }

    @Test
    public void eliminarUsuarioTest() {
        Usuario u = new Usuario();
        u.setCedula("444");
        u.setNombre("Luis");
        u.setEmail("luis@mail.com");
        u.setPassword("clave000");
        u.setGenero(GeneroPersona.MASCULINO);

        try {
            Usuario guardado = usuarioServicio.registrarUsuario(u);
            usuarioServicio.eliminarUsuario(guardado);
            Optional<Usuario> eliminado = usuarioServicio.findByEmailAndPassword(guardado.getEmail(), guardado.getPassword());
            Assert.assertFalse(eliminado.isPresent());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user deletion");
        }
    }

    @Test
    public void listarUsuariosTest() {
        // Primero, agregamos algunos usuarios
        Usuario u1 = new Usuario();
        u1.setCedula("555");
        u1.setNombre("Maria");
        u1.setEmail("maria@mail.com");
        u1.setPassword("clave111");
        u1.setGenero(GeneroPersona.FEMENINO);

        Usuario u2 = new Usuario();
        u2.setCedula("666");
        u2.setNombre("Pedro");
        u2.setEmail("pedro@mail.com");
        u2.setPassword("clave222");
        u2.setGenero(GeneroPersona.MASCULINO);

        try {
            usuarioServicio.registrarUsuario(u1);
            usuarioServicio.registrarUsuario(u2);
            // Listamos todos los usuarios
            var usuarios = usuarioServicio.listarUsuarios();
            Assert.assertFalse(usuarios.isEmpty());
            Assert.assertTrue(usuarios.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user listing");
        }
    }

    @Test
    public void obtenerUsuarioPorCedulaTest() {
        Usuario u = new Usuario();
        u.setCedula("777");
        u.setNombre("Sofia");
        u.setEmail("sofia@mail.com");
        u.setPassword("clave333");
        u.setGenero(GeneroPersona.FEMENINO);

        try {
            Usuario guardado = usuarioServicio.registrarUsuario(u);
            Usuario obtenido = usuarioServicio.obtenerUsuariosPorCedula(guardado.getCedula());
            Assert.assertNotNull(obtenido);
            Assert.assertEquals(guardado.getCedula(), obtenido.getCedula());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user retrieval by cedula");
        }
    }

    @Test
    public void buscarUsuarioPorNombreTest() {
        Usuario u = new Usuario();
        u.setCedula("888");
        u.setNombre("Gonzalo");
        u.setEmail("gonzalo@mail.com");
        u.setPassword("clave444");
        u.setGenero(GeneroPersona.MASCULINO);

        try {
            usuarioServicio.registrarUsuario(u);
            var usuarios = usuarioServicio.findAllByNombreContainsIgnoreCase("gonzalo");
            Assert.assertFalse(usuarios.isEmpty());
            Assert.assertTrue(usuarios.stream().anyMatch(user -> user.getNombre().equalsIgnoreCase("Gonzalo")));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user search by name");
        }
    }

    @Test
    public void paginarUsuariosTest() {
        // Crear usuarios para la prueba
        try {
            Usuario u1 = new Usuario();
            u1.setCedula("123");
            u1.setNombre("Ana");
            u1.setEmail("ana@mail.com");
            u1.setPassword("clave123");
            u1.setGenero(GeneroPersona.FEMENINO);
            usuarioServicio.registrarUsuario(u1);

            Usuario u2 = new Usuario();
            u2.setCedula("456");
            u2.setNombre("Luis");
            u2.setEmail("luis@mail.com");
            u2.setPassword("clave456");
            u2.setGenero(GeneroPersona.MASCULINO);
            usuarioServicio.registrarUsuario(u2);

            Usuario u3 = new Usuario();
            u3.setCedula("789");
            u3.setNombre("Gonzalo");
            u3.setEmail("gonzalo@mail.com");
            u3.setPassword("clave789");
            u3.setGenero(GeneroPersona.MASCULINO);
            usuarioServicio.registrarUsuario(u3);

            // Configuramos el paginador con ordenamiento por nombre
            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));

            // Obtenemos la página de usuarios
            Page<Usuario> lista = usuarioServicio.paginarUsuarios(pagina);

            // Verificamos que la página no esté vacía y tenga el tamaño esperado
            Assert.assertNotNull(lista);
            Assert.assertTrue(lista.getTotalElements() > 0); // Verifica que haya elementos en total
            Assert.assertTrue(lista.getSize() <= 2); // Verifica que el tamaño de la página no exceda el tamaño máximo
            Assert.assertEquals(2, lista.getNumberOfElements()); // Verifica el número de elementos en la página actual

            // Imprimimos la página de usuarios ordenada por nombre
            System.out.println("Página de usuarios ordenada por nombre:");
            lista.getContent().forEach(user -> System.out.println(user.getNombre()));

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred during user pagination test");
        }
    }


}
