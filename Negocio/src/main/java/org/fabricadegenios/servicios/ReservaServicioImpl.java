package org.fabricadegenios.servicios;

import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.dto.ReservaDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.model.GeneroPersona;
import org.fabricadegenios.model.Libro;
import org.fabricadegenios.model.Reserva;
import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.ReservaRepo;
import org.fabricadegenios.repositorios.UsuarioRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaServicioImpl implements ReservaServicio {

    private final ReservaRepo reservaRepo;
    private final UsuarioRepo usuarioRepo; // Añadir UsuarioRepo
    private final LibroServicio libroServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public ReservaServicioImpl(ReservaRepo reservaRepo, UsuarioRepo usuarioRepo, LibroServicio libroServicio, UsuarioServicio usuarioServicio) {
        this.reservaRepo = reservaRepo;
        this.usuarioRepo = usuarioRepo;
        this.libroServicio = libroServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    public ReservaDTO registrarReserva(ReservaDTO reservaDTO) throws Exception {
        if (reservaDTO.libroIds().isEmpty()) {
            throw new Exception("La reserva debe incluir al menos un libro.");
        }

        // Verificar disponibilidad de los libros antes de crear la reserva
        for (String libroId : reservaDTO.libroIds()) {
            LibroDTO libro = libroServicio.obtenerLibro(libroId);
            if (!libro.disponible()) {
                throw new Exception("Uno o más libros ya no están disponibles.");
            }
        }

        // Crear y guardar la reserva
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(reservaDTO.fechaReserva());
        reserva.setFechaDevolucion(reservaDTO.fechaDevolucion());
        reserva.setAnio(reservaDTO.anio());

        // Convertir UsuarioDTO a Usuario antes de asignarlo
        UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuario(reservaDTO.usuarioId());
        Usuario usuario = convertirADominio(usuarioDTO);
        reserva.setUsuario(usuario);

        // Obtener libros y asignarlos a la reserva
        List<LibroDTO> librosDTO;
        try {
            librosDTO = reservaDTO.libroIds().stream()
                    .map(libroId -> {
                        try {
                            return libroServicio.obtenerLibro(libroId);
                        } catch (Exception e) {
                            throw new RuntimeException("Error al obtener libro con ID: " + libroId, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new Exception("Error al obtener uno o más libros: " + e.getMessage(), e);
        }

        // Marcar los libros como no disponibles
        for (LibroDTO libroDTO : librosDTO) {
            Libro libro = libroServicio.convertirADominio(libroDTO);
            libro.setDisponible(false);
            // Convertir de nuevo a DTO para actualizar
            LibroDTO libroDTOActualizado = libroServicio.convertirADTO(libro);
            libroServicio.actualizarLibro(libroDTOActualizado);
        }


        // Convertir `LibroDTO` a entidad `Libro`
        List<Libro> libros = librosDTO.stream()
                .map(libroServicio::convertirADominio)
                .collect(Collectors.toList());

        reserva.setLibros(libros);
        reserva = reservaRepo.save(reserva);

        // Devolver el DTO de la reserva registrada
        return new ReservaDTO(
                reserva.getCodigo(),
                reserva.getFechaReserva(),
                reserva.getFechaDevolucion(),
                reserva.getAnio(),
                reserva.getUsuario().getCodigo(),
                librosDTO.stream().map(LibroDTO::isbn).collect(Collectors.toList())
        );
    }

    @Override
    public ReservaDTO obtenerReserva(Long id) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(id);
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }
        Reserva reserva = buscada.get();

        return new ReservaDTO(
                reserva.getCodigo(),
                reserva.getFechaReserva(),
                reserva.getFechaDevolucion(),
                reserva.getAnio(),
                reserva.getUsuario().getCodigo(),
                reserva.getLibros().stream().map(Libro::getIsbn).collect(Collectors.toList())
        );
    }

    @Override
    public ReservaDTO actualizarReserva(ReservaDTO reservaDTO) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(reservaDTO.codigo());
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }

        Reserva reserva = buscada.get();
        reserva.setFechaReserva(reservaDTO.fechaReserva());
        reserva.setFechaDevolucion(reservaDTO.fechaDevolucion());
        reserva.setAnio(reservaDTO.anio());

        // Convertir UsuarioDTO a Usuario antes de asignarlo
        UsuarioDTO usuarioDTO = usuarioServicio.obtenerUsuario(reservaDTO.usuarioId());
        Usuario usuario = convertirADominio(usuarioDTO);
        reserva.setUsuario(usuario);

        // Actualizar los libros en la reserva
        List<LibroDTO> librosDTO;
        try {
            librosDTO = reservaDTO.libroIds().stream()
                    .map(libroId -> {
                        try {
                            return libroServicio.obtenerLibro(libroId);
                        } catch (Exception e) {
                            throw new RuntimeException("Error al obtener libro con ID: " + libroId, e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new Exception("Error al obtener uno o más libros: " + e.getMessage(), e);
        }

        // Marcar los libros como no disponibles
        for (LibroDTO libroDTO : librosDTO) {
            Libro libro = libroServicio.convertirADominio(libroDTO);
            libro.setDisponible(false);
            // Convertir de nuevo a DTO para actualizar
            LibroDTO libroDTOActualizado = libroServicio.convertirADTO(libro);
            libroServicio.actualizarLibro(libroDTOActualizado);
        }


        // Convertir `LibroDTO` a entidad `Libro`
        List<Libro> libros = librosDTO.stream()
                .map(libroServicio::convertirADominio)
                .collect(Collectors.toList());

        reserva.setLibros(libros);
        reserva = reservaRepo.save(reserva);

        return new ReservaDTO(
                reserva.getCodigo(),
                reserva.getFechaReserva(),
                reserva.getFechaDevolucion(),
                reserva.getAnio(),
                reserva.getUsuario().getCodigo(),
                librosDTO.stream().map(LibroDTO::isbn).collect(Collectors.toList())
        );
    }

    @Override
    public void eliminarReserva(Long id) throws Exception {
        Optional<Reserva> buscada = reservaRepo.findById(id);
        if (buscada.isEmpty()) {
            throw new Exception("La reserva con el ID proporcionado no existe.");
        }
        Reserva reserva = buscada.get();
        reservaRepo.delete(reserva);

        // Marcar los libros como disponibles
        for (Libro libro : reserva.getLibros()) {
            libro.setDisponible(true);
            libroServicio.actualizarLibro(libroServicio.convertirADTO(libro));
        }
    }

    @Override
    public List<ReservaDTO> listarReservas() {
        return reservaRepo.findAll().stream()
                .map(reserva -> new ReservaDTO(
                        reserva.getCodigo(),
                        reserva.getFechaReserva(),
                        reserva.getFechaDevolucion(),
                        reserva.getAnio(),
                        reserva.getUsuario().getCodigo(),
                        reserva.getLibros().stream()
                                .map(Libro::getIsbn)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservaDTO> buscarReservasPorUsuario(Long usuarioCodigo) {
        try {
            // Obtener el usuario desde la base de datos usando el código
            Usuario usuario = usuarioRepo.findById(usuarioCodigo)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            // Buscar reservas por el usuario en la base de datos
            return reservaRepo.findByUsuario(usuario).stream()
                    .map(reserva -> new ReservaDTO(
                            reserva.getCodigo(),
                            reserva.getFechaReserva(),
                            reserva.getFechaDevolucion(),
                            reserva.getAnio(),
                            reserva.getUsuario().getCodigo(),
                            reserva.getLibros().stream()
                                    .map(Libro::getIsbn)
                                    .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Manejar la excepción y proporcionar más información
            throw new RuntimeException("Error al buscar reservas por usuario", e);
        }
    }




    private Usuario convertirADominio(UsuarioDTO usuarioDTO) {
        // Asumimos que UsuarioDTO tiene un método para obtener el género
        GeneroPersona generoPersona = usuarioDTO.genero(); // O alguna conversión si es necesario

        return new Usuario(
                usuarioDTO.cedula(),
                usuarioDTO.email(),
                generoPersona,
                usuarioDTO.nombre(),
                usuarioDTO.password() // Asegúrate de que `UsuarioDTO` tenga un campo para la contraseña
        );
    }
}
