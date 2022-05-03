
package com.example.demo.repositorios;

import com.example.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Integer>{
   
    /*@Query("select l.titulo, l.isbn, l.anio, l.ejemmplares, a.nombre, e.editorial from Libro l, Autor a, Editorial e")
    public List <Libro> listarLibros();*/
 
}
