package org.fabricadegenios.controllers;

import org.fabricadegenios.dto.LoginDTO;
import org.fabricadegenios.dto.MensajeDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.dto.admin.RegistroAutorDTO;
import org.fabricadegenios.dto.admin.RegistroLibroDTO;
import org.fabricadegenios.dto.admin.RegistroReservaDTO;
import org.fabricadegenios.dto.admin.RegistroUsuarioDTO;
import org.fabricadegenios.dto.TokenDTO;

import org.fabricadegenios.servicios.AdministradorServicio;
import org.fabricadegenios.servicios.AutenticacionServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fabricadegenios.servicios.UsuarioServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacionController {
    private final AutenticacionServicio autenticacionServicio;
    private final UsuarioServicio usuarioServicio;
    private final AdministradorServicio administradorServicio;

    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO)
            throws Exception {
        TokenDTO tokenDTO = autenticacionServicio.login(loginDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, tokenDTO));
    }

    @PostMapping("/crear-usuario")
    public ResponseEntity<MensajeDTO<String>> crearUsuario(@Valid @RequestBody RegistroUsuarioDTO
                                                                    registroUsuarioDTO) throws Exception{

        administradorServicio.registrarUsuario(registroUsuarioDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Usuario registrado correctamente") );
    }

    @PostMapping("/crear-autor")
    public ResponseEntity<MensajeDTO<String>> crearautor(@Valid @RequestBody RegistroAutorDTO
                                                                    registroAutorDTO) throws Exception{
        administradorServicio.registrarAutor(registroAutorDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "autor registrado correctamente") );
    }

    @PostMapping("/crear-libro")
    public ResponseEntity<MensajeDTO<String>> crearLibro(@Valid @RequestBody RegistroLibroDTO
                                                               registroLibroDTO) throws Exception{
        administradorServicio.registrarLibro(registroLibroDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "libro registrada correctamente") );
    }

    @PostMapping("/crear-reserva")
    public ResponseEntity<MensajeDTO<String>> crearReserva(@Valid @RequestBody RegistroReservaDTO registroReservaDTO) throws Exception{
        administradorServicio.registrarReserva(registroReservaDTO);
        return ResponseEntity.ok().body( new MensajeDTO<>(false, "Reserva registrada correctamente") );
    }

}
