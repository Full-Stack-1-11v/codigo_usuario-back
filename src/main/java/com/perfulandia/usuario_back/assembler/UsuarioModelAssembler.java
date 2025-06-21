package com.perfulandia.usuario_back.assembler;

import com.perfulandia.usuario_back.controller.UsuarioController;
import com.perfulandia.usuario_back.dto.UsuarioDTO;
import com.perfulandia.usuario_back.model.Usuario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Ensamblador que convierte una entidad {@link Usuario} en un modelo {@link EntityModel}
 * que incluye su representación DTO y enlaces HATEOAS.
 * <p>
 * Esta clase permite agregar hipervínculos a los recursos expuestos por la API REST,
 * siguiendo el principio HATEOAS.
 * </p>
 */
@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioDTO>> {

    /**
     * Convierte una entidad {@link Usuario} a un modelo {@link UsuarioDTO} con enlaces HATEOAS.
     *
     * @param usuario La entidad {@code Usuario} a transformar.
     * @return Un {@code EntityModel<UsuarioDTO>} que contiene los datos y los enlaces de navegación.
     */
    @Override
    public EntityModel<UsuarioDTO> toModel(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setDireccion(usuario.getDireccion());
        dto.setRut(usuario.getRut());
        dto.setActivo(usuario.isActivo());

        return EntityModel.of(dto,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).getPedidosByUsuario(usuario.getId())).withRel("pedidos"),
                linkTo(methodOn(UsuarioController.class).desactivarUsuario(usuario.getId())).withRel("desactivar"));
    }
}
