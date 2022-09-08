package com.example.Reto5;

import com.example.Reto5.controlador.ControladorProducto;
import com.example.Reto5.modelo.RepositorioProducto;
import com.example.Reto5.vista.actualizar;
import com.example.Reto5.vista.inventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Reto5Application {

    @Autowired
    RepositorioProducto repositorio;
    
    public static void main(String[] args) {
	new SpringApplicationBuilder(Reto5Application.class).headless(false).run(args);
    }

    @Bean
    void applicationRunner(){
        ControladorProducto p = new ControladorProducto(repositorio, new inventario(), new actualizar());        
    }    
}
