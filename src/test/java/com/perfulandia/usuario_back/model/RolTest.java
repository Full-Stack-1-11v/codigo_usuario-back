package com.perfulandia.usuario_back.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void testGettersSettersEqualsHashCodeToStringBuilder() {
        Rol rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("ROLE_ADMIN");

        Rol rol2 = new Rol(1L, "ROLE_ADMIN");

        Rol rol3 = Rol.builder().id(1L).nombre("ROLE_ADMIN").build();

        // Getters
        assertEquals(1L, rol1.getId());
        assertEquals("ROLE_ADMIN", rol1.getNombre());

        // equals y hashCode
        assertEquals(rol1, rol2);
        assertEquals(rol1.hashCode(), rol2.hashCode());

        // toString
        assertTrue(rol1.toString().contains("ROLE_ADMIN"));

        // builder
        assertEquals(1L, rol3.getId());
        assertEquals("ROLE_ADMIN", rol3.getNombre());
    }
}
