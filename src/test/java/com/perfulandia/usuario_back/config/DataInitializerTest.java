package com.perfulandia.usuario_back.config;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    private RolRepository rolRepository;
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        rolRepository = mock(RolRepository.class);
        dataInitializer = new DataInitializer(rolRepository);
    }

    @Test
    void run_deberiaCrearRolesSiNoExisten() throws Exception {
        // Simula que ningún rol existe
        when(rolRepository.findByNombre(anyString())).thenReturn(Optional.empty());

        // Ejecuta
        dataInitializer.run();

        // Verifica que se intentó guardar 4 roles
        verify(rolRepository, times(4)).save(any(Rol.class));
    }

    @Test
    void run_noDeberiaCrearRolesSiYaExisten() throws Exception {
        // Simula que todos los roles ya existen
        when(rolRepository.findByNombre(anyString())).thenReturn(Optional.of(new Rol()));

        // Ejecuta
        dataInitializer.run();

        // Verifica que no se intentó guardar ningún rol
        verify(rolRepository, never()).save(any(Rol.class));
    }
}