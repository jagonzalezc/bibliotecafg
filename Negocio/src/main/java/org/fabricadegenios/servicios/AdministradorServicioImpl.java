package org.fabricadegenios.servicios;

import lombok.RequiredArgsConstructor;
import org.fabricadegenios.dto.AutorDTO;
import org.fabricadegenios.dto.LibroDTO;
import org.fabricadegenios.dto.ReservaDTO;
import org.fabricadegenios.dto.UsuarioDTO;
import org.fabricadegenios.dto.admin.RegistroAutorDTO;
import org.fabricadegenios.dto.admin.RegistroLibroDTO;
import org.fabricadegenios.dto.admin.RegistroReservaDTO;
import org.fabricadegenios.dto.admin.RegistroUsuarioDTO;
import org.fabricadegenios.model.*;
import org.fabricadegenios.repositorios.AutorRepo;
import org.fabricadegenios.repositorios.LibroRepo;
import org.fabricadegenios.repositorios.ReservaRepo;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministradorServicioImpl implements AdministradorServicio {

    private final UsuarioRepo usuarioRepo;
    private final AutorRepo autorRepo;
    private final LibroRepo libroRepo;
    private final ReservaRepo reservaRepo;

    // MÉTODOS DE USUARIO
    @Override
    public Long registrarUsuario(RegistroUsuarioDTO registroUsuarioDTO) {
        usuarioRepo.findByCedula(registroUsuarioDTO.cedula())
                .ifPresent(usuario -> {
                    throw new RuntimeException("El usuario con la cédula ya existe");
                });

        Usuario usuario = convertirADominioUsuario(registroUsuarioDTO);
        return usuarioRepo.save(usuario).getCodigo();
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long codigo, RegistroUsuarioDTO registroUsuarioDTO) {
        Usuario usuario = usuarioRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El usuario con el código proporcionado no existe."));

        usuario.setNombre(registroUsuarioDTO.nombre());
        usuario.setEmail(registroUsuarioDTO.email());
        usuario.setCedula(registroUsuarioDTO.cedula());
        usuario.setRol(Rol.valueOf(registroUsuarioDTO.rol()));
        usuario.setPassword(registroUsuarioDTO.password());

        return convertirADTOUsuario(usuarioRepo.save(usuario));
    }

    @Override
    public UsuarioDTO obtenerUsuario(Long codigo) {
        Usuario usuario = usuarioRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El usuario con el código proporcionado no existe."));
        return convertirADTOUsuario(usuario);
    }

    @Override
    public void eliminarUsuario(Long codigo) {
        Usuario usuario = usuarioRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El usuario con el código proporcionado no existe."));
        usuarioRepo.delete(usuario);
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepo.findAll().stream()
                .map(this::convertirADTOUsuario)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO obtenerUsuariosPorCedula(String cedula) {
        Usuario usuario = usuarioRepo.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con la cédula dada"));
        return convertirADTOUsuario(usuario);
    }

    @Override
    public List<UsuarioDTO> findAllByNombreContainsIgnoreCase(String nombre) {
        return usuarioRepo.findAllByNombreContainsIgnoreCase(nombre).stream()
                .map(this::convertirADTOUsuario)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioDTO> findByEmailAndPassword(String email, String password) {
        return usuarioRepo.findByEmailAndPassword(email, password)
                .map(this::convertirADTOUsuario);
    }

    @Override
    public Optional<UsuarioDTO> findByCedulaAndPassword(String cedula, String password) {
        return usuarioRepo.findByCedulaAndPassword(cedula, password)
                .map(this::convertirADTOUsuario);
    }

    @Override
    public Page<UsuarioDTO> paginarUsuarios(Pageable pageable) {
        return usuarioRepo.findAll(pageable).map(this::convertirADTOUsuario);
    }

    // MÉTODOS DE AUTOR
    @Override
    public Long registrarAutor(RegistroAutorDTO registroAutorDTO) {
        autorRepo.findByNombre(registroAutorDTO.nombre())
                .ifPresent(autor -> {
                    throw new RuntimeException("El autor con el nombre ya existe.");
                });

        Autor autor = convertirADominioAutor(registroAutorDTO);
        return autorRepo.save(autor).getCodigo();
    }

    @Override
    public AutorDTO actualizarAutor(Long codigo, RegistroAutorDTO registroAutorDTO) {
        Autor autor = autorRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El autor con el código proporcionado no existe."));

        autor.setNombre(registroAutorDTO.nombre());
        autor.setAnio(registroAutorDTO.anio());

        return convertirADTOAutor(autorRepo.save(autor));
    }

    @Override
    public AutorDTO obtenerAutor(Long codigo) {
        Autor autor = autorRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El autor con el código proporcionado no existe."));
        return convertirADTOAutor(autor);
    }

    @Override
    public void eliminarAutor(Long codigo) {
        Autor autor = autorRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El autor con el código proporcionado no existe."));
        autorRepo.delete(autor);
    }

    @Override
    public List<AutorDTO> listarAutores() {
        return autorRepo.findAll().stream()
                .map(this::convertirADTOAutor)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AutorDTO> paginarAutores(Pageable pageable) {
        // Obtiene una página de autores desde el repositorio
        Page<Autor> autores = autorRepo.findAll(pageable);

        // Convierte la entidad Autor a AutorDTO
        return autores.map(this::convertirADTOAutor);
    }


    public Page<AutorDTO> buscarAutoresPorNombre(String nombre, Pageable pageable) {
        // Busca autores por nombre en el repositorio
        Page<Autor> autores = autorRepo.findByNombreContainingIgnoreCase(nombre, pageable);

        // Convierte la entidad Autor a AutorDTO
        return autores.map(this::convertirADTOAutor);
    }

    @Override
    public List<AutorDTO> listarAutoresPorIds(List<Long> ids) {
        return autorRepo.findAllById(ids).stream()
                .map(this::convertirADTOAutor)
                .collect(Collectors.toList());
    }

    // MÉTODOS DE LIBRO
    @Override
    public Long registrarLibro(RegistroLibroDTO registroLibroDTO) {
        libroRepo.findByIsbn(registroLibroDTO.isbn())
                .ifPresent(libro -> {
                    throw new RuntimeException("El serial ISBN ya se encuentra en uso.");
                });

        if (!esGeneroLibroValido(registroLibroDTO.genero())) {
            throw new RuntimeException("No existe el género ingresado.");
        }

        Libro libro = convertirADominioLibro(registroLibroDTO);
        return libroRepo.save(libro).getCodigo();
    }

    @Override
    public LibroDTO actualizarLibro(Long codigo, RegistroLibroDTO registroLibroDTO) {
        Libro libro = libroRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El libro con el código proporcionado no existe."));

        if (!esGeneroLibroValido(registroLibroDTO.genero())) {
            throw new RuntimeException("No existe el género ingresado.");
        }

        libro.setNombre(registroLibroDTO.nombre());
        libro.setEditorial(registroLibroDTO.editorial());
        libro.setAnio(registroLibroDTO.anio());
        libro.setDisponible(registroLibroDTO.disponible());
        libro.setGenero(GeneroLibro.valueOf(registroLibroDTO.genero()));

        return convertirADTOLibro(libroRepo.save(libro));
    }

    @Override
    public void asignarAutoresAlLibro(Long codigo, List<Long> autorIds) {
        Libro libro = libroRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El libro con el código proporcionado no existe."));

        List<Autor> autores = autorRepo.findAllById(autorIds);
        libro.setAutores(autores);
        libroRepo.save(libro);
    }

    @Override
    public LibroDTO obtenerLibro(Long codigo) {
        Libro libro = libroRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El libro con el código proporcionado no existe."));
        return convertirADTOLibro(libro);
    }

    @Override
    public void eliminarLibro(Long codigo) {
        Libro libro = libroRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("El libro con el código proporcionado no existe."));
        libroRepo.delete(libro);
    }

    @Override
    public List<LibroDTO> listarLibros() {
        return libroRepo.findAll().stream()
                .map(this::convertirADTOLibro)
                .collect(Collectors.toList());
    }

    @Override
    public Page<LibroDTO> paginarLibros(Pageable pageable) {
        return libroRepo.findAll(pageable).map(this::convertirADTOLibro);
    }

    @Override
    public Page<LibroDTO> buscarLibrosPorAutor(String nombreAutor, Pageable pageable) {
        return libroRepo.findByAutorNombre(nombreAutor, pageable).map(this::convertirADTOLibro);
    }

    @Override
    public List<LibroDTO> buscarLibrosPorGenero(String genero) {
        return libroRepo.findAllByGenero(genero).stream()
                .map(this::convertirADTOLibro)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroDTO> buscarLibrosDisponiblesPorGenero(String genero, LocalDate hoy) {
        return libroRepo.findAvailableBooksByGenre(genero, hoy).stream()
                .map(this::convertirADTOLibro)
                .collect(Collectors.toList());
    }

    @Override
    public Long registrarReserva(RegistroReservaDTO registroReservaDTO) {
        if (registroReservaDTO.libroIds().isEmpty()) {
            throw new RuntimeException("La reserva debe incluir al menos un libro.");
        }

        // Obtener la lista de libros disponibles y validar disponibilidad
        List<LibroDTO> librosDTO = registroReservaDTO.libroIds().stream()
                .map(this::obtenerLibro)
                .peek(libro -> {
                    if (!libro.disponible()) {
                        throw new RuntimeException("Uno o más libros no están disponibles.");
                    }
                })
                .toList();

        // Convertir el DTO a la entidad Reserva
        Reserva reserva = convertirADominioReserva(registroReservaDTO);

        // Asegúrate de que la lista de libros esté inicializada
        if (reserva.getLibros() == null) {
            reserva.setLibros(new ArrayList<>());
        }

        // Asignar los libros a la reserva
        librosDTO.forEach(libroDTO -> {
            Libro libro = libroRepo.findById(libroDTO.codigo())
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
            reserva.getLibros().add(libro);
            libro.setDisponible(false); // Marcar el libro como no disponible
        });

        // Guardar la reserva y retornar el código
        return reservaRepo.save(reserva).getCodigo();
    }

    @Override
    public void asignarLibrosAReserva(Long reservaId, List<Long> libroIds) throws Exception {
        // Obtener la reserva por ID
        Optional<Reserva> reservaOpt = reservaRepo.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }

        Reserva reserva = reservaOpt.get();

        // Verificar la disponibilidad de los libros
        for (Long libroId : libroIds) {
            Optional<Libro> libroOpt = libroRepo.findById(libroId);
            if (libroOpt.isEmpty()) {
                throw new Exception("Libro con ID " + libroId + " no encontrado");
            }
            Libro libro = libroOpt.get();
            if (!libro.getDisponible()) {
                throw new Exception("Libro con ID " + libroId + " no está disponible");
            }
        }

        // Asignar los libros a la reserva
        for (Long libroId : libroIds) {
            Optional<Libro> libroOpt = libroRepo.findById(libroId);
            Libro libro = libroOpt.get();
            reserva.getLibros().add(libro);
            // Actualizar la disponibilidad del libro
            libro.setDisponible(false);
            libroRepo.save(libro);
        }

        // Guardar la reserva actualizada
        reservaRepo.save(reserva);
    }

    @Override
    public ReservaDTO obtenerReserva(Long codigo) {
        Reserva reserva = reservaRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("La reserva con el código proporcionado no existe."));
        return convertirADTOReserva(reserva);
    }

    @Override
    public void actualizarReserva(Long reservaId, RegistroReservaDTO reservaActualizadaDTO) throws Exception {
        // Obtener la reserva existente
        Optional<Reserva> reservaOpt = reservaRepo.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            throw new Exception("Reserva no encontrada");
        }

        Reserva reserva = reservaOpt.get();

        // Actualizar los detalles de la reserva
        reserva.setFechaReserva(reservaActualizadaDTO.fechaReserva());
        reserva.setFechaDevolucion(reservaActualizadaDTO.fechaDevolucion());
        reserva.setAnio(reservaActualizadaDTO.anio());

        // Verificar y actualizar el usuario asociado a la reserva
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(reservaActualizadaDTO.usuarioId());
        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        reserva.setUsuario(usuarioOpt.get());

        // Actualizar los libros asociados a la reserva
        reserva.getLibros().clear(); // Limpiar libros actuales
        for (Long libroId : reservaActualizadaDTO.libroIds()) {
            Optional<Libro> libroOpt = libroRepo.findById(libroId);
            if (libroOpt.isEmpty()) {
                throw new Exception("Libro con ID " + libroId + " no encontrado");
            }
            Libro libro = libroOpt.get();
            reserva.getLibros().add(libro);
        }

        // Guardar la reserva actualizada
        reservaRepo.save(reserva);
    }

    @Override
    public void eliminarReserva(Long codigo) {
        Reserva reserva = reservaRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("La reserva con el código proporcionado no existe."));

        // Actualizar el estado de disponibilidad de los libros
        reserva.getLibros().forEach(libro -> {
            libro.setDisponible(true);
            libroRepo.save(libro); // Guardar el libro actualizado
        });

        // Eliminar la reserva
        reservaRepo.delete(reserva);
    }


    @Override
    public List<ReservaDTO> listarReservas() {
        return reservaRepo.findAll().stream()
                .map(this::convertirADTOReserva)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservaDTO> buscarReservasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("El usuario con el ID proporcionado no existe."));
        return reservaRepo.findByUsuario(usuario).stream()
                .map(this::convertirADTOReserva)
                .collect(Collectors.toList());
    }

    // MÉTODOS AUXILIARES
    private Usuario convertirADominioUsuario(RegistroUsuarioDTO dto) {
        GeneroPersona generoPersona;
        Rol rol;

        // Conversión de String a GeneroPersona
        try {
            generoPersona = GeneroPersona.valueOf(dto.genero().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("GeneroPersona inválido: " + dto.genero());
        }

        // Conversión de String a Rol
        try {
            rol = Rol.valueOf(dto.rol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + dto.rol());
        }

        return new Usuario(
                dto.cedula(),
                dto.email(),
                generoPersona,
                dto.nombre(),
                dto.password(),
                rol
        );
    }


    private UsuarioDTO convertirADTOUsuario(Usuario usuario) {
        return new UsuarioDTO(usuario.getCodigo(), usuario.getCedula(), usuario.getNombre(), usuario.getEmail(), usuario.getPassword(), usuario.getGenero(), usuario.getRol());
    }

    private Autor convertirADominioAutor(RegistroAutorDTO dto) {
        return new Autor(dto.nombre(), dto.anio());
    }

    private AutorDTO convertirADTOAutor(Autor autor) {
        return new AutorDTO(autor.getCodigo(), autor.getNombre(), autor.getAnio());
    }

    private Libro convertirADominioLibro(RegistroLibroDTO dto) {
        return new Libro(dto.isbn(), dto.nombre(), dto.editorial(), dto.anio(), dto.disponible(), GeneroLibro.valueOf(dto.genero()));
    }

    private LibroDTO convertirADTOLibro(Libro libro) {
        return new LibroDTO(
                libro.getCodigo(),
                libro.getIsbn(),
                libro.getNombre(),
                libro.getEditorial(),
                libro.getAnio(),
                libro.getDisponible(),
                libro.getGenero(),
                libro.getAutores() != null ? libro.getAutores().stream().map(Autor::getCodigo).collect(Collectors.toList()) : Collections.emptyList()
        );
    }


    private Reserva convertirADominioReserva(RegistroReservaDTO dto) {
        Usuario usuario = usuarioRepo.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reserva reserva = new Reserva();
        reserva.setAnio(dto.anio());
        reserva.setFechaDevolucion(dto.fechaDevolucion());
        reserva.setFechaReserva(dto.fechaReserva());
        reserva.setUsuario(usuario);



        return reserva;
    }



    private ReservaDTO convertirADTOReserva(Reserva reserva) {
        return new ReservaDTO(
                reserva.getCodigo(),
                reserva.getFechaReserva(),
                reserva.getFechaDevolucion(),
                reserva.getAnio(),
                reserva.getUsuario().getCodigo(),  // Asegúrate de que `getCodigo()` devuelve un `Long`
                reserva.getLibros().stream()
                        .map(Libro::getCodigo)          // Asegúrate de que `getCodigo()` devuelve un `Long`
                        .collect(Collectors.toList())
        );
    }


    // Implementación del método obtenerLibrosPorIsbn
    private List<Libro> obtenerLibrosPorId(List<Long> idList) {
        return idList.stream()
                .map(id -> libroRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("El libro con Id " + id + " no existe.")))
                .collect(Collectors.toList());
    }

    private boolean esGeneroLibroValido(String genero) {
        return Arrays.stream(GeneroLibro.values())
                .anyMatch(g -> g.name().equals(genero));
    }
}



