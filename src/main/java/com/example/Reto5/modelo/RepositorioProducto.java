package com.example.Reto5.modelo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioProducto extends CrudRepository<Producto,Integer>{
    
}
