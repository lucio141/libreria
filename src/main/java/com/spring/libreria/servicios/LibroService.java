package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Libro;
import com.spring.libreria.exceptions.AutorRemovedException;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.EditorialRemovedException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.repositorios.AutorRepositorio;
import com.spring.libreria.repositorios.EditorialRepositorio;
import com.spring.libreria.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroService {

    @Autowired
    private LibroRepositorio repositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(long isbn, String titulo, int anio, int ejemplares, int autorId, int editorialId) throws RepeatedObjectException, DeletedObjectException {
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAutor(autorRepositorio.findById(autorId).orElse(null));
        libro.setEditorial(editorialRepositorio.findById(editorialId).orElse(null));

        if (obtenerLibro().contains(libro)) {
            throw new RepeatedObjectException("Ya existe un libro registrado con los mismos datos");
        } else if (obtenerLibroEliminado().contains(libro)) {
            throw new DeletedObjectException("Existe un libro eliminado con los mismos datos");
        }

        repositorio.save(libro);
    }

    @Transactional
    public void modificarLibro(int id, long isbn, String titulo, int anio, int ejemplares, int autorId, int editorialId) throws NullObjectException, RepeatedObjectException, DeletedObjectException {
        Libro libro = repositorio.findById(id).orElse(null);
        Libro libroAux = new Libro();

        if (libro == null) {
            throw new NullObjectException("No existe un autor asociado al id " + id);
        }

        libroAux.setIsbn(isbn);
        libroAux.setTitulo(titulo);
        libroAux.setAnio(anio);
        libroAux.setEjemplares(ejemplares);
        libroAux.setEjemplaresRestantes(ejemplares);
        libroAux.setAutor(autorRepositorio.findById(autorId).orElse(null));
        libroAux.setEditorial(editorialRepositorio.findById(editorialId).orElse(null));

        if (!libroAux.equals(libro)) {
            if (obtenerLibro().contains(libro)) {
                throw new RepeatedObjectException("Ya existe un libro registrado con los mismos datos");
            } else if (obtenerLibroEliminado().contains(libro)) {
                throw new DeletedObjectException("Existe un libro eliminado con los mismos datos");
            }
        }
        repositorio.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> obtenerLibro() {
        return repositorio.getByAlta(true);
    }

    @Transactional(readOnly = true)
    public List<Libro> obtenerLibroEliminado() {
        return repositorio.getByAlta(false);
    }

    @Transactional
    public Libro obtenerPorId(int id) throws NullObjectException {
        Libro libro = repositorio.findById(id).orElse(null);
        
         if (libro == null) {
            throw new NullObjectException("No existe un usuario asociado al id " + id);
        }
        
        return libro;
    }

    @Transactional
    public void delete(int id) {
        repositorio.deleteById(id);
    }

    @Transactional
    public void recuperar(int id) throws NullObjectException, AutorRemovedException, EditorialRemovedException {
        Libro libro = repositorio.findById(id).orElse(null);
        
        if(libro==null){
            throw new NullObjectException("No existe un autor asociado al id " + id);
        }
        
        if(!libro.getAutor().isAlta()){
            throw new AutorRemovedException("El libro no puede ser recuperado porque su autor se encuentra eliminado");
        }
        
         if(!libro.getEditorial().isAlta()){
            throw new EditorialRemovedException("El libro no puede ser recuperado porque su editorial se encuentra eliminada");
        }
        
        repositorio.recuperar(id);

    }

}
