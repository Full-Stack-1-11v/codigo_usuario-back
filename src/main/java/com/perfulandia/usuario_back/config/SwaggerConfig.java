package com.perfulandia.usuario_back.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para la generación automática de la documentación de la API
 * utilizando Swagger (OpenAPI 3).
 *
 * <p>Esta clase define un bean {@link OpenAPI} que se utiliza para personalizar
 * la información general mostrada en la interfaz Swagger UI.</p>
 */
@Configuration
public class SwaggerConfig {

    /**
     * Crea y configura una instancia personalizada de {@link OpenAPI} con información
     * general como el título, la versión y la descripción de la API REST.
     *
     * @return un objeto {@link OpenAPI} con los metadatos de la documentación
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("API de Usuarios - Perfulandia")
                    .version("1.0")
                    .description("Documentación de la API REST para el microservicio de usuarios"));
    }
}
