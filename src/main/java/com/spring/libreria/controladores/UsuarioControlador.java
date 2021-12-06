package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Usuario;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.servicios.UsuarioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {
    @Autowired
    private UsuarioService servicio;
   
    
    @GetMapping()
    public ModelAndView mostrarUsuarios(){
        ModelAndView mav = new ModelAndView("autores-lista");//A CREAR

        
        List<Usuario> usuarios = servicio.obtenerUsuario();
        mav.addObject("usuarios", usuarios);
        mav.addObject("title", "Tabla de usuarios");
        
        
        return mav;
    }
    
    @GetMapping("/eliminados")
    public ModelAndView mostrarUsuariosEliminados(){
        ModelAndView mav = new ModelAndView("autores-lista-eliminados");//A CREAR
        List<Usuario> usuarios = servicio.obtenerUsuarioEliminado();
        mav.addObject("usuarios", usuarios);
        mav.addObject("title", "Tabla de usuarios eliminados");
        
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearUsuario(){
        ModelAndView mav = new ModelAndView("autores-formulario");//A CREAR
        
        mav.addObject("usuario",new Usuario());
        mav.addObject("title","Crear Usuario");
        mav.addObject("action","guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable int id){
        ModelAndView mav = new ModelAndView("usuarios-formulario");// A CREAR
      
        try {
            mav.addObject("usuario", servicio.obtenerPorId(id));
        } catch (NullObjectException ex) {
          
            mav.setViewName("redirect:/usuarios");
        }
        mav.addObject("title", "Editar Usuario");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String userName,@RequestParam String password,@RequestParam String mail,@RequestParam String nombre,@RequestParam String apellido,@RequestParam Long dni,@RequestParam LocalDate fechaDeNacimiento){

            servicio.crearUsuario(userName, password, mail, nombre, apellido, dni, fechaDeNacimiento);

        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id,@RequestParam String userName,@RequestParam String password,@RequestParam String mail,@RequestParam String nombre,@RequestParam String apellido,@RequestParam Long dni,@RequestParam LocalDate fechaDeNacimiento){
            
        servicio.modificarUsuario(id, userName, password, mail, nombre, apellido, dni, fechaDeNacimiento);
            
        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id){
        servicio.delete(id);
        return new RedirectView("/usuarios");
    }
    
    @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id){
        servicio.recuperar(id);
        return new RedirectView("/usuarios/eliminados");
    }
}
