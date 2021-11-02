package com.spring.libreria.repositorios;

import com.spring.libreria.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Integer> {

    @Modifying
    @Query("UPDATE Autor a SET a.alta= true WHERE a.id = :id")
    void recuperar(@Param("id") Integer id);

    @Query("from Autor a WHERE a.alta = :alta")
    List<Autor> getByAlta(@Param("alta") boolean Alta);
}
