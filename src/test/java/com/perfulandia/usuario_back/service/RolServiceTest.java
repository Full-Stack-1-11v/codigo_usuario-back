package com.perfulandia.usuario_back.service;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        Rol rol1 = new Rol(1L, "ADMIN");
        Rol rol2 = new Rol(2L, "USER");
        when(rolRepository.findAll()).thenReturn(Arrays.asList(rol1, rol2));

        List<Rol> resultado = rolService.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(rolRepository).findAll();
    }

    @Test
    void testObtenerPorIdExistente() {
        Rol rol = new Rol(1L, "ADMIN");
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Optional<Rol> resultado = rolService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());
    }

    @Test
    void testObtenerPorIdNoExistente() {
        when(rolRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Rol> resultado = rolService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGuardarRol() {
        Rol rol = new Rol(null, "NEW_ROLE");
        Rol rolGuardado = new Rol(1L, "NEW_ROLE");
        when(rolRepository.save(rol)).thenReturn(rolGuardado);

        Rol resultado = rolService.guardar(rol);

        assertEquals(1L, resultado.getId());
        assertEquals("NEW_ROLE", resultado.getNombre());
    }

    @Test
    void testEliminarRol() {
        Long id = 10L;

        rolService.eliminar(id);

        verify(rolRepository).deleteById(id);
    }
}
