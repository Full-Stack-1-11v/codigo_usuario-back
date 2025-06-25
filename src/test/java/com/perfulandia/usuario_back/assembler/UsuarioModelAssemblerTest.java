package com.perfulandia.usuario_back.assembler;

import com.perfulandia.usuario_back.dto.UsuarioDTO;
import com.perfulandia.usuario_back.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioModelAssemblerTest {

    private final UsuarioModelAssembler assembler = new UsuarioModelAssembler();

    @Test
    void testToModel() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Maykol");

        EntityModel<UsuarioDTO> model = assembler.toModel(usuario);

        assertNotNull(model);
        assertEquals(1L, model.getContent().getId());
        assertEquals("Maykol", model.getContent().getNombre());
        assertTrue(model.getLinks().hasLink("self"));
    }
}
