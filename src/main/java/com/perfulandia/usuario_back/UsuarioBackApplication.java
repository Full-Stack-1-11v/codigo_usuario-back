package com.perfulandia.usuario_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Clase principal del microservicio de usuarios para Perfulandia.
 * <p>
 * Esta clase inicia la aplicación Spring Boot y habilita el cliente Feign para la comunicación
 * entre microservicios.
 * </p>
 */
@SpringBootApplication
@EnableFeignClients
public class UsuarioBackApplication {

    /**
     * Método principal que arranca la aplicación Spring Boot.
     * @param args argumentos pasados por línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(UsuarioBackApplication.class, args);
    }
}
