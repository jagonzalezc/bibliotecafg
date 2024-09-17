package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.LoginDTO;
import org.fabricadegenios.dto.TokenDTO;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.fabricadegenios.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AutenticacionServicioImpl implements AutenticacionServicio {
    private final UsuarioRepo usuarioRepo;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;  // Inyección de PasswordEncoder

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        Usuario usuario = usuarioRepo.findByEmail(loginDTO.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + loginDTO.email()));

        if (usuario.getEmail() == null) {
            throw new Exception("No existe el email ingresado");
        }

        if (!passwordEncoder.matches(loginDTO.password(), usuario.getPassword())) {
            throw new Exception("La contraseña ingresada es incorrecta");
        }

        return new TokenDTO(crearToken(usuario));
    }

    private String crearToken(Usuario usuario) {
        // Obtener el rol desde la entidad Usuario
        String rol = usuario.getRol().toString().toLowerCase();  // Asumimos que Rol es un Enum y convertimos a minúsculas
        String nombre = usuario.getNombre();  // Nombre viene directamente de la entidad Usuario

        // Crear el mapa de claims para el token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        claims.put("nombre", nombre);
        claims.put("id", usuario.getCodigo());

        // Generar el token utilizando el método generarToken de jwtUtils
        String token = jwtUtils.generarToken(usuario.getEmail(), claims);
        System.out.println("Token generado: " + token);  // Útil para depuración

        return token;
    }
}

