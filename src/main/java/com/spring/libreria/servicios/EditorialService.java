package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.entidades.Libro;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.repositorios.EditorialRepositorio;
import com.spring.libreria.repositorios.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepositorio repositorio;

    @Autowired
    private LibroRepositorio repositorioLibro;

    @Transactional
    public void crearEditorial(String nombre) throws RepeatedObjectException, DeletedObjectException {
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);

        if (obtenerEditorial().contains(editorial)) {
            throw new RepeatedObjectException("Ya existe una editorial registrado con el nombre " + nombre);
        } else if (obtenerEditorialEliminada().contains(editorial)) {
            throw new DeletedObjectException("Existe una editorial eliminada con el nombre " + nombre + ". ¿Desea recuperarla?");
        }

        repositorio.save(editorial);
    }

    @Transactional
    public void modificarEditorial(int id, String nombre) throws RepeatedObjectException, DeletedObjectException, NullObjectException {
        Editorial editorial = repositorio.findById(id).get();

        if (editorial == null) {
            throw new NullObjectException("No existe una editorial asociada al id " + id);
        }

        if (!(nombre.equalsIgnoreCase(editorial.getNombre()))) {
            if (obtenerEditorial().contains(editorial)) {
                throw new RepeatedObjectException("Ya existe una editorial registrado con el nombre " + nombre);
            } else if (obtenerEditorialEliminada().contains(editorial)) {
                throw new DeletedObjectException("Existe una editorial eliminada con el nombre " + nombre + ". ¿Desea recuperarla?");
            }
        }

        editorial.setNombre(nombre);
        repositorio.save(editorial);
    }

    @Transactional(readOnly = true)
    public List<Editorial> obtenerEditorial() {
        return repositorio.getByAlta(true);
    }

    @Transactional(readOnly = true)
    public List<Editorial> obtenerEditorialEliminada() {
        return repositorio.getByAlta(false);
    }

    @Transactional
    public Editorial obtenerPorId(int id) throws NullObjectException {
        Editorial editorial = repositorio.findById(id).orElse(null);
        if (editorial == null) {
            throw new NullObjectException("No existe un usuario asociado al id " + id);
        }
        return editorial;
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
