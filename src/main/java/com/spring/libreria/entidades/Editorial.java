package com.spring.libreria.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete (sql = "UPDATE editorial SET alta = false WHERE id = ?")
public class Editorial {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private boolean alta;
    @OneToMany (mappedBy = "editorial")
    private List<Libro> libros;

    public Editorial() {
        alta = true;
    }

    public Editorial(int id, String nombre, List<Libro> libros) {
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
        if (!(obj instanceof Editorial)) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }

    
}
