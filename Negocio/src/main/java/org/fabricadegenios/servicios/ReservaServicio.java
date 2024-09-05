package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Reserva;

import java.util.List;

public interface ReservaServicio {
    Reserva registrarReserva(Reserva reserva) throws Exception;

    Reserva obtenerReserva(Long id) throws Exception;

    Reserva actualizarReserva(Reserva reserva) throws Exception;

    void eliminarReserva(Long id) throws Exception;

    List<Reserva> listarReservas();

    List<Reserva> buscarReservasPorUsuario(Long usuarioId);
}
