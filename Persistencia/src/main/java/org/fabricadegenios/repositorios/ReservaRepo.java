package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Reserva;
import org.fabricadegenios.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservaRepo extends JpaRepository<Reserva, Long> {

    // Método para filtrar reservas por usuario
    @Query("SELECT r FROM Reserva r WHERE r.usuario = :usuario")
    List<Reserva> findByUsuario(Usuario usuario);

    // Método para paginar reservas, puedes usar findAll() de JpaRepository para obtener todas las reservas y luego aplicar la paginación
    @Override
    Page<Reserva> findAll(Pageable pageable);
}


