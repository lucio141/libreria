
package com.spring.libreria.servicios;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepositorio repositorio;
    
    @Transactional
    public void crearAutor(String nombre){
        Autor autor = new Autor();
        
        autor.setNombre(nombre);
        
        repositorio.save(autor);
    }
    
    @Transactional
    public void modificarAutor(int id, String nombre){
        Autor autor = repositorio.getById(id);
        
        autor.setNombre(nombre);
        
        repositorio.save(autor);
    }
    
    @Transactional(readOnly = true)
    public List<Autor> obtenerAutor(){
        return repositorio.getByAlta(true);
    }
    
    @Transactional(readOnly = true)
    public List<Autor> obtenerAutorEliminado(){
        return repositorio.getByAlta(false);
    }
        
    @Transactional
    public Autor obtenerPorId(int id){
        Optional<Autor> autor = repositorio.findById(id);
        return  autor.orElse(null);
    }
    
    @Transactional
    public void delete(int id){
        repositorio.deleteById(id);
    }
    
    @Transactional
    public void recuperar(int id){
        repositorio.recuperar(id);
    }
}
