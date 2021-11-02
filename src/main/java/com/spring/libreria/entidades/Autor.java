package com.spring.libreria.entidades;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@SQLDelete (sql = "UPDATE autor SET alta = false WHERE id = ?")
@EntityListeners(AuditingEntityListener.class)
public class Autor {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private boolean alta;
    @OneToMany (mappedBy = "autor")
    private List<Libro> libros;

//    @CreatedDate
//    @Column(nullable = false, updatable= false)
//    private LocalDateTime creacion;
//    
//    @LastModifiedDate
//    private LocalDateTime modificaion;
    
    public Autor() {
        alta=true;
    }

    public Autor(int id, String nombre, List<Libro> libros) {
        this.id = id;
        this.nombre = nombre;
        this.alta = true;
        this.libros=libros;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int hashCode() {
        return this.toString().toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Autor)) return false;
        return this.hashCode()==obj.hashCode();
    }
    
}
