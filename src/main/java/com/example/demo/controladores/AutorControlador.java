package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.ServicioAutor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Autor")
public class AutorControlador {

    @Autowired
    private ServicioAutor servicioAutor;

    @PostMapping("/registrarAutorEfectivo") //modelmap es la que nos permite usar thymeleaf en los html
    public String registrarAutorEfectivo(ModelMap modelo, @RequestParam String nombre) throws ErrorServicio {

        try {
            servicioAutor.crearAutor(nombre);
            modelo.put("descripcion", "Autor " + nombre + " registrado correctamente a la base de datos");
            return "registracionEfectiva.html";

        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("error", e.getMessage()); //aparece el error en la interfaz si se da alguno
            modelo.put("nombre", nombre); //vuelve a aparecer la variable en el formulario para usarla nuevamennte, sino se desaparecen
            return "registrarAutor.html";
        }

    }

    //ERROR AL BUSCAR UN NOMBRE QUE NO ES.
    @GetMapping("/buscarAutorPorNombre")
    public String buscarAutorPorNombre(ModelMap modelo, @RequestParam(required = false) String nombreBusquedaNombre) throws ErrorServicio {
        try {
            Autor autor = servicioAutor.buscarAutorPorNombre(nombreBusquedaNombre);
            modelo.put("descripcion", "Autor " + autor.getNombre() + " encontrado en la base de datos \n" + autor.toString());
            return "autorBusqueda.html";
        } catch (ErrorServicio error) {
            error.printStackTrace();
            modelo.put("errorBusquedaPorNombre", error.getMessage());
            modelo.put("nombreBusquedaNombre", nombreBusquedaNombre);
            return "registrarAutor.html";
        }

    }

    @GetMapping("/buscarAutorPorId")
    public String buscarAutorPorId(ModelMap modelo, @RequestParam(required = false) Integer id) throws ErrorServicio {
        try {
            Autor autor = servicioAutor.buscarAutorPorId(id);
            modelo.put("descripcion", "Autor " + autor.getNombre() + " encontrado en la base de datos, id " + autor.getId() + " \n" + autor.toString());
            return "autorBusqueda.html";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("errorBusquedaId", e.getMessage());
            modelo.put("id", id);
            return "registrarAutor.html";
        }

    }

    @PostMapping("/borrarAutorPorId")
    public String borrarAutorPorId(ModelMap modelo, @RequestParam(required = false) Integer idBorrado) throws ErrorServicio {
        try {
            Autor autor = servicioAutor.buscarAutorPorId(idBorrado);
            servicioAutor.borrarAutorPorID(idBorrado);
            modelo.put("descripcion", "Autor " + autor.getNombre() + " con id " + autor.getId() + " BORRADO DE LA BASE DE DATOS\n");
            return "autorBusqueda.html";
        } catch (ErrorServicio e) {
            e.printStackTrace();
            modelo.put("errorBusquedaBorrar", e.getMessage());
            modelo.put("idBorrado", idBorrado);
            return "registrarAutor.html";
        }

    }

    @GetMapping("/ListarAutores")
    public String ListarAutores(ModelMap modelo) throws ErrorServicio {
        List<Autor> autores = servicioAutor.mostrarAutores();
        modelo.put("autores", autores);
        return "listarAutores.html";
    }

    
    @PostMapping("/deshabilitarAutor")
    public String darBaja(ModelMap modelo, @RequestParam Integer id) throws ErrorServicio {
        try {
            servicioAutor.desabilitarAutor(id);
            return "listarAutores.html";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "listarAutores.html";
        }
    }
    
    @PostMapping("/habilitarAutor")
    public String darAlta (ModelMap modelo, @RequestParam Integer id) throws ErrorServicio {
        try {
            servicioAutor.habilitarAutor(id);
            return "listarAutores.html";
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());
            return "listarAutores.html";
        }
    }

}
