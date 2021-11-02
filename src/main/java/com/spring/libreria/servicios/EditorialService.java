package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepositorio repositorio;

    @Transactional
    public void crearEditorial(String nombre) {
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        repositorio.save(editorial);
    }

    @Transactional
    public void modificarEditorial(int id, String nombre) {
        Editorial editorial = repositorio.findById(id).get();
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
    public Editorial obtenerPorId(int id) {
        Optional<Editorial> editorial = repositorio.findById(id);
        return editorial.orElse(null);
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
