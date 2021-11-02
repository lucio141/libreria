
package com.spring.libreria.repositorios;

import com.spring.libreria.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro,Integer> {
    
    @Modifying
    @Query("UPDATE Libro l SET l.alta= true WHERE l.id = :id")
    void recuperar(@Param("id") Integer id); 
   
    @Query("from Libro l WHERE l.alta = :alta")
    List<Libro> getByAlta(@Param("alta")boolean Alta);
    
}
