
package com.spring.libreria.entidades;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@SQLDelete(sql = "UPDATE autor SET alta = false WHERE id = ?")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    @Column(nullable = false, unique = true)
    private String userName;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, unique = true)
    private String mail;
    
    private String nombre;
    
    private String apellido;
        
    private Long dni;
    
    private LocalDate fechaDeNacimiento;
    
    @CreatedDate
    @Column(nullable =false, updatable = false)
    private LocalDate Creacion;
    
    @LastModifiedDate
    private LocalDate Modificacion;
    
    private boolean alta;

    public Usuario() {
        alta=true;
    }

    public Usuario(int Id, String userName, String password, String mail, String nombre, String apellido, Long dni, LocalDate fechaDeNacimiento, LocalDate Creacion, LocalDate Modificacion) {
        this.Id = Id;
        this.userName = userName;
        this.password = password;
        this.mail = mail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.Creacion = Creacion;
        this.Modificacion = Modificacion;
        this.alta = true;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public LocalDate getCreacion() {
        return Creacion;
    }

    public void setCreacion(LocalDate Creacion) {
        this.Creacion = Creacion;
    }

    public LocalDate getModificacion() {
        return Modificacion;
    }

    public void setModificacion(LocalDate Modificacion) {
        this.Modificacion = Modificacion;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }
    
    
    
    
    
}
