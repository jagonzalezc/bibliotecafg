package org.fabricadegenios.test;

import org.fabricadegenios.dto.LoginDTO;
import org.fabricadegenios.dto.TokenDTO;
import org.fabricadegenios.model.Rol;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.fabricadegenios.servicios.AutenticacionServicioImpl;
import org.fabricadegenios.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutenticacionServicioImplTest {

    @InjectMocks
    private AutenticacionServicioImpl autenticacionServicio;

    @Mock
    private UsuarioRepo usuarioRepo;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSuccess() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(encodedPassword);
        usuario.setRol(Rol.ADMIN); // Asegurarte de que el rol esté asignado
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtils.generarToken(any(), any())).thenReturn("token");

        // LoginDTO incluye un usuario vacío ya que no se debe utilizar aquí
        LoginDTO loginDTO = new LoginDTO(email, password, null);

        // Act
        TokenDTO tokenDTO = autenticacionServicio.login(loginDTO);

        // Assert
        assertNotNull(tokenDTO);
        assertEquals("token", tokenDTO.token());
        verify(usuarioRepo).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtUtils).generarToken(any(), any());
    }

    @Test
    void loginUserNotFound() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.empty());

        LoginDTO loginDTO = new LoginDTO(email, password, null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            autenticacionServicio.login(loginDTO);
        });
        assertEquals("Usuario no encontrado con el email: " + email, exception.getMessage());
    }

    @Test
    void loginPasswordIncorrect() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(encodedPassword);
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        LoginDTO loginDTO = new LoginDTO(email, password, null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            autenticacionServicio.login(loginDTO);
        });
        assertEquals("La contraseña ingresada es incorrecta", exception.getMessage());
    }
}


