package com.perfulandia.usuario_back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de seguridad para el microservicio de usuarios.
 * Define los beans necesarios para la autenticación y autorización, así como la configuración de filtros de seguridad.
 */
@Configuration
public class SecurityConfig {

    /**
     * Bean que proporciona el codificador de contraseñas utilizando el algoritmo BCrypt.
     *
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad para las solicitudes HTTP.
     * Actualmente, permite todas las solicitudes (modo abierto para pruebas).
     *
     * @param http configuración de seguridad HTTP
     * @return la cadena de filtros configurada
     * @throws Exception si ocurre algún error al construir la cadena de filtros
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Desactiva CSRF y permite todas las solicitudes (sin seguridad)
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Bean que proporciona el administrador de autenticación a partir de la configuración proporcionada por Spring.
     *
     * @param config configuración de autenticación de Spring
     * @return una instancia de {@link AuthenticationManager}
     * @throws Exception si no se puede crear el administrador de autenticación
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
