package org.fabricadegenios.repositorios;

import org.fabricadegenios.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepo extends JpaRepository<Reserva, Long> {
    // Puedes agregar métodos personalizados si lo necesitas
}

