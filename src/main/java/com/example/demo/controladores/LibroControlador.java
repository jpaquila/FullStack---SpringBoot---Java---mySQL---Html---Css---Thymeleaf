package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.ServicioAutor;
import com.example.demo.servicios.ServicioEditorial;
import com.example.demo.servicios.ServicioLibro;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Libro")
public class LibroControlador {

    @Autowired
    private ServicioLibro servicioLibro;
    @Autowired
    private ServicioAutor servicioAutor;
    @Autowired
    private ServicioEditorial servicioEditorial;

    @RequestMapping("/registrarLibroEfectivo") //Aca la ruta seria /Libro/RegistrarLibroEfectivo
    public String registrarLibro(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer idAutor, @RequestParam Integer idEditorial) throws ErrorServicio {
        try {
            servicioLibro.registrarLibro(isbn, titulo, anio, ejemplares, idAutor, idEditorial);
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage()); //pone en el th:value ${error} la excepcion que atrapo el catch
            modelo.put("titulo", titulo); //pone en el th:value ${titulo} la variable titulo, para que no se vacie el campo si da un error
            modelo.put("isbn", isbn);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            List<Autor> autores = servicioAutor.mostrarAutores();
            modelo.put("autores", autores);
            List<Editorial> editoriales = servicioEditorial.mostrarEditoriales();
            modelo.put("editoriales", editoriales);
            return "registrarLibro.html";
        }
        modelo.put("descripcion", "Libro " + titulo + " registrado correctamente en la base de datos");
        return "registracionEfectiva.html";
    }

    @GetMapping("/listarLibros")
    public String listarLibros(ModelMap modelo) throws ErrorServicio{
            List <Libro> libros = servicioLibro.mostrarLibros();
            modelo.put("libros", libros);
            return "listarLibros.html";

    }
}
