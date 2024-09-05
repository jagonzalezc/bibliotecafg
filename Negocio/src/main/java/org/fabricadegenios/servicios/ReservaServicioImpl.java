package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Libro;
import org.fabricadegenios.model.Reserva;
import org.fabricadegenios.repositorios.ReservaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaServicioImpl implements ReservaServicio {

    private final ReservaRepo reservaRepo;
    private final LibroServicio libroServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public ReservaServicioImpl(ReservaRepo reservaRepo, LibroServicio libroServicio, UsuarioServicio usuarioServicio) {
        this.reservaRepo = reservaRepo;
        this.libroServicio = libroServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    public Reserva registrarReserva(Reserva reserva) throws Exception {
        // Verificar que la reserva tenga al menos dos libros
        if (reserva.getLibros().size() < 2) {
            throw new Exception("La reserva debe incluir al menos dos libros.");
        }

        // Verificar disponibilidad de libros
        for (Libro libro : reserva.getLibros()) {
            if (!libro.getDisponible()) {
                throw new Exception("Uno o más libros ya no están disponibles.");
            }
        }

        // Actualizar la disponibilidad de los libros
        for (Libro libro : reserva.getLibros()) {
            libro.setDisponible(false);
            libroServicio.actualizarLibro(libro);
        }

        return reservaRepo.save(reserva);
    }


    @Override
    public Reserva obtenerReserva(Long id) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(id);
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }
        return buscada.get();
    }

    @Override
    public Reserva actualizarReserva(Reserva reserva) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(reserva.getCodigo());
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }
        return reservaRepo.save(reserva);
    }

    @Override
    public void eliminarReserva(Long id) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(id);
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }
        Reserva reserva = buscada.get();
        reservaRepo.delete(reserva);

        // Restaurar disponibilidad de libros
        for (Libro libro : reserva.getLibros()) {
            libro.setDisponible(true);
            libroServicio.actualizarLibro(libro);
        }
    }

    @Override
    public List<Reserva> listarReservas() {
        return reservaRepo.findAll();
    }

    @Override
    public List<Reserva> buscarReservasPorUsuario(Long usuarioCodigo) {
        return reservaRepo.findByUsuario(usuarioCodigo);
    }
}

