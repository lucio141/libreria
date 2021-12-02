package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.servicios.EditorialService;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialService servicio;

    @GetMapping()
    public ModelAndView mostrarEditoriales(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("editoriales-lista");

        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("exito", map.get("exito"));
            mav.addObject("error", map.get("error"));
            mav.addObject("error-removed", map.get("error-removed"));
        }

        List<Editorial> editoriales = servicio.obtenerEditorial();
        mav.addObject("editoriales", editoriales);
        mav.addObject("title", "Tabla de editoriales");

        return mav;
    }

    @GetMapping("/eliminados")
    public ModelAndView mostrarEditorialesEliminadas() {
        ModelAndView mav = new ModelAndView("editoriales-lista-eliminados");
        List<Editorial> editoriales = servicio.obtenerEditorialEliminada();
        mav.addObject("editoriales", editoriales);
        mav.addObject("title", "Tabla de editoriales eliminadas");

        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearEditorial(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("editoriales-formulario");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));
        }

        mav.addObject("editorial", new Editorial());
        mav.addObject("title", "Crear Editorial");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarEditorial(@PathVariable int id, RedirectAttributes attributes, HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("editoriales-formulario");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));
        }
        try {
            mav.addObject("editorial", servicio.obtenerPorId(id));
        } catch (NullObjectException ex) {
            attributes.addFlashAttribute("error", ex.getMessage());
            mav.setViewName("redirect:/editoriales");
        }
        
        mav.addObject("title", "Editar Editorial");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam String nombre, RedirectAttributes attributes) {
        try {
            servicio.crearEditorial(nombre);
         } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado",ex.getMessage());
            return new RedirectView("/editoriales/crear");
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado",ex.getMessage());
            return new RedirectView("/editoriales/crear");
        }
        return new RedirectView("/editoriales");
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam String nombre, RedirectAttributes attributes) {
        try {
            servicio.modificarEditorial(id, nombre);
        } catch (NullObjectException ex) {
            attributes.addFlashAttribute("error",ex.getMessage());
            return new RedirectView("/editoriales");
        } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado",ex.getMessage());
            return new RedirectView("/editoriales/editar/"+id);
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado",ex.getMessage());
            return new RedirectView("/editoriales/editar/"+id);
        }

        return new RedirectView("/editoriales");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id) {
        servicio.delete(id);
        return new RedirectView("/editoriales");
    }

    @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id) {
        servicio.recuperar(id);
        return new RedirectView("/editoriales/eliminados");
    }

}
