package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.ServicioAutor;
import com.example.demo.servicios.ServicioEditorial;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/") //INDICA LA RUTA INICIAL de toda la clase. Cada Mapping "/.." que encontremos, comenzará a partir de esta.
public class PortalControlador {

    @Autowired
    private ServicioEditorial servicioEditorial;
    @Autowired
    private ServicioAutor servicioAutor;


    @GetMapping("") //Al no tener nada, sería el mismo mapping de arriba, es decir, el "/"
    public String index() {
        return "index.html";
    }

    @RequestMapping("/registrarAutor")
    public String registrarAutor() {
        return "registrarAutor.html";
    }

    @RequestMapping("/registrarEditorial")
    public String registrarEditorial() {
        return "registrarEditorial.html";
    }

    //LIBRO
    @RequestMapping("/registrarLibro")
    public String registrarLibro(ModelMap modelo) throws ErrorServicio {
        List<Autor> autores = servicioAutor.mostrarAutores(); //sacamos la lista de los autores de la base de datos a través del repositorio
        modelo.put("autores", autores); //mandamos una variable llamada "autores" con el array dentro al fh:each del html
        List<Editorial> editoriales = servicioEditorial.mostrarEditoriales(); 
        modelo.put("editoriales", editoriales);
        return "registrarLibro.html";
    }

   

}
