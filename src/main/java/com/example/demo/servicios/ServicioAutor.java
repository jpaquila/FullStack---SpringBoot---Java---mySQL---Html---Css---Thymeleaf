package com.example.demo.servicios;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.AutorRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioAutor {
    


    @Autowired
    private AutorRepositorio autorRepositorio; //creamos una varaiable repositorio con autorwire
    
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
            if (nombre.contains(""+i+"")) {
                throw new ErrorServicio("El nombre no puede tener números");
            }
        }
        
    }
    
     public void autorActivo(String nombre) throws ErrorServicio{
        List<Autor> autores = autorRepositorio.findAll();
        for (Autor i : autores) {
            if (i.getNombre() == nombre) {
                throw new ErrorServicio("Este Autor ya está registrado"); 
            } 
        }
    }
    public void libroActivo(Integer id) throws ErrorServicio{
        List<Libro> libros = libroRepositorio.findAll();
        for (Libro i : libros) {
            if (i.getAutor().getId() == id) {
                throw new ErrorServicio("Este Autor ya tiene un libro activo. "
                        + "Borre primero el/los libros asociados y reintente nuevamente");  
            } 
        }
    }
    public void validarId(Integer id) throws ErrorServicio {
        if (id == null || id == 0) {
            throw new ErrorServicio("El id no puede estar vacío o ser Cero");
        }  
    }
    
    @Transactional
    public void crearAutor(String nombre) throws ErrorServicio {
        validarNombre(nombre);  //valida que el nombre no tenga caracteres incorrectos
        autorActivo(nombre); //valida que no exista otro autor con el mismo nombre
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepositorio.save(autor);  //se guarda en la base de datos
    }
    
    @Transactional(readOnly = true)
    public Autor buscarAutorPorId(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No existe Autor con ese ID");
        }
    }

    //ANDA MAL el else
    @Transactional(propagation = Propagation.NESTED) //CONSULTA PERSONALIZADA DE BUSCAR POR NOMBRE
    public Autor buscarAutorPorNombre(String nombreBusquedaNombre) throws ErrorServicio {
        validarNombre(nombreBusquedaNombre); //me aseguro que no venga el parámetro vacio
        Autor autor = autorRepositorio.buscarPorNombre(nombreBusquedaNombre);
        if (autor.getNombre().toUpperCase().equals(nombreBusquedaNombre.toUpperCase())) {
            return autor;
        } else {
            throw new ErrorServicio("No existe Autor con ese nombre");            
        }
    }

//    @Transactional(propagation = Propagation.NESTED)
//    public void borrarAutorPorNombre(String nombre) throws ErrorServicio {
//        Autor autor = autorRepositorio.buscarPorNombre(nombre);
//        if (autor.getNombre().toUpperCase().equals(nombre.toUpperCase())) {
//            autorRepositorio.delete(autor);
//        } else {
//               throw new ErrorServicio("No existe Autor con ese nombre");  
//        }
//    }
    
    
    @Transactional(propagation = Propagation.NESTED)
    public void borrarAutorPorID(Integer id) throws ErrorServicio {
        validarId(id);
        libroActivo(id);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            autorRepositorio.deleteById(id);
        } else {
            throw new ErrorServicio("No existe autor con ese ID para borrar");
        }
    }
    

    @Transactional(propagation = Propagation.NESTED)
    public void desabilitarAutor(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);
            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró autor con ese ID");
        }
    }
    
    @Transactional(propagation = Propagation.NESTED)
    public void habilitarAutor(Integer id) throws ErrorServicio {
        validarId(id);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            if (autor.getAlta() == false) {
                autor.setAlta(true);
                autorRepositorio.save(autor);
            } else {
                throw new ErrorServicio("Ese autor ya está dado de alta");
            }
        } else {
            throw new ErrorServicio("No se encontró autor con ese ID");
        }
    }
    
    @Transactional(readOnly = true)
    public List<Autor> mostrarAutores() throws ErrorServicio {
        List<Autor> autores = autorRepositorio.findAll();
        Collections.sort(autores, new Comparator<Autor>() { //ORDENADO DE DATOS
            @Override
            public int compare(Autor o1, Autor o2) {
                return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });
        return autores;
    }
    
}
