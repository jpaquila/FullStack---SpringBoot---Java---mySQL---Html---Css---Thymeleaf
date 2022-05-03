
package com.example.demo.controladores;

import com.example.demo.entidades.Editorial;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.ServicioEditorial;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Editorial") 
public class EditorialControlador {
     @Autowired
    private ServicioEditorial servicioEditorial;
    
     
   @PostMapping("/registrarEditorialEfectivo")
    public String registrarEditorial(ModelMap modelo, @RequestParam String nombre) throws ErrorServicio {
        try {
            servicioEditorial.crearEditorial(nombre);
            modelo.put("descripcion", "Editorial " + nombre + " registrada correctamente en la base de datos");
        return "registracionEfectiva.html";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            return "registrarEditorial.html";
        }
        
    }
    
       @GetMapping("/ListarEditoriales")
    public String ListarAutores(ModelMap modelo) throws ErrorServicio {
        List<Editorial> editoriales = servicioEditorial.mostrarEditoriales();
        modelo.put("editoriales", editoriales);
        return "listarEditoriales.html";
    }
   
}
