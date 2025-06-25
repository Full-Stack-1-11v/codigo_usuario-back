package com.perfulandia.usuario_back.assembler;

import com.perfulandia.usuario_back.dto.RolDTO;
import com.perfulandia.usuario_back.model.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class RolModelAssemblerTest {

    private final RolModelAssembler assembler = new RolModelAssembler();

    @Test
    void testToModel() {
        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");

        EntityModel<RolDTO> model = assembler.toModel(rol);

        assertNotNull(model);
        assertEquals(1L, model.getContent().getId());
        assertEquals("ROLE_USER", model.getContent().getNombre());
        assertTrue(model.getLinks().hasLink("self"));
    }
}
