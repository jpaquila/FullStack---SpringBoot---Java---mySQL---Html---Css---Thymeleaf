package com.example.demo.servicios;

import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.EditorialRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioEditorial {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");

    @Autowired
    private EditorialRepositorio editorialRepositorio; //creamos una varaiable repositorio con autorwire
    @Autowired
    private LibroRepositorio libroRepositorio;

    public void validarNombre(String nombre) throws ErrorServicio { //metodo que vincula a clase ErrorServicio para devlver un error personalizado
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacío"); //llamo al metodo de error que cree que hereda de excepcion en lugar de llamar a excepcion de una
        }
        /*if (nombre.contains(" ")) {
            throw new ErrorServicio("El nombre no puede tener espacios en blanco");
        }*/
        if (nombre.contains("!") || nombre.contains("$") || nombre.contains("#") || nombre.contains(".")) {
            throw new ErrorServicio("El nombre no puede tener caracteres especiales");
        }
        for (int i = 0; i < 10; i++) {
            if (nombre.contains("" + i + "")) {
                throw new ErrorServicio("El nombre no puede tener números");
            }
        }
    }

    public void libroActivo(Integer id) throws ErrorServicio {
        List<Libro> libros = libroRepositorio.findAll();
        for (Libro i : libros) {
            if (i.getAutor().getId() == id) {
                throw new ErrorServicio("Esta Editorial ya tiene un libro activo asociado. "
                        + "Borre primero el/los libros asociados y reintente nuevamente");
            }
        }
    }

     public void editorialActiva(String nombre) throws ErrorServicio{
        List<Editorial> editoriales = editorialRepositorio.findAll();
        for (Editorial i : editoriales) {
            if (i.getNombre() == nombre) {
                throw new ErrorServicio("Esta Editorial ya está registrada"); 
            } 
        }
    }
    public void validarId(Integer id) throws ErrorServicio {
        if (id == null || id == 0) {
            throw new ErrorServicio("El id no puede estar vacío o ser Cero");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void crearEditorial(String nombre) throws ErrorServicio {
        validarNombre(nombre);
        editorialActiva(nombre);
        Editorial e = new Editorial();
        e.setNombre(nombre);
        editorialRepositorio.save(e);  //se guarda en la base de datos
    }

    @Transactional(readOnly = true)
    public Editorial buscarEditorial(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            return e;
        } else {
            throw new ErrorServicio("No existe Editorial con esa ID");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void desabilitarEditorial(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id); //el metodo findbyId devuelve una clase optional como rta
        if (respuesta.isPresent()) { //si el autor esta presente con ese id
            Editorial e = respuesta.get(); //traemos con el get, de la clase optinoal, los datos al nuevo objeto autor
            e.setAlta(false);
            editorialRepositorio.save(e); //seteamos nuevo nommbre en la base de datos
        } else {
            throw new ErrorServicio("No se encontró editorial con ese ID");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void habilitarEditorial(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial e = respuesta.get();
            if (e.getAlta() == false) {
                e.setAlta(true);
                editorialRepositorio.save(e);
            } else {
                throw new ErrorServicio("Esa editorial ya está dada de alta");
            }
        } else {
            throw new ErrorServicio("No se encontró editorial con ese ID");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void borrarEditorial(Integer id) throws ErrorServicio {
        validarId(id);
        libroActivo(id);
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorialRepositorio.delete(editorial);
        } else {
            throw new ErrorServicio("No hay editorial para borrar con ese ID");
        }

    }

    @Transactional(readOnly = true)
    public List<Editorial> mostrarEditoriales() {
        List<Editorial> editoriales = editorialRepositorio.findAll();
        Collections.sort(editoriales, new Comparator<Editorial>() { //ORDENADO DE DATOS
            @Override
            public int compare(Editorial o1, Editorial o2) {
                return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });
        return editoriales;
    }

}
