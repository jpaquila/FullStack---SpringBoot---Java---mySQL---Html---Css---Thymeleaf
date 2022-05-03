
package com.example.demo.repositorios;

import com.example.demo.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Integer>{
    
    @Query("select a from Autor a where a.nombre = :PARAMETRO")
    public Autor buscarPorNombre(@Param("PARAMETRO")String nombre);
    
    @Query("select a from Autor a where a.id = :PARAMETRO")
    public Autor buscarPorID(@Param("PARAMETRO") Integer id);
}
