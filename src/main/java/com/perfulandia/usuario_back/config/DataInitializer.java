package com.perfulandia.usuario_back.config;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_USER");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            rolRepository.save(new Rol(null, nombreRol));
            System.out.println("Rol creado: " + nombreRol);
        }
    }
}
