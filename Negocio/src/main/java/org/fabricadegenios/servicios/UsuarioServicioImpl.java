package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepo usuarioRepo;

    public UsuarioServicioImpl(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO dto) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findByEmail(dto.email());
        if (buscado.isPresent()) {
            throw new Exception("El email ya se encuentra en uso");
        }

        Usuario usuario = convertirADominio(dto);
        Usuario guardado = usuarioRepo.save(usuario);
        return convertirADTO(guardado);
    }

    @Override
    public UsuarioDTO obtenerUsuario(Long id) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(id);
        if (buscado.isEmpty()) {
            throw new Exception("El código no se encuentra en uso");
        }

        Usuario usuario = buscado.get();
        return convertirADTO(usuario);
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO dto) throws Exception {
        // Verificar si el usuario con el ID proporcionado ya existe en la base de datos
        Optional<Usuario> optionalUsuario = usuarioRepo.findById(dto.id());

        if (optionalUsuario.isEmpty()) {
            throw new Exception("Usuario con ID " + dto.id() + " no existe."); // O manejar de otra manera
        }

        // Actualizar los campos del usuario existente con los valores del DTO
        Usuario usuarioExistente = optionalUsuario.get();
        usuarioExistente.setNombre(dto.nombre());
        usuarioExistente.setCedula(dto.cedula());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setPassword(dto.password());
        usuarioExistente.setGenero(dto.genero());

        // Guardar la entidad actualizada
        Usuario actualizado = usuarioRepo.save(usuarioExistente);

        // Convertir de nuevo a DTO y devolver el usuario actualizado
        return convertirADTO(actualizado);
    }


    @Override
    public void eliminarUsuario(Long id) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(id);
        if (buscado.isEmpty()) {
            throw new Exception("El código no se encuentra en uso");
        }

        usuarioRepo.delete(buscado.get());
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        return usuarios.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public UsuarioDTO obtenerUsuariosPorCedula(String cedula) throws Exception {
        Optional<Usuario> usuario = usuarioRepo.findByCedula(cedula);
        if (usuario.isEmpty()) {
            throw new Exception("No existe un usuario con la cédula dada");
        }

        return convertirADTO(usuario.get());
    }

    @Override
    public List<UsuarioDTO> findAllByNombreContainsIgnoreCase(String nombre) {
        List<Usuario> usuarios = usuarioRepo.findAllByNombreContainsIgnoreCase(nombre);
        return usuarios.stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    public Optional<UsuarioDTO> findByEmailAndPassword(String email, String password) {
        Optional<Usuario> usuario = usuarioRepo.findByEmailAndPassword(email, password);
        return usuario.map(this::convertirADTO);
    }

    @Override
    public Optional<UsuarioDTO> findByCedulaAndPassword(String cedula, String password) {
        Optional<Usuario> usuario = usuarioRepo.findByCedulaAndPassword(cedula, password);
        return usuario.map(this::convertirADTO);
    }

    @Override
    public Page<UsuarioDTO> paginarUsuarios(Pageable pageable) {
        Page<Usuario> pagina = usuarioRepo.findAll(pageable);
        return pagina.map(this::convertirADTO);
    }

    public boolean estaDisponible(String email) {
        Optional<Usuario> usuario = usuarioRepo.findByEmail(email);
        return usuario.isEmpty();
    }

    // Métodos reutilizables para convertir entre Usuario y UsuarioDTO

    public Usuario convertirADominio(UsuarioDTO usuarioDTO) {
        return new Usuario(
                usuarioDTO.cedula(),
                usuarioDTO.email(),
                usuarioDTO.genero(),
                usuarioDTO.nombre(),
                usuarioDTO.password(),
                usuarioDTO.rol()
        );
    }

    public UsuarioDTO convertirADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getCodigo(),
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getGenero(),
                usuario.getRol()
        );
    }

}
