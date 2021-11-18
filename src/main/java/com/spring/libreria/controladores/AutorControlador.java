
package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.servicios.AutorService;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/autores")
public class AutorControlador {
    
    @Autowired
    private AutorService servicio;
   
    
    @GetMapping()
    public ModelAndView mostrarAutores(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("autores-lista");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);
        
        if(map != null){
            mav.addObject("exito", map.get("exito"));
            mav.addObject("error", map.get("error"));
            mav.addObject("error-removed", map.get("error-removed"));
        }
        
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
    public ModelAndView crearAutor(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("autores-formulario");
        Map<String,?> map = RequestContextUtils.getInputFlashMap(request);
        
        if(map!=null){
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));            
        }
        
        mav.addObject("autor",new Autor());
        mav.addObject("title","Crear Autor");
        mav.addObject("action","guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarAutor(@PathVariable int id, RedirectAttributes attributes, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("autores-formulario");
        Map<String,?> map = RequestContextUtils.getInputFlashMap(request);
        
        if(map!=null){
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));            
        }
        
        try {
            mav.addObject("autor", servicio.obtenerPorId(id));
        } catch (NullObjectException ex) {
            attributes.addFlashAttribute("error", ex.getMessage());
            mav.setViewName("redirect:/autores");
        }
        mav.addObject("title", "Editar Autor");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes attributes){
        try {
            servicio.crearAutor(nombre);
            attributes.addFlashAttribute("exito", "Se ha agregado el autor");
        } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado",ex.getMessage());
            return new RedirectView("/autores/crear");
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado",ex.getMessage());
            return new RedirectView("/autores/crear");
        }
        return new RedirectView("/autores");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam String nombre, RedirectAttributes attributes){
        try {
            servicio.modificarAutor(id, nombre);
            attributes.addFlashAttribute("exito","Se ha modificado el autor exitosamente");
        } catch (NullObjectException ex) {
            attributes.addFlashAttribute("error",ex.getMessage());
            return new RedirectView("/autores");
        } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado",ex.getMessage());
            return new RedirectView("/autores/editar/"+id);
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado",ex.getMessage());
            return new RedirectView("/autores/editar/"+id);
        }
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
