
package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Usuario;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.repositorios.UsuarioRepositorio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService implements UserDetailsService{

    
    @Autowired
    private UsuarioRepositorio repositorio;

    @Transactional
    public void crearUsuario(String userName, String password, String mail, String nombre, String apellido, Long dni, LocalDate fechaDeNacimiento) {
    
        Usuario usuario = new Usuario();
        
        usuario.setUserName(userName);
        usuario.setPassword(password);
        usuario.setMail(mail);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        
        repositorio.save(usuario);
    }

    @Transactional
    public void modificarUsuario(int id, String userName, String password, String mail, String nombre, String apellido, Long dni, LocalDate fechaDeNacimiento) {
    
        Usuario usuario = repositorio.findById(id).orElse(null);
        
        usuario.setUserName(userName);
        usuario.setPassword(password);
        usuario.setMail(mail);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setFechaDeNacimiento(fechaDeNacimiento);
        
        repositorio.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuario() {
        return repositorio.getByAlta(true);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuarioEliminado() {
        return repositorio.getByAlta(false);
    }

    @Transactional
    public Usuario obtenerPorId(int id) throws NullObjectException {
        Usuario usuario = repositorio.findById(id).orElse(null);

        if (usuario == null) {
            throw new NullObjectException("No existe un usuario asociado al id " + id);
        }

        return usuario;
    }

    @Transactional
    public void delete(int id) {
        repositorio.deleteById(id);
    }

    @Transactional
    public void recuperar(int id) {
        repositorio.recuperar(id);
    }
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Usuario usuario = repositorio.findByUserName(userName).orElseThrow(() -> {
            NullObjectException nullObjectException = new NullObjectException("no se encontro un usuario con el userName " + userName);
        });
        
        
        return User user = new User(usuario.getUserName(), usuario.getPassword(), Collections.emptyList());
    }
    
}
