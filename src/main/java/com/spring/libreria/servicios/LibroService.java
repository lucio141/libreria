package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.entidades.Libro;
import com.spring.libreria.repositorios.AutorRepositorio;
import com.spring.libreria.repositorios.EditorialRepositorio;
import com.spring.libreria.repositorios.LibroRepositorio;
import java.util.ArrayList;
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
    public void crearLibro(long isbn, String titulo, int anio, int ejemplares, int autorId, int editorialId) {
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAutor(autorRepositorio.findById(autorId).orElse(null));
        libro.setEditorial(editorialRepositorio.findById(editorialId).orElse(null));
        repositorio.save(libro);
    }

    @Transactional
    public void modificarLibro(int id, long isbn, String titulo, int anio, int ejemplares, int autorId, int editorialId) {
        Libro libro = repositorio.getById(id);

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAutor(autorRepositorio.findById(autorId).orElse(null));
        libro.setEditorial(editorialRepositorio.findById(editorialId).orElse(null));

        repositorio.save(libro);
    }
    
    public List<Libro> listarLibros(boolean alta){
        List<Libro> libros = repositorio.findAll();
        for (Libro libro : libros) {
            if(libro.isAlta()!=alta){
                libros.remove(libro);
            }
        }
        return libros;
    }
    
    @Transactional(readOnly = true)
    public List<Libro> obtenerLibro() {
        if(listarLibros(true).isEmpty()){
            List<Libro> libros = null;
            return libros;
        }
        return listarLibros(true);
    }

    @Transactional(readOnly = true)
    public List<Libro> obtenerLibroBorrado() {
        return listarLibros(false);
    }

    @Transactional
    public Libro obtenerPorId(int id) {
        Optional<Libro> libro = repositorio.findById(id);
        return libro.orElse(null);
    }

    @Transactional
    public void delete(int id) {
        repositorio.deleteById(id);
    }

    @Transactional
    public void recuperar(int id) {
        repositorio.recuperar(id);
    }

}
