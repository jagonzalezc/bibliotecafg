package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepo extends JpaRepository<Usuario, String> {
}
