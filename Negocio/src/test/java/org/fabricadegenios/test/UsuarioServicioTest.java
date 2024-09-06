package org.fabricadegenios.test;

import org.fabricadegenios.NegocioApplication;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.servicios.UsuarioServicio;
import org.junit.jupiter.api.Assertions;
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
        UsuarioDTO dto = new UsuarioDTO(null, "111", "Maria Lopez", "maria@gmail.com", "clave123", GeneroPersona.FEMENINO);

        try {
            // Guardamos el usuario usando DTO
            UsuarioDTO guardado = usuarioServicio.registrarUsuario(dto);
            // Comprobamos que si haya quedado
            Assertions.assertNotNull(guardado);
            Assertions.assertEquals(dto.email(), guardado.email());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user registration: " + e.getMessage());
        }
    }

    @Test
    public void obtenerUsuarioTest() {
        UsuarioDTO dto = new UsuarioDTO(null, "222", "Carlos", "carlos@mail.com", "clave456", GeneroPersona.MASCULINO);

        try {
            UsuarioDTO guardado = usuarioServicio.registrarUsuario(dto);
            UsuarioDTO obtenido = usuarioServicio.obtenerUsuario(guardado.id());
            Assertions.assertNotNull(obtenido);
            Assertions.assertEquals(guardado.id(), obtenido.id());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user retrieval: " + e.getMessage());
        }
    }

    @Test
    public void actualizarUsuarioTest() {
        // Creación de un DTO de usuario con datos iniciales
        UsuarioDTO dto = new UsuarioDTO(null, "333", "Ana", "ana@mail.com", "clave789", GeneroPersona.FEMENINO);

        try {
            // Registra un nuevo usuario
            UsuarioDTO guardado = usuarioServicio.registrarUsuario(dto);

            // Actualiza los datos del usuario previamente guardado
            UsuarioDTO dtoActualizado = new UsuarioDTO(
                    guardado.id(),           // Usa el ID del usuario guardado
                    guardado.cedula(),       // Mantiene la misma cédula
                    "Ana Updated",           // Cambia el nombre a uno actualizado
                    guardado.email(),        // Mantiene el mismo correo electrónico
                    guardado.password(),     // Mantiene la misma contraseña
                    guardado.genero()        // Mantiene el mismo género
            );

            // Llama al método de actualizar usuario
            UsuarioDTO actualizado = usuarioServicio.actualizarUsuario(dtoActualizado);

            // Comprueba que el nombre fue actualizado correctamente
            Assertions.assertEquals("Ana Updated", actualizado.nombre());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user update: " + e.getMessage());
        }
    }

    @Test
    public void eliminarUsuarioTest() {
        UsuarioDTO dto = new UsuarioDTO(null, "444", "Luis", "luis@mail.com", "clave000", GeneroPersona.MASCULINO);

        try {
            UsuarioDTO guardado = usuarioServicio.registrarUsuario(dto);
            usuarioServicio.eliminarUsuario(guardado.id());

            // Buscar el usuario eliminado
            Optional<UsuarioDTO> eliminado = usuarioServicio.findByEmailAndPassword(guardado.email(), guardado.password());

            // Verificar que no esté presente
            Assertions.assertFalse(eliminado.isPresent());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user deletion");
        }
    }


    @Test
    public void listarUsuariosTest() {
        UsuarioDTO dto1 = new UsuarioDTO(null, "555", "Maria", "maria@mail.com", "clave111", GeneroPersona.FEMENINO);
        UsuarioDTO dto2 = new UsuarioDTO(null, "666", "Pedro", "pedro@mail.com", "clave222", GeneroPersona.MASCULINO);

        try {
            usuarioServicio.registrarUsuario(dto1);
            usuarioServicio.registrarUsuario(dto2);
            // Listamos todos los usuarios
            var usuarios = usuarioServicio.listarUsuarios();
            Assertions.assertFalse(usuarios.isEmpty());
            Assertions.assertTrue(usuarios.size() >= 2);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user listing: " + e.getMessage());
        }
    }

    @Test
    public void obtenerUsuarioPorCedulaTest() {
        UsuarioDTO dto = new UsuarioDTO(null, "777", "Sofia", "sofia@mail.com", "clave333", GeneroPersona.FEMENINO);

        try {
            UsuarioDTO guardado = usuarioServicio.registrarUsuario(dto);
            UsuarioDTO obtenido = usuarioServicio.obtenerUsuariosPorCedula(guardado.cedula());
            Assertions.assertNotNull(obtenido);
            Assertions.assertEquals(guardado.cedula(), obtenido.cedula());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user retrieval by cedula: " + e.getMessage());
        }
    }

    @Test
    public void buscarUsuarioPorNombreTest() {
        UsuarioDTO dto = new UsuarioDTO(null, "888", "Gonzalo", "gonzalo@mail.com", "clave444", GeneroPersona.MASCULINO);

        try {
            usuarioServicio.registrarUsuario(dto);
            var usuarios = usuarioServicio.findAllByNombreContainsIgnoreCase("gonzalo");
            Assertions.assertFalse(usuarios.isEmpty());
            Assertions.assertTrue(usuarios.stream().anyMatch(user -> user.nombre().equalsIgnoreCase("Gonzalo")));
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user search by name: " + e.getMessage());
        }
    }

    @Test
    public void paginarUsuariosTest() throws Exception {
        try {
            UsuarioDTO dto1 = new UsuarioDTO(null, "123", "Ana", "ana@mail.com", "clave123", GeneroPersona.FEMENINO);
            UsuarioDTO dto2 = new UsuarioDTO(null, "456", "Luis", "luis@mail.com", "clave456", GeneroPersona.MASCULINO);
            UsuarioDTO dto3 = new UsuarioDTO(null, "789", "Gonzalo", "gonzalo@mail.com", "clave789", GeneroPersona.MASCULINO);

            usuarioServicio.registrarUsuario(dto1);
            usuarioServicio.registrarUsuario(dto2);
            usuarioServicio.registrarUsuario(dto3);

            Pageable pagina = PageRequest.of(0, 2, Sort.by("nombre"));
            Page<UsuarioDTO> lista = usuarioServicio.paginarUsuarios(pagina);

            Assertions.assertNotNull(lista);
            Assertions.assertTrue(lista.getTotalElements() > 0);
            Assertions.assertTrue(lista.getSize() <= 2);
            Assertions.assertEquals(2, lista.getNumberOfElements());

            System.out.println("Página de usuarios ordenada por nombre:");
            lista.getContent().forEach(user -> System.out.println(user.nombre()));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Exception occurred during user pagination test: " + e.getMessage());
        }
    }
}
