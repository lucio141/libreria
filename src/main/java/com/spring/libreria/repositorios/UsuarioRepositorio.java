package com.spring.libreria.repositorios;

import com.spring.libreria.entidades.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    @Modifying
    @Query("UPDATE Usuario u SET u.alta= true WHERE u.id = :id")
    void recuperar(@Param("id") Integer id);

    @Query("Select from Usuario u WHERE u.alta = :alta")
    List<Usuario> getByAlta(@Param("alta") boolean Alta);
    
    @Query("Select from Usuario u WHERE u.userName = :userName")
    Usuario buscarUsuarioPorUsername(@Param("userName") String userName);
    
    Optional<Usuario> findByUserName(String userName);
}
