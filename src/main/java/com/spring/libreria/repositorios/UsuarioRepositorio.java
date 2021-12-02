package com.spring.libreria.repositorios;

import com.spring.libreria.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    @Modifying
    @Query("UPDATE Usuario a SET a.alta= true WHERE a.id = :id")
    void recuperar(@Param("id") Integer id);

    @Query("from Autor a WHERE a.alta = :alta")
    List<Usuario> getByAlta(@Param("alta") boolean Alta);
}
