package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.entidades.Libro;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.repositorios.AutorRepositorio;
import com.spring.libreria.repositorios.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorService {

    @Autowired
    private AutorRepositorio repositorio;
    
    @Autowired
    private LibroRepositorio repositorioLibro;
    
    

    @Transactional
    public void crearAutor(String nombre) throws RepeatedObjectException, DeletedObjectException {
        Autor autor = new Autor();

        autor.setNombre(nombre);

        if (obtenerAutor().contains(autor)) {
            throw new RepeatedObjectException("Ya existe un autor registrado con el nombre " + nombre);
        } else if (obtenerAutorEliminado().contains(autor)) {
            throw new DeletedObjectException("Existe un autor eliminado con el nombre " + nombre + ". ¿Desea recuperarlo?");
        }

        repositorio.save(autor);
    }

    @Transactional
    public void modificarAutor(int id, String nombre) throws NullObjectException, RepeatedObjectException, DeletedObjectException {
        Autor autor = repositorio.findById(id).orElse(null);

        if (autor == null) {
            throw new NullObjectException("No existe un autor asociado al id " + id);
        }

        if (!(nombre.equalsIgnoreCase(autor.getNombre()))) {
            if (obtenerAutor().contains(autor)) {
                throw new RepeatedObjectException("Ya existe un autor registrado con el nombre " + nombre);
            } else if (obtenerAutorEliminado().contains(autor)) {
                throw new DeletedObjectException("Existe un autor eliminado con el nombre " + nombre + ". ¿Desea recuperarlo?");
            }
        }
        autor.setNombre(nombre);
        repositorio.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> obtenerAutor() {
        return repositorio.getByAlta(true);
    }

    @Transactional(readOnly = true)
    public List<Autor> obtenerAutorEliminado() {
        return repositorio.getByAlta(false);
    }

    @Transactional
    public Autor obtenerPorId(int id) throws NullObjectException {
        Autor autor = repositorio.findById(id).orElse(null);

        if (autor == null) {
            throw new NullObjectException("No existe un usuario asociado al id " + id);
        }

        return autor;
    }

    @Transactional
    public void delete(int id) {
       
        for (Libro Libro : repositorio.getById(id).getLibros()) {
            repositorioLibro.delete(Libro);
        }

        repositorio.deleteById(id);
        

    }

    @Transactional
    public void recuperar(int id) {
        repositorio.recuperar(id);
    }
}
