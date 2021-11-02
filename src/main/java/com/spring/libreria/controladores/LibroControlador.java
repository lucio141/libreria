package com.spring.libreria.controladores;

import com.spring.libreria.entidades.Autor;
import com.spring.libreria.entidades.Editorial;
import com.spring.libreria.entidades.Libro;
import com.spring.libreria.servicios.AutorService;
import com.spring.libreria.servicios.EditorialService;
import com.spring.libreria.servicios.LibroService;
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
@RequestMapping("/libros")
public class LibroControlador {

    @Autowired
    private LibroService servicio;

    @Autowired
    private AutorService autorServicio;

    @Autowired
    private EditorialService editorialServicio;

    @GetMapping()
    public ModelAndView mostrarLibros() {
        ModelAndView mav = new ModelAndView("libros-lista");
        List<Libro> libros = servicio.obtenerLibro();
        mav.addObject("libros", libros);
        mav.addObject("title", "Tabla de libros");

        return mav;
    }

    @GetMapping("/eliminados")
    public ModelAndView mostrarLibrosEliminados() {
        ModelAndView mav = new ModelAndView("libros-lista-eliminados");
        List<Libro> libros = servicio.obtenerLibroEliminado();
        mav.addObject("libros", libros);
        mav.addObject("title", "Tabla de libros eliminados");

        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearLibro() {
        ModelAndView mav = new ModelAndView("/libros-formulario");

        List<Autor> autores = autorServicio.obtenerAutor();
        List<Editorial> editoriales = editorialServicio.obtenerEditorial();

        mav.addObject("autores", autores);
        mav.addObject("editoriales", editoriales);
        mav.addObject("libro", new Libro());
        mav.addObject("title", "Crear Libro");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarLibro(@PathVariable int id) {
        ModelAndView mav = new ModelAndView("/libros-formulario");

        List<Autor> autores = autorServicio.obtenerAutor();
        List<Editorial> editoriales = editorialServicio.obtenerEditorial();

        mav.addObject("autores", autores);
        mav.addObject("editoriales", editoriales);
        mav.addObject("libro", servicio.obtenerPorId(id));
        mav.addObject("title", "editar Libro");
        mav.addObject("action", "modificar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam long isbn, @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares, @RequestParam("autor") int autorId, @RequestParam("editorial") int editorialId) {
        servicio.crearLibro(isbn, titulo, anio, ejemplares, autorId, editorialId);
        return new RedirectView("/libros");
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam int id, @RequestParam long isbn, @RequestParam String titulo, @RequestParam int anio, @RequestParam int ejemplares, @RequestParam("autor") int autorId, @RequestParam("editorial") int editorialId) {
        servicio.modificarLibro(id, isbn, titulo, anio, ejemplares, autorId, editorialId);
        return new RedirectView("/libros");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView delete(@PathVariable int id) {
        servicio.delete(id);
        return new RedirectView("/libros");
    }
    
        @PostMapping("/recuperar/{id}")
    public RedirectView recuperar(@PathVariable int id) {
        servicio.recuperar(id);
        return new RedirectView("/libros/eliminados");
    }

}
