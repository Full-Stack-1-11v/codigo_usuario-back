package com.perfulandia.usuario_back.model;

import org.junit.jupiter.api.Test;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testGettersSettersEqualsHashCodeToStringBuilder() {
        Usuario usuario1 = Usuario.builder()
                .id(1L)
                .nombre("Maykol")
                .apellido("Vargas")
                .rut("12.345.678-9")
                .correo("maykol@mail.com")
                .direccion("Calle Falsa 123")
                .password("clave123")
                .activo(true)
                .roles(new HashSet<>())
                .rawPassword("temporal123")
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(1L)
                .nombre("Maykol")
                .apellido("Vargas")
                .rut("12.345.678-9")
                .correo("maykol@mail.com")
                .direccion("Calle Falsa 123")
                .password("clave123")
                .activo(true)
                .roles(new HashSet<>())
                .rawPassword("temporal123")
                .build();

        // Verifica getters
        assertEquals(1L, usuario1.getId());
        assertEquals("Maykol", usuario1.getNombre());
        assertEquals("Vargas", usuario1.getApellido());
        assertEquals("12.345.678-9", usuario1.getRut());
        assertEquals("maykol@mail.com", usuario1.getCorreo());
        assertEquals("Calle Falsa 123", usuario1.getDireccion());
        assertEquals("clave123", usuario1.getPassword());
        assertEquals("temporal123", usuario1.getRawPassword());
        assertTrue(usuario1.isActivo());
        assertNotNull(usuario1.getRoles());

        // equals y hashCode
        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());

        // toString incluye nombre y correo
        assertTrue(usuario1.toString().contains("Maykol"));
        assertTrue(usuario1.toString().contains("maykol@mail.com"));
    }
}
