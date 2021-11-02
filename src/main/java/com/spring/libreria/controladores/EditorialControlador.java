package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.servicios.EditorialService;
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
@RequestMapping("/editoriales")
public class EditorialControlador {
    
    @Autowired
    private EditorialService servicio;
    
    @GetMapping()
    public ModelAndView mostrarEditoriales(){
        ModelAndView mav = new ModelAndView("editoriales-lista");
        List<Editorial> editoriales = servicio.obtenerEditorial();
        mav.addObject("editoriales", editoriales);
        mav.addObject("title", "Tabla de editoriales");
        
        return mav;
    }
    
    @GetMapping("/eliminados")
    public ModelAndView mostrarEditorialesEliminadas(){
        ModelAndView mav = new ModelAndView("editoriales-lista-eliminados");
        List<Editorial> editoriales = servicio.obtenerEditorialEliminada();
        mav.addObject("editoriales", editoriales);
        mav.addObject("title", "Tabla de editoriales eliminadas");
        
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crearEditorial(){
        ModelAndView mav = new ModelAndView("editoriales-formulario");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editarEditorial(@PathVariable int id){
        ModelAndView mav = new ModelAndView("editoriales-formulario");
        mav.addObject("editorial", servicio.obtenerPorId(id));
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre){
        servicio.crearEditorial(nombre);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam String nombre){
        servicio.modificarEditorial(id,nombre);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id){
        servicio.delete(id);
        return new RedirectView("/editoriales");
    }
    
    @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id){
        servicio.recuperar(id);
        return new RedirectView("/editoriales/eliminados");
    }
    
}
