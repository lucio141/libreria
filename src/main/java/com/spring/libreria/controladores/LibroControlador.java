package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.entidades.Libro;
import com.spring.libreria.exceptions.AutorRemovedException;
import com.spring.libreria.exceptions.DeletedObjectException;
import com.spring.libreria.exceptions.EditorialRemovedException;
import com.spring.libreria.exceptions.NullObjectException;
import com.spring.libreria.exceptions.RepeatedObjectException;
import com.spring.libreria.servicios.AutorService;
import com.spring.libreria.servicios.EditorialService;
import com.spring.libreria.servicios.LibroService;
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
@RequestMapping("/libros")
public class LibroControlador {

    @Autowired
    private LibroService servicio;

    @Autowired
    private AutorService autorServicio;

    @Autowired
    private EditorialService editorialServicio;

    @GetMapping()
    public ModelAndView mostrarLibros(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("libros-lista");
        List<Libro> libros = servicio.obtenerLibro();
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("exito", map.get("exito"));
            mav.addObject("error", map.get("error"));
            mav.addObject("error-removed", map.get("error-removed"));
        }

        mav.addObject("libros", libros);
        mav.addObject("title", "Tabla de libros");

        return mav;
    }

    @GetMapping("/eliminados")
    public ModelAndView mostrarLibrosEliminados(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("libros-lista-eliminados");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("exito", map.get("exito"));
            mav.addObject("error", map.get("error"));
            mav.addObject("autorRemoved", map.get("autorRemoved"));
            mav.addObject("editorialRemoved", map.get("editorialRemoved"));
        }

        List<Libro> libros = servicio.obtenerLibroEliminado();
        mav.addObject("libros", libros);
        mav.addObject("title", "Tabla de libros eliminados");

        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearLibro(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/libros-formulario");

        List<Autor> autores = autorServicio.obtenerAutor();
        List<Editorial> editoriales = editorialServicio.obtenerEditorial();

        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));
        }

        mav.addObject("autores", autores);
        mav.addObject("editoriales", editoriales);
        mav.addObject("libro", new Libro());
        mav.addObject("title", "Crear Libro");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarLibro(@PathVariable int id, HttpServletRequest request, RedirectAttributes attributes) {
        ModelAndView mav = new ModelAndView("/libros-formulario");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if (map != null) {
            mav.addObject("duplicado", map.get("duplicado"));
            mav.addObject("eliminado", map.get("eliminado"));
        }

        List<Autor> autores = autorServicio.obtenerAutor();
        List<Editorial> editoriales = editorialServicio.obtenerEditorial();

        mav.addObject("autores", autores);
        mav.addObject("editoriales", editoriales);

        try {
            mav.addObject("libro", servicio.obtenerPorId(id));
        } catch (NullObjectException ex) {
            attributes.addFlashAttribute("error", ex.getMessage());
            mav.setViewName("redirect:/libros");
        }

        mav.addObject("title", "editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam long isbn, @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares, @RequestParam("autor") int autorId, @RequestParam("editorial") int editorialId, RedirectAttributes attributes) {

        try {
            servicio.crearLibro(isbn, titulo, anio, ejemplares, autorId, editorialId);
            attributes.addFlashAttribute("exito", "El libro se ha creado con Exito");
        } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado", ex.getMessage());
            return new RedirectView("/libros/crear");
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado", ex.getMessage());
            return new RedirectView("/libros/crear");
        }

        return new RedirectView("/libros");
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam long isbn, @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares, @RequestParam("autor") int autorId, @RequestParam("editorial") int editorialId, RedirectAttributes attributes) {
       
        try {
            servicio.modificarLibro(id, isbn, titulo, anio, ejemplares, autorId, editorialId);
        } catch (NullObjectException ex) {
           attributes.addFlashAttribute("error",ex.getMessage());
            return new RedirectView("/libros");
       } catch (RepeatedObjectException ex) {
            attributes.addFlashAttribute("duplicado",ex.getMessage());
            return new RedirectView("/libros/editar/"+id);
        } catch (DeletedObjectException ex) {
            attributes.addFlashAttribute("eliminado",ex.getMessage());
            return new RedirectView("/libros/editar/"+id);
        }
        
        return new RedirectView("/libros");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id) {
        servicio.delete(id);
        return new RedirectView("/libros");
    }

    @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id, RedirectAttributes attributes) {
        try {
            servicio.recuperar(id);
             attributes.addFlashAttribute("exito","Se ha recuperado correctamente");
        } catch (NullObjectException ex) {
           attributes.addFlashAttribute("error", ex.getMessage());
           return new RedirectView("/libros/eliminados");
                   } catch (AutorRemovedException ex) {
                       attributes.addFlashAttribute("autorRemoved", ex.getMessage());
           return new RedirectView("/libros/eliminados");
        } catch (EditorialRemovedException ex) {
                       attributes.addFlashAttribute("editorialRemoved", ex.getMessage());
           return new RedirectView("/libros/eliminados");
        }
        return new RedirectView("/libros/eliminados");
    }

}
