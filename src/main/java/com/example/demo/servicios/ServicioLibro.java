package com.example.demo.servicios;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.AutorRepositorio;
import com.example.demo.repositorios.EditorialRepositorio;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioLibro {

    Scanner leer = new Scanner(System.in).useDelimiter("\n");

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    public void validar(String titulo, Long isbn, Integer anio, Integer ejemplares, Integer idAutor, Integer idEditorial) throws ErrorServicio {
        if (isbn == null || isbn == 0) {
            throw new ErrorServicio("El isbn no puede estar vacio o valer 0");
        }
        
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El titulo no puede ser nulo");
        }
        if (anio == null || anio < 1500) {
            throw new ErrorServicio("El año no puede estar vacio o ser menor a 1500");
        }
        if (ejemplares == null || ejemplares <= 0) {
            throw new ErrorServicio("Ejemplares no puede estar vacio y debe ser mayor de cero");
        }
        validarEditorial(idEditorial);
        validarAutor(idAutor);
    }

    public void validarEditorial(Integer idEditorial) throws ErrorServicio{
        if (idEditorial == null) {
            throw new ErrorServicio("La editorial no puede estar vacia");
        }
        Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
        if (!editorial.isPresent()) {
            throw new ErrorServicio("No se encontró Editorial con ese ID");
        }
    }
    
     public void validarAutor(Integer idAutor) throws ErrorServicio{
         if (idAutor == null) {
            throw new ErrorServicio("El autor no puede estar vacio");
        }
        Optional<Autor> autor = autorRepositorio.findById(idAutor);
        if (!autor.isPresent()) {
            throw new ErrorServicio("No se encontró Autor con ese ID");
        }
    }
    
    @Transactional(propagation = Propagation.NESTED)
    public void registrarLibro(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer idAutor, Integer idEditorial) throws ErrorServicio {
        
        Libro libro = new Libro(); 
       
       validar(titulo, isbn, anio, ejemplares, idAutor, idEditorial);

        Optional<Autor> optionalAutor = autorRepositorio.findById(idAutor);
        Autor autor = optionalAutor.get();
      
        Optional<Editorial> optionalEditorial = editorialRepositorio.findById(idEditorial);
        Editorial editorial = optionalEditorial.get();
       
        libro.setEditorial(editorial);
        libro.setAutor(autor);
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setEjemplaresPrestados(0);
        libroRepositorio.save(libro);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void modificarLibro(Integer id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer idAutor, Integer idEditorial) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            registrarLibro(isbn, titulo, anio, ejemplares, idAutor, idEditorial);
        } else {
            throw new ErrorServicio("No existe libro con ese ID");
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void deshabilitarLibro(Integer id, Long isbn, String titulo, Integer anio, Integer ejemplares) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro l = respuesta.get();
            if (l.getAlta() == true) {
                l.setAlta(false);
                System.out.println("Libro dado de baja correctamente");
                libroRepositorio.save(l);
            } else {
                throw new ErrorServicio("ese libro ya está dado de baja");
            }

        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void eliminarLibro(Integer id) {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        libroRepositorio.delete(respuesta.get());
    }
    
    @Transactional(readOnly = true) 
    public List<Libro> mostrarLibros(){
        List<Libro> libros =  libroRepositorio.findAll();
        Collections.sort(libros, new Comparator<Libro>() { //ORDENADO DE DATOS
            @Override
            public int compare(Libro o1, Libro o2) {
                return o1.getTitulo().compareToIgnoreCase(o2.getTitulo());
            }
        });
        return libros;
    }
}
