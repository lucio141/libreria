
package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Usuario;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void crearAutor(String nombre) throws RepeatedObjectException, DeletedObjectException {
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);

        if (obtenerAutor().contains(autor)) {
            throw new RepeatedObjectException("Ya existe un autor registrado con el nombre " + nombre);
        } else if (obtenerAutorEliminado().contains(usuario)) {
            throw new DeletedObjectException("Existe un autor eliminado con el nombre " + nombre + ". ¿Desea recuperarlo?");
        }

        repositorio.save(usuario);
    }
    
    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
