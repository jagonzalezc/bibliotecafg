package org.fabricadegenios.servicios;

import lombok.RequiredArgsConstructor;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepo usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor no es necesario por @RequiredArgsConstructor, se eliminó

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO dto) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findByEmail(dto.email());
        if (buscado.isPresent()) {
            throw new Exception("El email ya se encuentra en uso");
        }

        Usuario usuario = convertirADominio(dto);
        String passwordCifrada = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordCifrada);
        Usuario guardado = usuarioRepo.save(usuario);
        return convertirADTO(guardado);
    }

    @Override
    public UsuarioDTO obtenerUsuario(Long id) throws Exception {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new Exception("El código no se encuentra en uso"));
        return convertirADTO(usuario);
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO dto) throws Exception {
        Usuario usuarioExistente = usuarioRepo.findById(dto.id())
                .orElseThrow(() -> new Exception("Usuario con ID " + dto.id() + " no existe."));

        // Actualizar campos
        usuarioExistente.setNombre(dto.nombre());
        usuarioExistente.setCedula(dto.cedula());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setPassword(passwordEncoder.encode(dto.password())); // Cifra la nueva contraseña
        usuarioExistente.setGenero(dto.genero());

        Usuario actualizado = usuarioRepo.save(usuarioExistente);
        return convertirADTO(actualizado);
    }

    @Override
    public void eliminarUsuario(Long id) throws Exception {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new Exception("El código no se encuentra en uso"));
        usuarioRepo.delete(usuario);
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
        Usuario usuario = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new Exception("No existe un usuario con la cédula dada"));
        return convertirADTO(usuario);
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
        Optional<Usuario> usuario = usuarioRepo.findByEmailAndPassword(email, passwordEncoder.encode(password));
        return usuario.map(this::convertirADTO);
    }

    @Override
    public Optional<UsuarioDTO> findByCedulaAndPassword(String cedula, String password) {
        Optional<Usuario> usuario = usuarioRepo.findByCedulaAndPassword(cedula, passwordEncoder.encode(password));
        return usuario.map(this::convertirADTO);
    }

    @Override
    public Page<UsuarioDTO> paginarUsuarios(Pageable pageable) {
        Page<Usuario> pagina = usuarioRepo.findAll(pageable);
        return pagina.map(this::convertirADTO);
    }

    public boolean estaDisponible(String email) {
        return usuarioRepo.findByEmail(email).isEmpty();
    }

    // Métodos reutilizables para convertir entre Usuario y UsuarioDTO
    private Usuario convertirADominio(UsuarioDTO usuarioDTO) {
        return new Usuario(
                usuarioDTO.cedula(),
                usuarioDTO.email(),
                usuarioDTO.genero(),
                usuarioDTO.nombre(),
                usuarioDTO.password(), // El password se cifra luego
                usuarioDTO.rol()
        );
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
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
