package org.fabricadegenios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF si es una API REST
                .csrf(csrf -> csrf.disable())

                // Configurar la autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a la ruta de login
                        .requestMatchers("/api/auth/login").permitAll()

                        // Solo permitir a ADMIN realizar los CRDs
                        .requestMatchers("/api/auth/crear-usuario",
                                "/api/auth/crear-autor",
                                "/api/auth/crear-libro",
                                "/api/auth/crear-reserva").hasRole("ADMIN")

                        // Todas las demás peticiones requieren autenticación
                        .anyRequest().authenticated()
                )

                // Habilitar autenticación básica
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




