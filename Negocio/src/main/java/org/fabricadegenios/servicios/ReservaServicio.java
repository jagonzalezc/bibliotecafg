package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.ReservaDTO;
import org.fabricadegenios.model.Reserva;

import java.util.List;

public interface ReservaServicio {


    ReservaDTO registrarReserva(ReservaDTO reservaDTO) throws Exception;

    ReservaDTO obtenerReserva(Long id) throws Exception;

    ReservaDTO actualizarReserva(ReservaDTO reservaDTO) throws Exception;

    void eliminarReserva(Long id) throws Exception;

    List<ReservaDTO> listarReservas();

    List<ReservaDTO> buscarReservasPorUsuario(Long usuarioCodigo) throws Exception;
}
