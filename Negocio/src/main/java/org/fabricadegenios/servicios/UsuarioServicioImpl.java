package org.fabricadegenios.servicios;

import org.fabricadegenios.model.Usuario;
import org.fabricadegenios.repositorios.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements UsuarioServicio{

    private final UsuarioRepo usuarioRepo;

    public UsuarioServicioImpl(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Usuario registrarUsuario(Usuario u) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findByEmail(u.getEmail());
        if (buscado.isPresent()) {
            throw new Exception("El email ya se encuentra en uso");
        }
        return usuarioRepo.save(u);
    }



    @Override
    public Usuario obtenerUsuario(Long id) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(id);
        if(buscado.isEmpty()){
            throw new Exception("El código no se encuentra en uso");
        }
        return buscado.get();
    }

    @Override
    public Usuario actualizarUsuario(Usuario u) throws Exception {
        return usuarioRepo.save(u);
    }

    @Override
    public void eliminarUsuario(Usuario u) throws Exception {
        Optional<Usuario> buscado = usuarioRepo.findById(u.getCodigo());
        if(buscado.isEmpty()){
            throw new Exception("El código no se encuentra en uso");
        }
        usuarioRepo.delete(u);

    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario obtenerUsuariosPorCedula(String cedula) throws Exception {
        Optional<Usuario> usuario = usuarioRepo.findByCedula(cedula);
        if(usuario.isEmpty()){
            throw new Exception("No existe un usuario con el cedula dada");
        }
        return usuario.get();
    }

    @Override
    public List<Usuario> findAllByNombreContainsIgnoreCase(String nombre) {
        return usuarioRepo.findAllByNombreContainsIgnoreCase(nombre);
    }

    @Override
    public Optional<Usuario> findByEmailAndPassword(String email, String password) {
        return usuarioRepo.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<Usuario> findByCedulaAndPassword(String cedula, String password) {
        return usuarioRepo.findByCedulaAndPassword(cedula, password);
    }

    @Override
    public Page<Usuario> paginarUsuarios(Pageable pageable) {
        return usuarioRepo.findAll(pageable);
    }
    //
    public boolean estaDisponible(String email){
        Optional<Usuario> usuario = usuarioRepo.findByEmail(email);
        return usuario.isEmpty();
    }



}
