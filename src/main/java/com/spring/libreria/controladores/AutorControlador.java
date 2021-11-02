
package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.servicios.AutorService;
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
@RequestMapping("/autores")
public class AutorControlador {
    
    @Autowired
    private AutorService servicio;
   
    
    @GetMapping()
    public ModelAndView mostrarAutores(){
        ModelAndView mav = new ModelAndView("autores-lista");
        List<Autor> autores = servicio.obtenerAutor();
        mav.addObject("autores", autores);
        mav.addObject("title", "Tabla de autores");
        
        return mav;
    }
    
    @GetMapping("/eliminados")
    public ModelAndView mostrarAutoresEliminados(){
        ModelAndView mav = new ModelAndView("autores-lista-eliminados");
        List<Autor> autores = servicio.obtenerAutorEliminado();
        mav.addObject("autores", autores);
        mav.addObject("title", "Tabla de autores eliminados");
        
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearAutor(){
        ModelAndView mav = new ModelAndView("autores-formulario");
        mav.addObject("autor",new Autor());
        mav.addObject("title","Crear Autor");
        mav.addObject("action","guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarAutor(@PathVariable int id){
        ModelAndView mav = new ModelAndView("autores-formulario");
        mav.addObject("autor", servicio.obtenerPorId(id));
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre){
        servicio.crearAutor(nombre);
        return new RedirectView("/autores");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam String nombre){
        servicio.modificarAutor(id, nombre);
        return new RedirectView("/autores");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id){
        servicio.delete(id);
        return new RedirectView("/autores");
    }
    
    @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id){
        servicio.recuperar(id);
        return new RedirectView("/autores/eliminados");
    }
    
}
