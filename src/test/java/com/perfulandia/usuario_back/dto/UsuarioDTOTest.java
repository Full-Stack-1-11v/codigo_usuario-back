package com.perfulandia.usuario_back.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

    @Test
    void testGettersAndSetters() {
        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(1L);
        dto.setNombre("Maykol");
        dto.setApellido("Vargas");
        dto.setRut("12.345.678-9");
        dto.setCorreo("maykol@mail.com");
        dto.setDireccion("Calle Falsa 123");
        dto.setActivo(true);

        assertEquals(1L, dto.getId());
        assertEquals("Maykol", dto.getNombre());
        assertEquals("Vargas", dto.getApellido());
        assertEquals("12.345.678-9", dto.getRut());
        assertEquals("maykol@mail.com", dto.getCorreo());
        assertEquals("Calle Falsa 123", dto.getDireccion());
        assertTrue(dto.isActivo());
    }
}
