package com.spring.libreria.repositorios;

import com.spring.libreria.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {

    @Modifying
    @Query("UPDATE Editorial e SET e.alta= true WHERE e.id = :id")
    void recuperar(@Param("id") Integer id);

    @Query("from Editorial e WHERE e.alta = :alta")
    List<Editorial> getByAlta(@Param("alta") boolean Alta);
}
