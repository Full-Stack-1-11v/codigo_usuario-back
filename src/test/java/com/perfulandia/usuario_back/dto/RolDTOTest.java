package com.perfulandia.usuario_back.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RolDTOTest {

    @Test
    void testGettersSettersEqualsHashCodeToString() {
        RolDTO rol1 = new RolDTO(1L, "ROLE_ADMIN");
        RolDTO rol2 = new RolDTO();
        rol2.setId(1L);
        rol2.setNombre("ROLE_ADMIN");

        assertEquals(rol1.getId(), rol2.getId());
        assertEquals(rol1.getNombre(), rol2.getNombre());

        assertEquals(rol1, rol2);  // Lombok @Data implementa equals()
        assertEquals(rol1.hashCode(), rol2.hashCode());  // Lombok también
        assertTrue(rol1.toString().contains("ROLE_ADMIN"));  // Lombok genera toString()
    }
}
