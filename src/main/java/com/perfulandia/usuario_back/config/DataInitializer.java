package com.perfulandia.usuario_back.config;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Clase que se ejecuta automáticamente al iniciar la aplicación.
 * Su propósito es inicializar la base de datos con roles predeterminados
 * si estos no existen aún
 *
 * <p>Roles creados automáticamente:</p>
 * <ul>
 *     <li>ROLE_ADMIN</li>
 *     <li>ROLE_USER</li>
 *     <li>ROLE_CLIENTE</li>
 *     <li>ROLE_EMPLEADO</li>
 * </ul>
 *
 * Esta clase implementa {@link CommandLineRunner} para ejecutar código
 * justo después de que el contexto de Spring Boot haya sido cargado.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /**
     * Repositorio para acceder y manipular los roles en la base de datos.
     */
    private final RolRepository rolRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param rolRepository el repositorio de roles
     */
    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Método ejecutado automáticamente al iniciar la aplicación.
     * Crea los roles predeterminados si no existen en la base de datos.
     *
     * @param args argumentos de línea de comandos
     * @throws Exception si ocurre un error durante la ejecución
     */
    @Override
    public void run(String... args) throws Exception {
        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_USER");
        crearRolSiNoExiste("ROLE_CLIENTE");
        crearRolSiNoExiste("ROLE_EMPLEADO");
    }

    /**
     * Verifica si un rol existe en la base de datos; si no, lo crea.
     *
     * @param nombreRol el nombre del rol a verificar o crear
     */
    private void crearRolSiNoExiste(String nombreRol) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            rolRepository.save(new Rol(null, nombreRol));
            System.out.println("Rol creado: " + nombreRol);
        }
    }
}
